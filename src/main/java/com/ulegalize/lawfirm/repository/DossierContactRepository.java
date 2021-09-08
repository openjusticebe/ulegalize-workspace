package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.DossierContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DossierContactRepository extends JpaRepository<DossierContact, Long>, JpaSpecificationExecutor<DossierContact> {

}