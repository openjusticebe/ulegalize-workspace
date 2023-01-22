package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.LawfirmUserDTO;
import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.model.converter.EntityToLawfirmUserDTOConverter;
import com.ulegalize.lawfirm.model.converter.EntityToUserConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TMessageUser;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.repository.TMessageUserRepository;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import com.ulegalize.lawfirm.repository.VirtualRepository;
import com.ulegalize.lawfirm.rest.AuthApi;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import com.ulegalize.lawfirm.service.v2.cache.CacheService;
import com.ulegalize.lawfirm.service.v2.cache.CacheUtils;
import com.ulegalize.lawfirm.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserV2ServiceImpl implements UserV2Service {

    private final VirtualRepository virtualRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final EntityToUserConverter entityToUserConverter;
    private final EntityToLawfirmUserDTOConverter entityToLawfirmUserDTOConverter;
    private final AuthApi authApi;

    private final TMessageUserRepository tMessageUserRepository;

    private final TUsersRepository tUsersRepository;
    private final CacheService cacheService;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    public UserV2ServiceImpl(TUsersRepository tUsersRepository, VirtualRepository virtualRepository, EntityToUserConverter entityToUserConverter, LawfirmUserRepository lawfirmUserRepository, EntityToLawfirmUserDTOConverter entityToLawfirmUserDTOConverter, AuthApi authApi, TMessageUserRepository tMessageUserRepository, CacheService cacheService) {
        this.tUsersRepository = tUsersRepository;
        this.virtualRepository = virtualRepository;
        this.entityToUserConverter = entityToUserConverter;
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.entityToLawfirmUserDTOConverter = entityToLawfirmUserDTOConverter;
        this.authApi = authApi;
        this.tMessageUserRepository = tMessageUserRepository;
        this.cacheService = cacheService;
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

            cacheService.evictCaches(CacheUtils.CACHE_USER_PROFILE);

        });
    }

    @Override
    public void changePwd(String userId, String newPwd) {
        log.debug("changePwd new Password for user {}", userId);
        authApi.changePassword(userId, newPwd);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TUsers createUsers(String userEmail, String clientFrom, EnumLanguage language, boolean emailVerified) {
        TUsers user = new TUsers();
        user.setEmail(userEmail);
        String fullname = StringUtils.substringBefore(userEmail, "@");
        user.setFullname(fullname);
        user.setAvatar(new byte[0]);
        user.setInitiales("");
        // temporary, with 2 + 10 char must be unique
        user.setIdUser("ul" + String.valueOf(UUID.randomUUID()).substring(0, 10));
        user.setHashkey(Utils.generateHashkey());
        // sometimes verified or not
        if (emailVerified) {
            user.setHashkey("");
            user.setIdValid(EnumValid.VERIFIED);
        } else {
            user.setIdValid(EnumValid.UNVERIFIED);
        }
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

        ZonedDateTime now = ZonedDateTime.now();

        TMessageUser tMessageUser = new TMessageUser();
        tMessageUser.setUserId(user.getId());
        tMessageUser.setTMessage(null);
        tMessageUser.setValid(false);
        tMessageUser.setDateTo(now);
        tMessageUser.setCreUser(user.getIdUser());

        tMessageUserRepository.save(tMessageUser);

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
            tUsers.setHashkey("");
            tUsers.setIdValid(EnumValid.VERIFIED);
            tUsersRepository.save(tUsers);
            cacheService.evictCaches(CacheUtils.CACHE_USER_PROFILE);
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

    @Override
    public LawyerDTO getLawfirmUserById(Long id) {
        log.info("Entering getLawfirmUserById id {}", id);

        TUsers user = findById(id);
        log.info("Leaving getLawfirmUserById");
        return entityToUserConverter.apply(user, false);
    }

    @Override
    public long findTotalUser() {
        log.info("Entering findTotalUser");
        return tUsersRepository.count();
    }

    @Override
    public Map<String, Long> findTotalUserBy() {
        log.info("Entering findTotalUser");
        List<TUsers> allUsers = tUsersRepository.findAll();

        return allUsers.stream().collect(Collectors.groupingBy(TUsers::getClientFrom, Collectors.counting()));
    }

    @Override
    public Long getNewTotalUserWeek() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Entering getNewTotalUserWeek {}", now);

        return tUsersRepository.countAllByWeek(now.minusWeeks(1), now);
    }

}
