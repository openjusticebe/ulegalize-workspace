package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.VatCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface VatCountryRepository extends JpaRepository<VatCountry, Long>, JpaSpecificationExecutor<VatCountry> {
    public List<VatCountry> findByIdCountryAlpha2(String idCountryAlpha2);
}