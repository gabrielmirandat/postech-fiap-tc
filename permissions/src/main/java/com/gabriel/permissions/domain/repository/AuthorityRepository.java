package com.gabriel.permissions.domain.repository;

import com.gabriel.permissions.domain.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
}
