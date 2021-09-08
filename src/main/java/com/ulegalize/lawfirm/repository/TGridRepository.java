package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ItemDto;
import com.ulegalize.lawfirm.model.entity.TGrid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TGridRepository extends JpaRepository<TGrid, Integer>, JpaSpecificationExecutor<TGrid> {

    @Query("select new com.ulegalize.dto.ItemDto(g.ID, g.DESCRIPTION) from TGrid g")
    List<ItemDto> findAllList();
}