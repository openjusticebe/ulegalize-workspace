package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ItemStringDto;
import com.ulegalize.lawfirm.model.entity.TLangues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TLanguesRepository extends JpaRepository<TLangues, String>, JpaSpecificationExecutor<TLangues> {
    @Query("select new com.ulegalize.dto.ItemStringDto(l.idLg, l.lgDesc) from TLangues l order by l.lgDesc")
    public List<ItemStringDto> findAllOrderByLgDesc();
}