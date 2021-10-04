package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.model.enumeration.EnumValid;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import com.ulegalize.lawfirm.repository.VirtualRepository;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class UserV2ServiceImpl implements UserV2Service {

    private final VirtualRepository virtualRepository;

    private final TUsersRepository tUsersRepository;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    public UserV2ServiceImpl(TUsersRepository tUsersRepository, VirtualRepository virtualRepository) {
        this.tUsersRepository = tUsersRepository;
        this.virtualRepository = virtualRepository;
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
        user.setHashkey("");
        user.setIdValid(EnumValid.UNVERIFIED);
        user.setLoginCount(0L);
        user.setUserpass("");
        String alias = userEmail.toLowerCase().substring(0, userEmail.indexOf("@"));
        user.setAliasPublic(alias);
        user.setSpecialities("");
        user.setLanguage(EnumLanguage.FR.getShortCode());

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
}
