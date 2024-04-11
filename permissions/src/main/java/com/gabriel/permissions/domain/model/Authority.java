package com.gabriel.permissions.domain.model;

import com.gabriel.core.domain.Entity;
import com.gabriel.core.domain.model.Description;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@jakarta.persistence.Entity
@RequiredArgsConstructor
@Data
public class Authority extends Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Authority cannot be blank")
    @Size(max = 255, message = "Authority name cannot exceed 255 characters")
    @Pattern(regexp = "^[^:]+:(write|read|update|delete)$", message = "Authority must follow the pattern <resource>:<write|read|update|delete>")
    @Column(unique = true)
    private String name;

    @Valid
    @Embedded
    private Description description;

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;
}
