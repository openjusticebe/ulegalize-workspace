package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TMessageUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TMessageUserRepository extends JpaRepository<TMessageUser, String>, JpaSpecificationExecutor<TMessageUser> {

}