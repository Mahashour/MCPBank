package com.example.banksys.client.domain.model;

import com.example.banksys.client.domain.exception.InvalidNationalIdException;

/**
 * Abstract base class for all national ID numbers.
 *
 * @see EgyptianNationalId
 * @see ForeignNationalId
 */
public abstract class NationalId {

    protected final String fullId;

    protected NationalId(String fullId) {
        if (fullId == null || fullId.isBlank())
            throw new InvalidNationalIdException("National ID must not be null");
        this.fullId = fullId;
    }

    public String getFullId() {
        return fullId;
    }

    public abstract String getMasked();

    /**
     * Factory method to make a national ID from two strings.
     *
     * @param type  EG for Egypt, FOREIGN otherwise.
     * @param value The full ID.
     * @return National ID object.
     */
    public static NationalId makeNationalId(String type, String value) {
        return switch (type) {
            case "EG" -> new EgyptianNationalId(value);
            case "FOREIGN" -> new ForeignNationalId(value);
            default -> throw new InvalidNationalIdException("Unknown national ID type: " + type);
        };
    }

    /**
     * Method to get the ID's country code. It is mostly to be used in {@link com.example.banksys.client.infrastructure.NationalIdConverter}.
     *
     * @return Either a 2-letter ISO-3166-2 code or "FOREIGN" if the country does not have a special ID number in the system.
     */
    public String getTypeCode() {
        return this instanceof EgyptianNationalId ? "EG" : "FOREIGN";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || (this.getClass() != o.getClass())) return false;
        NationalId that = (NationalId) o;
        return fullId.equals(that.fullId);
    }

    @Override
    public final int hashCode() {
        return java.util.Objects.hash(fullId, this.getClass());
    }

    @Override
    public String toString() {
        return this.getMasked();
    }

}
