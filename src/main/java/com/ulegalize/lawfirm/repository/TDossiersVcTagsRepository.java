package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TDossiersVcTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TDossiersVcTagsRepository extends JpaRepository<TDossiersVcTags, Long> {

    @Query("select tdvt.id " +
            " from TDossiersVcTags tdvt " +
            " where tdvt.tDossiers.idDoss = ?1 ")
    List<Long> findTagsByDossierID(Long dossierId);

    @Query("select tdvt " +
            " from TDossiersVcTags tdvt " +
            " where tdvt.tDossiers.idDoss = ?1 and tdvt.tVirtualCabTags.id = ?2")
    Optional<TDossiersVcTags> findDossierTagsByIdDossandTagsId(Long dossierId, Long TagsId);


}
