package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TVirtualcabVat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TVirtualcabVatRepository extends JpaRepository<TVirtualcabVat, Long>, JpaSpecificationExecutor<TVirtualcabVat> {
    void deleteByVcKey(String vcKey);

    List<TVirtualcabVat> findAllByVcKeyAndVATIsNotNull(String vcKey);

    List<TVirtualcabVat> findAllByVcKey(String vcKey);

    Long countAllByVcKey(String vcKey);

    Optional<TVirtualcabVat> findAllByVcKeyAndVAT(String vcKey, BigDecimal vat);

    Optional<TVirtualcabVat> findByVcKeyAndIsDefaultAndVATIsNotNull(String vcKey, Boolean isDefault);
}