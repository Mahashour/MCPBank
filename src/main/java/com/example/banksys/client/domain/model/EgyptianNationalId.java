package com.example.banksys.client.domain.model;

import java.time.DateTimeException;
import java.time.LocalDate;

import com.example.banksys.client.domain.exception.InvalidNationalIdException;

/**
 * An immutable value object representing a valid Egyptian National ID number.
 * <p>
 * The 14-digit string is validated during construction.
 * <p>
 * <strong>Validation rules:</strong>
 * <ul>
 *     <li>Exactly 14 digits.</li>
 *     <li>Digit 1 is 2 (1900s) or 3 (2000s)</li>
 *     <li>Digits 2-7 are a valid date in the past</li>
 *     <li>Digits 8-9 are a known governorate code</li>
 *     <li>Digits 10-13 are the 4-digit sequence number, the last digit determining gender
 *     (odd = male, even = female).</li>
 *     <li>Digit 14 is a check-digit with a modulus-11 algorithm with alternating weights.</li>
 * </ul>
 *
 * @see NationalId
 * @see Governorate
 * @see Gender
 */
public final class EgyptianNationalId extends NationalId {

    private final LocalDate dateOfBirth;
    private final Gender gender;
    private final Governorate registrationGovernorate;
    private final String sequenceNumber;

    /**
     * Creates a new Egyptian national ID from 14-digit string (the ID).
     *
     * @param fullId The 14-digit ID.
     * @throws InvalidNationalIdException if the ID fails any structural rule / checksum
     */
    public EgyptianNationalId(String fullId) {
        super(fullId);
        validateLengthAndDigits(fullId);

        // century number assignment
        char centuryNumber = fullId.charAt(0);
        if (centuryNumber != '2' && centuryNumber != '3')
            throw new InvalidNationalIdException("First digit must be 2 or 3.");

        // date of birth assignment
        int century = centuryNumber == '2' ? 1900 : 2000;
        int year = century + Integer.parseInt(fullId.substring(1, 3));
        int month = Integer.parseInt(fullId.substring(3, 5));
        int day = Integer.parseInt(fullId.substring(5, 7));

        try {
            this.dateOfBirth = LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            throw new InvalidNationalIdException("Invalid birth date (digits 2-7)", e);
        }

        // governorate assignment
        String govCode = fullId.substring(7, 9);
        if (!(Governorate.isValidNationalGovernorateCode(govCode))) {
            throw new InvalidNationalIdException("Unknown governorate code (digits 8 & 9): " + govCode);
        }
        this.registrationGovernorate = Governorate.fromNationalGovernorateCode(govCode);

        // sequence number and gender assignment
        this.sequenceNumber = fullId.substring(9, 13);
        int genderDigit = Character.digit(fullId.charAt(12), 10);
        this.gender = (genderDigit % 2 == 0) ? Gender.FEMALE : Gender.MALE;

        if (!isValidCheckDigit(fullId)) {
            throw new InvalidNationalIdException("National ID check digit is invalid");
        }
    }

    private static void validateLengthAndDigits(String fullId) {
        if (fullId.length() != 14)
            throw new InvalidNationalIdException("National ID must be 14 characters long");
        if (!fullId.chars().allMatch(Character::isDigit))
            throw new InvalidNationalIdException("National ID must be only digits");
    }

    private static boolean isValidCheckDigit(String id) {
        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int digit = Character.digit(id.charAt(i), 10);
            int weight = (i % 2 == 0) ? 1 : 2;
            int product = digit * weight;
            if (product > 9) product -= 9;
            sum += product;
        }

        int expectedCheckDigit = sum % 11;
        if (expectedCheckDigit == 10) return false;
        int actualCheckDigit = Character.digit(id.charAt(13), 10);

        return actualCheckDigit == expectedCheckDigit;
    }

    // safe to use anywhere
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public Governorate getRegistrationGovernorate() {
        return registrationGovernorate;
    }

    @Override
    public String getMasked() {
        return fullId.substring(0, 9) + "*****";
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }
}
