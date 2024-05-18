package com.gabriel.core.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.ValueObject;
import com.gabriel.core.domain.model.id.PermissionID;
import lombok.Getter;

import java.io.IOException;
import java.time.Instant;

@Getter
public class Permission extends ValueObject {

    private final PermissionID permissionID;

    private final Name roleName;

    private final Name authorityName;

    private final Instant timestamp;

    @JsonCreator
    public Permission(@JsonProperty("PermissionID") PermissionID permissionID,
                      @JsonProperty("roleName") Name roleName,
                      @JsonProperty("authorityName") Name authorityName,
                      @JsonProperty("timestamp") @JsonAlias("updateTimestamp") Instant timestamp) {
        this.permissionID = permissionID;
        this.roleName = roleName;
        this.authorityName = authorityName;
        this.timestamp = timestamp;
    }

    public static Permission deserialize(ObjectMapper deserializer, byte[] bytes) {
        try {
            return deserializer.readValue(bytes, Permission.class);
        } catch (IOException e) {
            throw new IllegalStateException("Error deserializing permission");
        }
    }

    public byte[] serialized(ObjectMapper serializer) {
        try {
            return serializer.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error serializing permission");
        }
    }
}
