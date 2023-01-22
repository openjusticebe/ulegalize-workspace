package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.DTOToMessageEntityConverter;
import com.ulegalize.lawfirm.model.dto.MessageDTO;
import com.ulegalize.lawfirm.model.entity.TMessage;
import com.ulegalize.lawfirm.model.entity.TMessageUser;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.repository.TMessageRepository;
import com.ulegalize.lawfirm.repository.TMessageUserRepository;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import com.ulegalize.lawfirm.service.MessageService;
import com.ulegalize.lawfirm.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final TMessageUserRepository tMessageUserRepository;

    private final DTOToMessageEntityConverter dtoToMessageEntityConverter;

    private final TMessageRepository tMessageRepository;

    private final TUsersRepository tUsersRepository;

    public MessageServiceImpl(TMessageUserRepository tMessageUserRepository, DTOToMessageEntityConverter dtoToMessageEntityConverter, TMessageRepository tMessageRepository, TUsersRepository tUsersRepository) {
        this.tMessageUserRepository = tMessageUserRepository;
        this.dtoToMessageEntityConverter = dtoToMessageEntityConverter;
        this.tMessageRepository = tMessageRepository;
        this.tUsersRepository = tUsersRepository;
    }

    @Override
    public MessageDTO getMessage() {

        log.info("Entering getMessage");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ZonedDateTime now = ZonedDateTime.now();

        Optional<TMessageUser> tMessageUserOptional = tMessageUserRepository.findByUserId(lawfirmToken.getUserId(), now);

        return tMessageUserOptional.map(tMessageUser -> new MessageDTO(tMessageUser.getUserId(), tMessageUser.getValid(),
                        Utils.getLabel(EnumLanguage.fromshortCode(lawfirmToken.getLanguage()), StringEscapeUtils.unescapeHtml4(tMessageUser.getTMessage().getMessageFr()), StringEscapeUtils.unescapeHtml4(tMessageUser.getTMessage().getMessageEn()), StringEscapeUtils.unescapeHtml4(tMessageUser.getTMessage().getMessageNl()), StringEscapeUtils.unescapeHtml4(tMessageUser.getTMessage().getMessageDe()))))
                .orElse(null);
    }

    @Override
    @Transactional
    public void deactivateMessage() {

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ZonedDateTime now = ZonedDateTime.now();

        Optional<TMessageUser> tMessageUserOptional = tMessageUserRepository.findByUserId(lawfirmToken.getUserId(), now);

        if (tMessageUserOptional.isPresent()) {
            tMessageUserOptional.get().setValid(false);
            tMessageUserRepository.save(tMessageUserOptional.get());
        } else {
            log.warn("LawfirmUser already deactivated {}", lawfirmToken.getUserId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "LawfirmUser already deactivated");
        }
    }

    @Override
    public Long createMessage(MessageDTO messageDTO) {

        log.info("Entering createMessage with messageDTO {}", messageDTO);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<TUsers> tUsersOptional = tUsersRepository.findById(lawfirmToken.getUserId());

        if (tUsersOptional.isEmpty()) {
            log.error("Unknown user {} ", lawfirmToken.getUserId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown user " + lawfirmToken.getUserId());
        }

        TMessage tMessage = dtoToMessageEntityConverter.apply(messageDTO, new TMessage());

        tMessageRepository.save(tMessage);

        List<TUsers> TUsersList = tUsersRepository.findAll();

        List<TMessageUser> tMessageUserList = new ArrayList<>();

        for (TUsers tUsers : TUsersList) {

            TMessageUser tMessageUser = new TMessageUser();

            tMessageUser.setUserId(tUsers.getId());
            tMessageUser.setValid(true);
            tMessageUser.setCreUser(lawfirmToken.getUsername());
            tMessageUser.setDateTo(messageDTO.getDateTo());
            tMessageUser.setTMessage(tMessage);

            tMessageUserList.add(tMessageUser);
        }

        tMessageUserRepository.saveAll(tMessageUserList);

        return tMessage.getId();
    }
}
