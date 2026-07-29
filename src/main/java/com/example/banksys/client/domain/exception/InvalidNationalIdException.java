package com.example.banksys.client.domain.exception;

/**
 * An exception that is thrown when a validation check on a national ID fails.
 * @see com.example.banksys.client.domain.model.EgyptianNationalId
 */
public class InvalidNationalIdException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    public InvalidNationalIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNationalIdException(String message) {
        super(message);
    }

}
