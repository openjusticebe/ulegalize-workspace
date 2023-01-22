package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.Auth0Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Auth0Repository extends JpaRepository<Auth0Entity, Long> {
    Optional<Auth0Entity> findFirstBy();
}
