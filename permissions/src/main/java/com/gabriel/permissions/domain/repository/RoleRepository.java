package com.gabriel.permissions.domain.repository;

import com.gabriel.permissions.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
