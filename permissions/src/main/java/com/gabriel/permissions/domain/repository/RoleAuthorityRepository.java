package com.gabriel.permissions.domain.repository;

import com.gabriel.permissions.domain.model.RoleAuthority;
import com.gabriel.permissions.domain.model.RoleAuthorityKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, RoleAuthorityKey> {
}
