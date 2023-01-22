package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TMessageRepository extends JpaRepository<TMessage, Long>, JpaSpecificationExecutor<TMessage> {
}
