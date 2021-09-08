package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface LawfirmRepository extends CrudRepository<LawfirmEntity, String> {

    @Query(value = "SELECT l from LawfirmEntity l where lower(l.alias) = ?1")
    LawfirmEntity findLawfirmByAlias(String alias);

    @Query(value = "SELECT l from LawfirmEntity l where upper(l.vckey) = ?1")
    Optional<LawfirmEntity> findLawfirmByVckey(String vcKey);

    @Query(value = "SELECT new com.ulegalize.dto.LawfirmDTO(   " + "l.vckey," + "l.name," + "l.alias,"
            + "l.abbreviation," + "l.companyNumber," + "l.objetsocial," + "l.currency," + "l.website,"
            + "l.couthoraire," + "l.startInvoiceNumber," + "l.street," + "l.city," + "l.postalCode,"
            + "l.countryCode," + "l.email," + "l.phoneNumber," + "l.fax," + "l.notification," + "l.logo," + "l.driveType)"
            + " from LawfirmEntity l where upper(l.vckey) = ?1")
    Optional<LawfirmDTO> findLawfirmDTOByVckey(String vcKey);

    @Query(value = "SELECT new com.ulegalize.dto.LawfirmDTO(   " + "l.vckey," + "l.name," + "l.alias,"
            + "l.abbreviation," + "l.companyNumber," + "l.objetsocial," + "l.currency," + "l.website,"
            + "l.couthoraire," + "l.startInvoiceNumber," + "l.street," + "l.city," + "l.postalCode,"
            + "l.countryCode," + "l.email," + "l.phoneNumber," + "l.fax," + "l.notification," + "l.logo," + "l.driveType)"
            + " from LawfirmEntity l where upper(l.vckey) like concat('%', ?1, '%')")
    List<LawfirmDTO> searchLawfirmDTOByVckey(String vcKey);

    @Query(value = "SELECT l from LawfirmEntity l where l.alias like CONCAT(?1,'%') and l.licenseId = 1 and l.lawfirmWebsite.active = true")
    List<LawfirmEntity> findLawfirmLikeByAlias(String alias);

    @Query(value = "SELECT l from LawfirmEntity l where l.licenseId = 1 and l.lawfirmWebsite.active = true")
    List<LawfirmEntity> findAllByLicenseId();

    List<LawfirmEntity> findAll();

}