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
public class Role extends Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Role cannot be blank")
    @Size(max = 255, message = "Role name cannot exceed 255 characters")
    @Pattern(regexp = "POSTECH_[a-zA-Z0-9]+", message = "Role must follow the pattern POSTECH_ followed by alphanumeric characters")
    @Column(unique = true)
    private String name;

    @Valid
    @Embedded
    private Description description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_authority",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities;
}
