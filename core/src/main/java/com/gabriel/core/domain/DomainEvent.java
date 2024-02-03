package com.gabriel.core.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public interface DomainEvent {

    default String id() {
        return UUID.randomUUID().toString();
    }

    default String audience() {
        return "external-bounded-context";
    }

    default String context() {
        return "domain";
    }

    public String source();

    public String subject();

    public String type();

    // serialized bytes of data
    public byte[] payload(ObjectMapper serializer) throws JsonProcessingException;
}
