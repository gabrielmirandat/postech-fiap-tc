package com.gabriel.permissions.domain.model;

import com.gabriel.core.domain.model.id.PermissionID;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PermissionIDConverter implements AttributeConverter<PermissionID, String> {

    @Override
    public String convertToDatabaseColumn(PermissionID attribute) {
        if (attribute != null) {
            return attribute.getId();
        }
        return null;
    }

    @Override
    public PermissionID convertToEntityAttribute(String dbData) {
        if (dbData != null) {
            return new PermissionID(dbData);
        }
        return null;
    }
}
