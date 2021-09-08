package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TRefCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TRefCurrencyRepository extends JpaRepository<TRefCurrency, String>, JpaSpecificationExecutor<TRefCurrency> {

}