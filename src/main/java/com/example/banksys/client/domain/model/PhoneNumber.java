package com.example.banksys.client.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;

/**
 * Embeddable class for holding a phone number in the E.164 format.
 */
@Embeddable
public class PhoneNumber {

    @Column(nullable = false, length = 16)
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Must be a valid E.164 number, e.g. +201234567890")
    private String number;

    public PhoneNumber(){}

    public PhoneNumber(String number){
        this();
        this.setNumber(number);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if(!number.matches("^\\+[1-9]\\d{1,14}$"))
            throw new IllegalArgumentException("Invalid E.164 number: " + number);
        this.number = number;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof PhoneNumber that)) return false;
        return number.equals(that.number);
    }

    @Override
    public int hashCode(){
        return number.hashCode();
    }
}
