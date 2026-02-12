package com.gabriel.permissions.domain.model

import com.gabriel.model.PermissionId
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class PermissionIDConverter : AttributeConverter<PermissionId, String> {

    override fun convertToDatabaseColumn(attribute: PermissionId?): String? {
        return attribute?.value
    }

    override fun convertToEntityAttribute(dbData: String?): PermissionId? {
        return dbData?.let {
            PermissionId.newBuilder().setValue(it).build()
        }
    }
}
