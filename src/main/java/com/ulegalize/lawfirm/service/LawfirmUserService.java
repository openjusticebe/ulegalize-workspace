package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.dto.LawfirmUserDTO;
import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.dto.SecurityGroupUserDTO;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface LawfirmUserService {
    public List<LawfirmDTO> getLawfirmsByUserId(Long userId) throws ResponseStatusException;

    List<LawfirmUserDTO> getLawfirmUsers(String vcKey);

    String switchLawfirm(Long userId, String newVcKeySelected);

    List<LawfirmDTO> getLawfirms();

    SecurityGroupUserDTO getLawfirmUserByUserId(Long userId);

    LawyerDTO updateRoleLawfirmUser(LawyerDTO lawyerDTO);

    LawfirmUserDTO updateIsPublicLawfirmUser(String vcKey, Long userId, String active);

    LawfirmUserDTO updateIsActiveLawfirmUser(String vcKey, Long userId, String isActive);
}
