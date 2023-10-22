package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.entities.enums.EntityType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

abstract class EntityID implements ID {

    protected String id;

    public EntityID(EntityType entityType) {
        String generated = generate(entityType);
        validate(generated);
        this.id = generated;
    }

    public EntityID(String id) {
        validate(id);
        this.id = id;
    }

    protected String generate(EntityType entityType) {
        String timestamp = UUID.randomUUID().toString().split("-")[0];
        LocalDate today = LocalDate.now();
        return String.format("%s-%s-%s", timestamp, entityType, today);
    }

    public void validate(String id) {
        String[] intrinsics = id.split("-");
        assert !intrinsics[0].isEmpty();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDate.parse(intrinsics[1], formatter)
                .format(formatter);
        assert formattedDate.equals(intrinsics[1]);

        assert EntityType.getAllDefaultReprs()
                .contains(intrinsics[2]);
    }
}
