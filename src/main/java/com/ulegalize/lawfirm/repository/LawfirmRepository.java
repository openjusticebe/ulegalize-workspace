package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.enumeration.EnumStatusAssociation;
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
            + "l.couthoraire,l.startInvoiceNumber,l.startDossierNumber,l.street,l.city,l.postalCode,"
            + "l.countryCode,l.email,l.phoneNumber,l.fax,l.notification,l.logo,l.driveType)"
            + " from LawfirmEntity l where upper(l.vckey) = ?1")
    Optional<LawfirmDTO> findLawfirmDTOByVckey(String vcKey);

    @Query(value = "select l" +
            " from LawfirmEntity l" +
            " where l.vckey not like ?2" +
            "  and l.vckey like UPPER(CONCAT('%',?1,'%'))" +
            "  and l.vckey in (" +
            "    select (case when lawfirmSender.vckey not like ?2 then lawfirmSender.vckey else lawfirmRecipient.vckey end)" +
            "    from TWorkspaceAssociated w" +
            "    join w.lawfirmSender lawfirmSender" +
            "    left join w.lawfirmRecipient lawfirmRecipient" +
            "    where (" +
            "                  ((lawfirmSender.vckey like ?2)" +
            "                      or lawfirmRecipient.vckey like ?2)" +
            "                  and" +
            "                  (w.status = (?3))" +
            "              )" +
            ")")
    List<LawfirmEntity> searchLawfirmDTOByVckey(String vcKey, String vcKeySender, EnumStatusAssociation status);

    @Query(value = " select l" +
            " from LawfirmEntity l" +
            " where l.vckey not like ?2" +
            "  and l.vckey like UPPER(CONCAT('%',?1,'%'))" +
            "  and l.vckey not in (select (case when lawfirmSender.vckey not like ?2 then lawfirmSender.vckey else lawfirmRecipient.vckey end)" +
            "                    from TWorkspaceAssociated w" +
            " join w.lawfirmSender lawfirmSender" +
            " left join w.lawfirmRecipient lawfirmRecipient " +
            "                    where (lawfirmSender.vckey like ?2" +
            "                        or lawfirmRecipient.vckey like ?2)" +
            "                      and (w.status in (?3)))")
    List<LawfirmEntity> searchLawfirmDTOByVckeyAndStatusAssociation(String vcKey, String vcKeySender, List<EnumStatusAssociation> enumStatusAssociationList);

    @Query(value = "SELECT l from LawfirmEntity l where l.alias like CONCAT(?1,'%') and l.licenseId = 1 and l.lawfirmWebsite.active = true")
    List<LawfirmEntity> findLawfirmLikeByAlias(String alias);

    @Query(value = "SELECT l from LawfirmEntity l where l.licenseId = 1 and l.lawfirmWebsite.active = true")
    List<LawfirmEntity> findAllByLicenseId();

    @Query(value = "SELECT distinct l from LawfirmEntity l " +
            "join fetch l.lawfirmUsers lu " +
            "join fetch lu.user " +
            "left join fetch l.lawfirmWebsite " +
            "where l.licenseId = 1 and l.temporaryVc= false and l.vckey like UPPER(CONCAT('%',?1,'%'))" +
            " order by l.date_upd desc")
    List<LawfirmEntity> findAllByLicenseIdAndTemporaryVc(String searchCriteria);

    @Query(value = "SELECT new com.ulegalize.dto.LawfirmDTO( l.vckey,l.name,l.alias,"
            + "l.abbreviation,l.companyNumber,l.objetsocial,l.currency,l.website,"
            + "l.couthoraire,l.startInvoiceNumber,l.startDossierNumber,l.street,l.city,l.postalCode,"
            + "l.countryCode,l.email,l.phoneNumber,l.fax,l.notification,l.logo,l.driveType)" +
            " from LawfirmEntity l " +
            "join  l.lawfirmUsers lu " +
            "join  lu.user user " +
            "where user.id = ?1 and lu.isSelected = true")
    Optional<LawfirmDTO> findBySelected(Long userId);

    List<LawfirmEntity> findAll();
}
