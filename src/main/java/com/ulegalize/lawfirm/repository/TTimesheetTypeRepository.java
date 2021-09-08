package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ItemDto;
import com.ulegalize.dto.PrestationTypeDTO;
import com.ulegalize.lawfirm.model.entity.TTimesheetType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TTimesheetTypeRepository extends CrudRepository<TTimesheetType, Integer>, JpaSpecificationExecutor<TTimesheetType> {
    void deleteByVcKey(String vcKey);

    @Query("select new com.ulegalize.dto.ItemDto(m.idTs , m.description) " +
            " from TTimesheetType m where m.vcKey = :vcKey and m.archived = :archived order by m.description asc")
    List<ItemDto> findAllByVcKeyAndArchived(String vcKey, Boolean archived);

    @Query("select new com.ulegalize.dto.PrestationTypeDTO(m.idTs , m.vcKey, m.description, m.userUpd, m.dateUpd, m.archived) " +
            " from TTimesheetType m where m.vcKey = :vcKey order by m.description asc")
    List<PrestationTypeDTO> findAllItemByVcKey(String vcKey);

    List<TTimesheetType> findAllByVcKey(String vcKey);


    @Query("select new com.ulegalize.dto.PrestationTypeDTO(m.idTs , m.vcKey, m.description, m.userUpd, m.dateUpd, m.archived) " +
            " from TTimesheetType m where m.vcKey = :vcKey and m.idTs = :prestationsType order by m.description asc")
    PrestationTypeDTO findDTOById(String vcKey, Integer prestationsType);
}