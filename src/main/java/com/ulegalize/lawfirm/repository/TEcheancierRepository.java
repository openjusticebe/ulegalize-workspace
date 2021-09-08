package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TEcheancier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TEcheancierRepository extends JpaRepository<TEcheancier, Long>, JpaSpecificationExecutor<TEcheancier> {

}