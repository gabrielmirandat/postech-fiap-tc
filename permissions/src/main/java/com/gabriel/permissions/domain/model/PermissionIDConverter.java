package com.gabriel.permissions.domain.model;

import com.gabriel.model.PermissionId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PermissionIDConverter implements AttributeConverter<PermissionId, String> {

    @Override
    public String convertToDatabaseColumn(PermissionId attribute) {
        if (attribute != null) {
            return attribute.getValue();
        }
        return null;
    }

    @Override
    public PermissionId convertToEntityAttribute(String dbData) {
        if (dbData != null) {
            return PermissionId.newBuilder().setValue(dbData).build();
        }
        return null;
    }
}
