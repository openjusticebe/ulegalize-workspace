package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface LawfirmUserRepository extends CrudRepository<LawfirmUsers, Long> {

    @Query(value = "SELECT l from LawfirmUsers l where l.lawfirm.vckey = ?1")
    List<LawfirmUsers> findLawfirmUsersByVcKey(String vcKey);

    @Query(value = "SELECT l from LawfirmUsers l where l.lawfirm.vckey = ?1 and l.user.id = ?2")
    Optional<LawfirmUsers> findLawfirmUsersByVcKeyAndUserId(String vcKey, Long userId);

    @Query(value = "SELECT l from LawfirmUsers l " +
            " where l.id = :vcu_id" +
            " and not exists (select dr.vcOwner from TDossierRights dr where dr.vcUserId = l.id and dr.dossierId = :dossierId)")
    Optional<LawfirmUsers> findByVcIdAndDossier_NotExist(@Param("vcu_id") Long vcu_id, @Param("dossierId") Long dossierId);

    @Query(value = "SELECT l from LawfirmUsers l " +
            " where l.lawfirm.vckey = :vckey and l.user.id in :userIdList" +
            " and not exists (select dr.vcOwner from TDossierRights dr where dr.vcUserId = l.id and dr.dossierId = :dossierId)")
    List<LawfirmUsers> findByVcAndUserIdAndDossier_NotExist(@Param("vckey") String vckey, @Param("userIdList") List<Long> userIdList, @Param("dossierId") Long dossierId);

    @Query(value = "SELECT l from LawfirmUsers l where l.lawfirm.vckey = :vcKey and not exists (select dr.vcOwner from TDossierRights dr where dr.vcUserId = l.id and dr.dossierId = :dossierId)")
    List<LawfirmUsers> findByVcKeyAndDossier_NotExist(@Param("vcKey") String vcKey, @Param("dossierId") Long dossierId);

    @Query("select lu from LawfirmUsers lu join lu.user u where u.id = :userId and lu.isPublic = true")
    List<LawfirmUsers> findPublicUsersByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT l from LawfirmUsers l " +
            " join fetch l.lawfirm law " +
            " join fetch law.lawfirmWebsite lw" +
            " where l.user.id = ?1")
    List<LawfirmUsers> findLawfirmUsersByUserId(Long userId);

    @Query(value = "SELECT l from LawfirmUsers l where l.user.id = :userId and l.lawfirm.temporaryVc = :temporary")
    List<LawfirmUsers> findLawfirmUsersByUserIdAndTemporary(Long userId, Boolean temporary);

    /*
    must be an optional , to avoid issue get a list
     */
    @Query(value = "SELECT l from LawfirmUsers l join fetch l.user u where u.id = ?1 and l.isSelected = ?2 and l.isActive = true")
    List<LawfirmUsers> findLawfirmUsersByUserIdAndIsSelected(Long userId, Boolean selected);

    @Query(value = "delete from LawfirmUsers l where l.id = ?1")
    @Modifying
    void deleteByLawfirmUsersId(Long id);

}
