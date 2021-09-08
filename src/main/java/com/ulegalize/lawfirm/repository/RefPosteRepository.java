package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.AccountingTypeDTO;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.lawfirm.model.entity.RefPoste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RefPosteRepository extends JpaRepository<RefPoste, Integer>, JpaSpecificationExecutor<RefPoste> {
    @Query("select new com.ulegalize.dto.ItemDto(m.idPoste , m.refPoste) " +
            " from RefPoste m where m.vcKey = :vcKey and m.archived = :archived order by m.refPoste")
  List<ItemDto> findAllByVcKeyAndArchived(String vcKey, Boolean archived);

    @Query("select new com.ulegalize.dto.ItemDto(m.idPoste , m.refPoste) " +
            " from RefPoste m" +
            " where m.vcKey = :vcKey" +
            " and m.fraisProcedure = true" +
            " and m.archived = :archived" +
            " order by m.refPoste")
  List<ItemDto> findAllDeboursByVcKeyAndArchived(String vcKey, Boolean archived);

    @Query("select new com.ulegalize.dto.ItemDto(m.idPoste , m.refPoste) " +
            " from RefPoste m" +
            " where m.vcKey = :vcKey" +
            " and m.fraisCollaboration = true" +
            " and m.archived = :archived" +
            " order by m.refPoste")
  List<ItemDto> findAllFraisCollaByVcKeyAndArchived(String vcKey, Boolean archived);

    @Query("select new com.ulegalize.dto.ItemDto(m.idPoste , m.refPoste) " +
            " from RefPoste m" +
            " where m.vcKey = :vcKey" +
            " and m.honoraires = true" +
            " and m.archived = :archived" +
            " order by m.refPoste")
  List<ItemDto> findAllHonoraireByVcKeyAndArchived(String vcKey, Boolean archived);

  void deleteByVcKey(String vcKey);

  List<RefPoste> findAllByVcKey(String vcKey);


    Optional<RefPoste> findFirstByVcKeyAndHonoraires(String vcKey, Boolean honoraire);

    @Query("select new com.ulegalize.dto.AccountingTypeDTO(m.idPoste , m.vcKey, m.refPoste, " +
            "m.userUpd, m.dateUpd, m.archived, m.fraisProcedure, m.honoraires, m.fraisCollaboration, m.facturable) " +
            " from RefPoste m" +
            " where m.vcKey = :vcKey order by m.refPoste asc")
    List<AccountingTypeDTO> findAllItemByVcKey(String vcKey);

    @Query("select new com.ulegalize.dto.AccountingTypeDTO(m.idPoste , m.vcKey, m.refPoste, " +
            "m.userUpd, m.dateUpd, m.archived, m.fraisProcedure, m.honoraires, m.fraisCollaboration, m.facturable) " +
            " from RefPoste m" +
            " where m.vcKey = :vcKey and m.idPoste = :refPosteId")
    AccountingTypeDTO findDTOById(String vcKey, Integer refPosteId);
}