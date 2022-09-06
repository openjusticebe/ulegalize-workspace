package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TMessageUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TMessageUserRepository extends JpaRepository<TMessageUser, String>, JpaSpecificationExecutor<TMessageUser> {

    @Query("select t " +
            "from TMessageUser t " +
            "join TMessage tm on t.tMessage.id = tm.id " +
            "where t.userId = :userId and t.valid = true and t.dateTo >= :expiryDate")
    Optional<TMessageUser> findByUserId(Long userId, LocalDateTime expiryDate);
}