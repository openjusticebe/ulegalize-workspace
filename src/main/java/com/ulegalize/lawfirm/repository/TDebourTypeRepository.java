package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.FraisAdminDTO;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.entity.TDebourType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TDebourTypeRepository extends JpaRepository<TDebourType, Long>, JpaSpecificationExecutor<TDebourType> {
    void deleteByVcKey(String vcKey);

    @Query("select new com.ulegalize.dto.ItemLongDto(m.idDebourType , m.description) " +
            " from TDebourType m where m.vcKey = :vcKey and m.archived = :archived order by m.description asc")
    List<ItemLongDto> findAllByVcKeyAndArchived(String vcKey, boolean archived);

    @Query("select new com.ulegalize.dto.FraisAdminDTO(m.idDebourType , m.idMesureType,  mt.description, m.pricePerUnit) " +
            " from TDebourType m " +
            " join TMesureType mt on mt.idMesureType = m.idMesureType" +
            " where m.idDebourType = :idDebourType and m.archived = :archived order by m.description asc")
    FraisAdminDTO getFraisMatiere(Long idDebourType, boolean archived);

    List<TDebourType> findAllByVcKey(String vcKey);

    @Query("select new com.ulegalize.dto.FraisAdminDTO(m.idDebourType , m.vcKey, m.description, m.pricePerUnit, mt.idMesureType, mt.description, m.archived) " +
            " from TDebourType m" +
            " join TMesureType mt on mt.idMesureType = m.idMesureType" +
            " where m.vcKey = :vcKey order by m.description asc")
    List<FraisAdminDTO> findAllItemByVcKey(String vcKey);

    @Query("select new com.ulegalize.dto.FraisAdminDTO(m.idDebourType , m.vcKey, m.description, m.pricePerUnit, mt.idMesureType, mt.description, m.archived) " +
            " from TDebourType m " +
            " join TMesureType mt on mt.idMesureType = m.idMesureType" +
            " where m.vcKey = :vcKey and m.idDebourType = :deboursTypeId order by m.description asc")
    FraisAdminDTO findDTOById(String vcKey, Long deboursTypeId);
}