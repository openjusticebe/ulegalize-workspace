package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TFactureStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TFactureStatutRepository extends JpaRepository<TFactureStatut, Integer>, JpaSpecificationExecutor<TFactureStatut> {

}