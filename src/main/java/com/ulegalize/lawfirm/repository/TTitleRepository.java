package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TTitleRepository extends JpaRepository<TTitle, String>, JpaSpecificationExecutor<TTitle> {

}