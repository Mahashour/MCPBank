package com.example.banksys.client.infrastructure;

import com.example.banksys.client.domain.model.EgyptianNationalId;
import com.example.banksys.client.domain.model.NationalId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converts NationalId objects to a plain VARCHAR column and back.
 * <p>
 * In the database, the VARCHAR is formatted as the ID type, followed by a `, followed by the ID number.
 * <p>
 * For example: "FOREIGN`12345".
 */
@Converter(autoApply = true)
public class NationalIdConverter implements AttributeConverter<NationalId, String> {

    @Override
    public String convertToDatabaseColumn(NationalId attribute) {
        if (attribute == null) return null;
        String type = attribute.getTypeCode();
        return type + "`" + attribute.getFullId();
    }

    @Override
    public NationalId convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String[] values = dbData.split("`");
        if (values.length != 2)
            throw new IllegalArgumentException("ID formatted incorrectly");
        return NationalId.makeNationalId(values[0], values[1]);
    }
}
