package com.example.banksys.client.domain.model;

import com.example.banksys.client.domain.exception.InvalidNationalIdException;

/**
 * Creates a simple national ID number which validates a reasonable length during construction.
 * To be used if the country's specific national ID does not already have an existing model.
 *
 * @see NationalId
 */
public class ForeignNationalId extends NationalId {

    public ForeignNationalId(String fullId) {
        super(fullId);
        if((fullId.length() < 5) || (fullId.length() > 30))
            throw new InvalidNationalIdException("National ID is between 5 and 30 characters");
    }

    /**
     * Masks the fullId by masking half of it.
     * Used for toString. If the unmasked ID is needed, use {@link #getFullId()}.
     *
     * @return Returns the masked fullID string.
     */
    @Override
    public String getMasked() {
        int half = fullId.length()/2;
        return fullId.substring(0, half) + "*".repeat(fullId.length() - half);
    }
}
