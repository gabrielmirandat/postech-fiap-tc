package com.gabriel.orders.core.domain.valueobjects;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

public interface ID {

    default String generate() {
        return UUID.randomUUID().toString();
    }

    void validate(String id);
}
