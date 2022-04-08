package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.LawfirmUserDTO;
import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.converter.EntityToLawfirmUserDTOConverter;
import com.ulegalize.lawfirm.model.converter.EntityToUserConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.model.enumeration.EnumValid;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import com.ulegalize.lawfirm.repository.VirtualRepository;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import com.ulegalize.lawfirm.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserV2ServiceImpl implements UserV2Service {

    private final VirtualRepository virtualRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final EntityToUserConverter entityToUserConverter;
    private final EntityToLawfirmUserDTOConverter entityToLawfirmUserDTOConverter;

    private final TUsersRepository tUsersRepository;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    public UserV2ServiceImpl(TUsersRepository tUsersRepository, VirtualRepository virtualRepository, EntityToUserConverter entityToUserConverter, LawfirmUserRepository lawfirmUserRepository, EntityToLawfirmUserDTOConverter entityToLawfirmUserDTOConverter) {
        this.tUsersRepository = tUsersRepository;
        this.virtualRepository = virtualRepository;
        this.entityToUserConverter = entityToUserConverter;
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.entityToLawfirmUserDTOConverter = entityToLawfirmUserDTOConverter;
    }

    @Override
    public void changeLanguage(Long userId, String language) {
        Optional<TUsers> usersOptional = tUsersRepository.findById(userId);

        if (language == null || language.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "language not found");
        }
        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(language);
        if (enumLanguage == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "language not found");
        }
        usersOptional.ifPresent(user -> {
            user.setLanguage(language);
            tUsersRepository.save(user);
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TUsers createUsers(String userEmail, String clientFrom, EnumLanguage language) {
        TUsers user = new TUsers();
        user.setEmail(userEmail);
        String fullname = StringUtils.substringBefore(userEmail, "@");
        user.setFullname(fullname);
        user.setAvatar(new byte[0]);
        user.setInitiales("");
        // temporary
        user.setIdUser("ul" + String.valueOf(UUID.randomUUID()).substring(0, 4));
        user.setHashkey(Utils.generateHashkey());
        user.setIdValid(EnumValid.VERIFIED);
        user.setLoginCount(0L);
        user.setUserpass("");
        String alias = userEmail.toLowerCase().substring(0, userEmail.indexOf("@"));
        user.setAliasPublic(alias);
        user.setSpecialities("");
        user.setLanguage(EnumLanguage.FR.getShortCode());
        user.setClientFrom(clientFrom);

        TUsers save = tUsersRepository.save(user);

        user.setIdUser("#ul" + save.getId());

        tUsersRepository.save(user);

        log.debug("User {} created", user.getId());

        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        if (activeProfile.equalsIgnoreCase("dev")
                || activeProfile.equalsIgnoreCase("test")) {
            virtualRepository.deleteUser(userId);
        }
    }

    @Override
    public boolean verifyUser(String email, String hashkey) {
        log.info("Entering verifyUser email {} and hashkey {}", email, hashkey);
        if (email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found");
        }
        if (hashkey.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hashkey not found");
        }
        Optional<TUsers> usersOptional = tUsersRepository.findByEmail(email);

        if (usersOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is unknown");
        }

        TUsers tUsers = usersOptional.get();
        boolean resultHashkey = Objects.equals(tUsers.getHashkey(), hashkey);
        log.info("Result hashkey {}, hashkey is : {} and your hashkey is : {}", resultHashkey, tUsers.getHashkey(), hashkey);

        if (resultHashkey) {
            tUsers.setIdValid(EnumValid.VERIFIED);
            tUsersRepository.save(tUsers);
        }

        return resultHashkey;
    }

    @Override
    public TUsers findById(Long userId) {
        log.info("Entering findById userId {}", userId);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "userId not found");
        }
        Optional<TUsers> usersOptional = tUsersRepository.findById(userId);

        return usersOptional.orElseThrow();
    }

    @Override
    public List<LawyerDTO> findValid() {
        log.info("Entering findValid");
        return tUsersRepository.findDTOByValid(EnumValid.VERIFIED);
    }

    @Override
    public List<LawyerDTO> getLawfirmUsers(String vcKey) {
        log.info("Entering getLawfirmUsers vckey {}", vcKey);
        if (vcKey == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "userId not found");
        }
        List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findLawfirmUsersByVcKey(vcKey);

        return lawfirmUsersList.stream().map(lawfirmUsers -> {
            return entityToUserConverter.apply(lawfirmUsers.getUser(), false);
        }).collect(Collectors.toList());
    }

    @Override
    public List<LawfirmUserDTO> geLawfirmUserByVcKey(String vcKey) {
        log.info("Entering geLawfirmUserByVcKey vckey {}", vcKey);
        if (vcKey == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "userId not found");
        }
        List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findLawfirmUsersByVcKey(vcKey);

        return entityToLawfirmUserDTOConverter.convertToList(lawfirmUsersList);
    }
}
