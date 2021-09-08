package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.model.enumeration.EnumValid;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TUsersRepository extends CrudRepository<TUsers, Long> {
    @Query(value = "SELECT lu.user from LawfirmUsers lu where lu.isPublic = true group by lu.user.id")
    List<TUsers> findAllByPublic();

    @Query("select u from TUsers u inner join u.lawfirmUsers lu where u.fullname like CONCAT('%', :fullName,'%') and u.specialities like CONCAT('%',:specialities,'%') and lu.isPublic = true group by u.id")
    List<TUsers> findUsersByFullName(@Param("fullName") String fullName, @Param("specialities") String specialities);

    @Query("select u from TUsers u inner join u.lawfirmUsers lu where u.aliasPublic = :aliasPublic and lu.isPublic = true")
    Optional<TUsers> findPublicUserByAliasPublic(@Param("aliasPublic") String aliasPublic);

    @Query("select new com.ulegalize.dto.LawyerDTO(u.id, u.idUser, u.email, u.language, u.fullname) from TUsers u where u.email = :email")
    Optional<LawyerDTO> findDTOByEmail(String email);

    Optional<TUsers> findByEmail(String email);

    @Query("select u from TUsers u" +
            " where u.idValid = :enumValid" +
            " and u.valid = :isValid" +
            " and (u.fullname like CONCAT('%', :searchValue,'%') " +
            " or u.email like CONCAT('%', :searchValue,'%') )")
    List<TUsers> findBySearchAndIdValidAndValid(String searchValue, EnumValid enumValid, boolean isValid);
}