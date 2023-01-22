package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.dto.LawfirmUserDTO;
import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.dto.SecurityGroupUserDTO;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface LawfirmUserService {
    List<LawfirmDTO> getLawfirmsByUserId(Long userId) throws ResponseStatusException;

    List<LawfirmUserDTO> getLawfirmUsers(String vcKey);

    String switchLawfirm(Long userId, String newVcKeySelected);

    SecurityGroupUserDTO getLawfirmUserByUserId(Long userId);

    LawyerDTO updateRoleLawfirmUser(LawyerDTO lawyerDTO);

    LawfirmUserDTO updateIsPublicLawfirmUser(String vcKey, Long userId, String active);

}
