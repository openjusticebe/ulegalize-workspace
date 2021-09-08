package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ItemStringDto;
import com.ulegalize.lawfirm.model.entity.TCountries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TCountriesRepository extends JpaRepository<TCountries, Integer>, JpaSpecificationExecutor<TCountries> {
    @Query("select new com.ulegalize.dto.ItemStringDto(c.alpha3, c.nomFrFr) from TCountries c order by c.nomFrFr")
    List<ItemStringDto> findAllOrderBy();

    @Query("select new com.ulegalize.dto.ItemStringDto(c.alpha2, c.nomFrFr) from TCountries c order by c.nomFrFr")
    List<ItemStringDto> findAllOrderByA2();
}