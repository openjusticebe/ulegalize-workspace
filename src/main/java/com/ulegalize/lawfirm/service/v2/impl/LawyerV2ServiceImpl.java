package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.DTOToUserEntityConverter;
import com.ulegalize.lawfirm.model.converter.EntityToUserConverter;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import com.ulegalize.lawfirm.service.v2.LawyerV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
@Transactional
public class LawyerV2ServiceImpl implements LawyerV2Service {
    private final TUsersRepository userRepository;
    private final EntityToUserConverter entityToUserConverter;
    private final DTOToUserEntityConverter dtoToUserEntityConverter;

    public LawyerV2ServiceImpl(TUsersRepository userRepository, EntityToUserConverter entityToUserConverter, DTOToUserEntityConverter dtoToUserEntityConverter) {
        this.userRepository = userRepository;
        this.entityToUserConverter = entityToUserConverter;
        this.dtoToUserEntityConverter = dtoToUserEntityConverter;
    }

    @Override
    public LawyerDTO getLawyer() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering getLayer {}", lawfirmToken.getUserId());
        Optional<TUsers> userOptional = userRepository.findById(lawfirmToken.getUserId());
        if (!userOptional.isPresent()) {
            log.error("Unknown user {} ", lawfirmToken.getUserId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown user " + lawfirmToken.getUserId());
        }
        return entityToUserConverter.apply(userOptional.get(), false);
    }

    @Override
    public Long updateLawyer(LawyerDTO lawyerDTO) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering updateLawyer {}", lawfirmToken.getUserId());
        Optional<TUsers> userOptional = userRepository.findById(lawyerDTO.getId());
        if (!userOptional.isPresent()) {
            log.error("Unknown user {} ", lawfirmToken.getUserId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown user " + lawfirmToken.getUserId());
        }
        if (!lawyerDTO.getAlias().matches("^[a-zà-ú0-9-+.]+")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alias is not matching user " + lawyerDTO.getAlias());
        }

        TUsers tUsers = dtoToUserEntityConverter.apply(lawyerDTO, userOptional.get());

        userRepository.save(tUsers);
        log.info("User updated {}", tUsers.getId());

        return tUsers.getId();
    }

    @Override
    public Long updateLawyerLogo(byte[] bytes) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering updateLawyerLogo {}", lawfirmToken.getUserId());
        Optional<TUsers> userOptional = userRepository.findById(lawfirmToken.getUserId());
        if (!userOptional.isPresent()) {
            log.error("Unknown user {} ", lawfirmToken.getUserId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown user " + lawfirmToken.getUserId());
        }

        userOptional.get().setAvatar(bytes);

        userRepository.save(userOptional.get());
        log.info("User avatar updated {}", userOptional.get().getId());

        return userOptional.get().getId();
    }
}
