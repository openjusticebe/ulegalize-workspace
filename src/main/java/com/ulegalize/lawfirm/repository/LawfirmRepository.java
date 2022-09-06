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

    @Query(value = "SELECT new com.ulegalize.dto.LawfirmDTO(   l.vckey,l.name,l.alias,"
            + "l.abbreviation,l.companyNumber,l.objetsocial,l.currency,l.website,"
            + "l.couthoraire,l.startInvoiceNumber,l.street,l.city,l.postalCode,"
            + "l.countryCode,l.email,l.phoneNumber,l.fax,l.notification,l.logo,l.driveType)"
            + " from LawfirmEntity l where upper(l.vckey) = ?1")
    Optional<LawfirmDTO> findLawfirmDTOByVckey(String vcKey);

    @Query(nativeQuery = true, value = "select l.*" +
            " from t_virtualcab l" +
            " where l.key not like ?2" +
            "  and l.key like UPPER(CONCAT('%',?1,'%'))" +
            "  and l.key in (" +
            "    select (if(w.id_sender not like ?2, w.id_sender, w.id_recipient))" +
            "    from t_workspace_associated w" +
            "    where (" +
            "                  ((w.id_sender like ?2)" +
            "                      or w.id_recipient like ?2)" +
            "                  and" +
            "                  (w.status like (?3))" +
            "              )" +
            ")")
    List<LawfirmEntity> searchLawfirmDTOByVckey(String vcKey, String vcKeySender, String status);

    @Query(nativeQuery = true, value = " select *" +
            " from t_virtualcab l" +
            " where l.key not like ?2" +
            "  and l.key like UPPER(CONCAT('%',?1,'%'))" +
            "  and l.key not in (select if(w.id_sender not like ?2, w.id_sender, w.id_recipient)" +
            "                    from t_workspace_associated w" +
            "                    where (w.id_sender like ?2" +
            "                        or w.id_recipient like ?2)" +
            "                      and (w.status in ('PENDING', 'ACCEPTED')))")
    List<LawfirmEntity> searchLawfirmDTOByVckeyAndStatusAssociation(String vcKey, String vcKeySender);

    @Query(value = "SELECT l from LawfirmEntity l where l.alias like CONCAT(?1,'%') and l.licenseId = 1 and l.lawfirmWebsite.active = true")
    List<LawfirmEntity> findLawfirmLikeByAlias(String alias);

    @Query(value = "SELECT l from LawfirmEntity l where l.licenseId = 1 and l.lawfirmWebsite.active = true")
    List<LawfirmEntity> findAllByLicenseId();

    @Query(value = "SELECT distinct l from LawfirmEntity l " +
            "join fetch l.lawfirmUsers lu " +
            "join fetch lu.user " +
            "left join fetch l.lawfirmWebsite " +
            "where l.licenseId = 1 and l.temporaryVc= false " +
            " order by l.date_upd desc")
    List<LawfirmEntity> findAllByLicenseIdAndTemporaryVc();

    List<LawfirmEntity> findAll();

}