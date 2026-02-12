package com.gabriel.permissions.domain.model

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.sql.Timestamp
import java.time.Instant

@Converter(autoApply = true)
class InstantAttributeConverter : AttributeConverter<Instant, Timestamp> {

    override fun convertToDatabaseColumn(instant: Instant?): Timestamp? {
        return instant?.let { Timestamp.from(it) }
    }

    override fun convertToEntityAttribute(timestamp: Timestamp?): Instant? {
        return timestamp?.toInstant()
    }
}
