package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TMatiereRubriques;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TMatiereRubriquesRepository extends JpaRepository<TMatiereRubriques, Long>, JpaSpecificationExecutor<TMatiereRubriques> {

}