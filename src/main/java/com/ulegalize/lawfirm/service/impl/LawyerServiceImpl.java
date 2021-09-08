package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.converter.EntityToUserConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import com.ulegalize.lawfirm.service.LawyerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class LawyerServiceImpl implements LawyerService {
    private final LawfirmUserRepository lawfirmUserRepository;
    private final TUsersRepository userRepository;
    private final EntityToUserConverter entityToUserConverter;

    @Autowired
    public LawyerServiceImpl(LawfirmUserRepository lawfirmUserRepository, TUsersRepository userRepository, EntityToUserConverter entityToUserConverter) {
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.userRepository = userRepository;
        this.entityToUserConverter = entityToUserConverter;
    }


    @Override
    public List<LawyerDTO> getFilterLawyer(String search, String name, String pref) throws LawfirmBusinessException {
        List<LawyerDTO> lawyerDTOS = new ArrayList<>();
        List<TUsers> userList;

        if (search != null && search.equalsIgnoreCase("all")) {
            userList = userRepository.findAllByPublic();
        } else {
            userList = userRepository.findUsersByFullName(name, pref);
        }
        userList.stream().forEach(user -> {
            List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findPublicUsersByUserId(user.getId());
            LawyerDTO lawyerDTO = entityToUserConverter.apply(user, false);
            lawyerDTO.setVcKeySelected(lawfirmUsersList.stream().map(LawfirmUsers::getLawfirm).map(LawfirmEntity::getVckey).collect(Collectors.toList()));
            lawyerDTOS.add(lawyerDTO);
        });

        return lawyerDTOS;
    }

    @Override
    public LawyerDTO getPublicLawyer(String aliasPublic) throws LawfirmBusinessException {
        Optional<TUsers> user = userRepository.findPublicUserByAliasPublic(aliasPublic);

        if (user.isPresent()) {
            return entityToUserConverter.apply(user.get(), true);
        }

        throw new LawfirmBusinessException("No lawyer found with alias = " + aliasPublic);
    }

    @Override
    public LawyerDTO getLayerByEmail(String userEmail) throws LawfirmBusinessException {
        Optional<TUsers> userOptional = userRepository.findByEmail(userEmail);
        if (!userOptional.isPresent()) {
            log.error("Unknown user {} ", userEmail);
            throw new LawfirmBusinessException("Unknown user " + userEmail);
        }
        LawyerDTO lawyerDTO = entityToUserConverter.apply(userOptional.get(), false);
        lawyerDTO.setVcKeySelected(userOptional.get().getLawfirmUsers().stream().map(LawfirmUsers::getLawfirm).map(LawfirmEntity::getVckey).collect(Collectors.toList()));

        return lawyerDTO;
    }
}
