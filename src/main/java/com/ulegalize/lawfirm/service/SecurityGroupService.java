package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.*;
import com.ulegalize.lawfirm.model.LawfirmToken;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface SecurityGroupService {

    LawfirmToken getUserProfile(String clientFrom, String email, String token, boolean withSecurity);

    LawfirmToken getSimpleUserProfile(String email, String token);

    List<LawyerDTO> getFullUserResponsableList(String vcKey);

    List<SecurityGroupDTO> getSecurityGroup();

    Integer createUserSecurity(Long userId, SecurityGroupUserDTO securityGroupUserDTO) throws ResponseStatusException;

    Long deleteSecurityUsersGroup(Long securityGroupId);

    boolean existSecurityGroupByName(String securityGroupName);

    Long createSecurityGroup(Long userId, String newName);

    Long deleteSecurityGroup(Long securityGroupId);

    List<SecurityGroupUserDTO> getSecurityUserGroupBySecurityGroupId(Long securityGroupId);

    List<LawyerDTO> getOutSecurityUserGroupBySecurityGroupId(Long securityGroupId);

    Long addUserSecurity(Long securitySecurityId, Long userId);

    Long deleteSecurityGroupById(Long securityGroupUserId);

    List<ItemLongDto> getSecurityRightGroupBySecurityGroupId(Long securityGroupId);

    List<ItemDto> getOutSecurityRightGroupBySecurityGroupId(Long securityGroupId);

    Long addRightSecurity(Long securityGroupId, Integer rightId);

    Long deleteSecurityGroupRightById(Long securityGroupRightId);
}
