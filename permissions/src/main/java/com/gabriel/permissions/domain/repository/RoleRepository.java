package com.gabriel.permissions.domain.repository;

import com.gabriel.permissions.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String name);

    void deleteByName(String name);

    void deleteAll();
}
