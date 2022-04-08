package com.ulegalize.lawfirm.repository;

import com.ulegalize.enumeration.EnumSecurityAppGroups;
import com.ulegalize.lawfirm.model.entity.TSecurityGroupRights;
import com.ulegalize.lawfirm.model.entity.TSecurityGroupUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TSecurityGroupUsersRepository extends JpaRepository<TSecurityGroupUsers, Long>, JpaSpecificationExecutor<TSecurityGroupUsers> {
    @Query("select securityGroup.tSecurityGroupRightsList" +
            " from TSecurityGroupUsers tsgu " +
            " join tsgu.tSecurityGroups securityGroup " +
            " where tsgu.user.id = :idUser " +
            " and securityGroup.vcKey = :vcKey" +
            " and securityGroup.tSecAppGroupId in :enumSecurityAppGroups ")
    public List<TSecurityGroupRights> findByIdUserAndVckey(Long idUser, String vcKey, List<EnumSecurityAppGroups> enumSecurityAppGroups);

    @Query("select securityGroup.tSecurityGroupRightsList" +
            " from TSecurityGroupUsers tsgu " +
            " join tsgu.tSecurityGroups securityGroup " +
            " where tsgu.user.id = :idUser " +
            " and securityGroup.vcKey = :vcKey" +
            " and securityGroup.tSecAppGroupId = :enumSecurityAppGroups ")
    public List<TSecurityGroupRights> findByIdUserAndVckeyAndEqual(Long idUser, String vcKey, EnumSecurityAppGroups enumSecurityAppGroups);

    @Query("select tsgu" +
            " from TSecurityGroupUsers tsgu " +
            " join tsgu.user.lawfirmUsers lawfirmUsers " +
            " join tsgu.tSecurityGroups securityGroup " +
            " where securityGroup.vcKey = :vcKey" +
            " and lawfirmUsers.lawfirm.vckey = :vcKey" +
            " and lawfirmUsers.isActive = true" +
            " and securityGroup.tSecAppGroupId = :enumSecurityAppGroup ")
    public List<TSecurityGroupUsers> findByVckeyAndRight(String vcKey, EnumSecurityAppGroups enumSecurityAppGroup);

    @Query("select tsgu" +
            " from TSecurityGroupUsers tsgu " +
            " join tsgu.tSecurityGroups securityGroup " +
            " where securityGroup.id = :secGroupId " +
            " and securityGroup.vcKey = :vcKey" +
            " and securityGroup.tSecAppGroupId = :enumSecurityAppGroup ")
    public List<TSecurityGroupUsers> findBySecGroupUserIdAndVckey(Long secGroupId, String vcKey, EnumSecurityAppGroups enumSecurityAppGroup);


    @Query("select tsgu " +
            " from TSecurityGroupUsers tsgu " +
            " join tsgu.tSecurityGroups sg " +
            " where sg.id = :securityGroupId " +
            " and sg.vcKey = :vcKey ")
    public List<TSecurityGroupUsers> findByTSecurityGroupsIdAndVckey(Long securityGroupId, String vcKey);

    @Query("select tsgu " +
            " from TSecurityGroupUsers tsgu " +
            " join tsgu.tSecurityGroups sg" +
            " join tsgu.user user" +
            " where user.id = :id and sg.vcKey = :vcKey ")
    public List<TSecurityGroupUsers> findByIdAndVckey(Long id, String vcKey);


    @Query(value = "delete from TSecurityGroupUsers l where l.id in ?1")
    @Modifying
    void deleteAllById(List<Long> ids);
}