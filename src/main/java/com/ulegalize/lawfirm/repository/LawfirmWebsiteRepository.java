package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.LawfirmWebsiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LawfirmWebsiteRepository extends JpaRepository<LawfirmWebsiteEntity, String>, JpaSpecificationExecutor<LawfirmWebsiteEntity> {

    Optional<LawfirmWebsiteEntity> findAllByVcKey(String vcKey);
}