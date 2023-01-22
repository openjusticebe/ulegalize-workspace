package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.EmailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface EmailsEntityRepository extends JpaRepository<EmailsEntity, Long>, JpaSpecificationExecutor<EmailsEntity> {

    @Query(value = "SELECT d from EmailsEntity d where d.reminderSent = false and d.reminderDate < :now")
    List<EmailsEntity> findEmailsEntitiesByReminderDate(ZonedDateTime now);

}