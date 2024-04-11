package com.gabriel.permissions.domain.repository;

import com.gabriel.permissions.domain.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
