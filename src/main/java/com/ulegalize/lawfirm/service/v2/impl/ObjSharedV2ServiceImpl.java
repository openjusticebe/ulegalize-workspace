package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.Item;
import com.ulegalize.dto.ShareFileDTO;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.TObjShared;
import com.ulegalize.lawfirm.model.entity.TObjSharedWith;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.repository.TObjSharedRepository;
import com.ulegalize.lawfirm.repository.TObjSharedWithRepository;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import com.ulegalize.lawfirm.rest.DriveFactory;
import com.ulegalize.lawfirm.rest.v2.DriveApi;
import com.ulegalize.lawfirm.service.MailService;
import com.ulegalize.lawfirm.service.v2.ObjSharedV2Service;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import com.ulegalize.lawfirm.utils.EmailUtils;
import com.ulegalize.mail.transparency.EnumMailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class ObjSharedV2ServiceImpl implements ObjSharedV2Service {
    private final TObjSharedRepository tObjSharedRepository;
    private final TObjSharedWithRepository tObjSharedWithRepository;
    private final UserV2Service userV2Service;
    private final TUsersRepository tUsersRepository;
    private final MailService mailService;
    private final DriveFactory driveFactory;

    public ObjSharedV2ServiceImpl(TObjSharedRepository tObjSharedRepository, TObjSharedWithRepository tObjSharedWithRepository, UserV2Service userV2Service, TUsersRepository tUsersRepository,
                                  MailService mailService, DriveFactory driveFactory) {
        this.tObjSharedRepository = tObjSharedRepository;
        this.tObjSharedWithRepository = tObjSharedWithRepository;
        this.userV2Service = userV2Service;
        this.tUsersRepository = tUsersRepository;
        this.mailService = mailService;
        this.driveFactory = driveFactory;
    }

    @Override
    public void shareObject(ShareFileDTO shareFileDTO) {
        log.debug("Entering shareObject  {}", shareFileDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long size = shareFileDTO.getSize() != null ? shareFileDTO.getSize() : 0L;
        List<String> emails = shareFileDTO.getUsersItem().stream().map(Item::getValue).collect(Collectors.toList());
        if (shareFileDTO.getShared_with() != null && !shareFileDTO.getShared_with().isEmpty()) {
            emails.addAll(shareFileDTO.getShared_with());
        }
        shareFolder(shareFileDTO.getObj(), lawfirmToken.getVcKey(), lawfirmToken.getUsername(), lawfirmToken.getUserId(), size, emails, shareFileDTO.getMsg(), shareFileDTO.getRight(), lawfirmToken.getClientFrom());
        List<String> sendEmails = shareFileDTO.getUsersItem().stream().map(Item::getValue).collect(Collectors.toList());

        sendEmails.forEach(email -> {
            log.debug("Email {} to be sent", email);
            Optional<TUsers> usersOptional = tUsersRepository.findByEmail(email);
            usersOptional.ifPresent(tUsers -> {
                String language = tUsers.getLanguage() != null ? tUsers.getLanguage().toLowerCase() : EnumLanguage.FR.getShortCode();

                        mailService.sendMailWithoutMeetingAndIcs(EnumMailTemplate.MAILSHAREDUSERSECURITYTEMPLATE,
                                EmailUtils.prepareContextForSharedFolderUser(email, shareFileDTO.getObj(), tUsers.getFullname(), lawfirmToken.getVcKey(), lawfirmToken.getClientFrom()), language
                        );
                    }
            );
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void shareFolder(String obj, String vckey, String username, Long userIdFrom, long size, List<String> emails, String message, int right, String clientFrom) {
        log.debug("Entering shareFolder String obj {},  String vckey {},  String username {}, Long userIdFrom {},  long size {},  List<String> emails {}", obj, vckey, username, userIdFrom, size, emails);

        Optional<TObjShared> optionalTObjShared = tObjSharedRepository.findByVcKeyAndObj(vckey, obj);
        TObjShared tObjShared = null;

        if (!optionalTObjShared.isPresent()) {
            tObjShared = new TObjShared();
            tObjShared.setObj(obj);
            tObjShared.setVcKey(vckey);
            tObjShared.setUserUpd(username);
            tObjShared.setDateUpd(LocalDateTime.now());
            tObjShared.setSize(size);
            tObjShared = tObjSharedRepository.save(tObjShared);
        } else {
            tObjShared = optionalTObjShared.get();
        }

        TObjShared finalTObjShared = tObjShared;
        emails.forEach(email -> {
            log.debug("Email {} to be sent", email);
            Optional<TUsers> usersOptional = tUsersRepository.findByEmail(email);
            TUsers users = usersOptional.orElseGet(() -> userV2Service.createUsers(email, clientFrom, EnumLanguage.FR, true));

            tObjSharedWithRepository.deleteByObjAndUserTo(finalTObjShared.getId(), users.getId());
            TObjSharedWith tObjSharedWith = new TObjSharedWith();
            tObjSharedWith.setObjId(finalTObjShared.getId());
            tObjSharedWith.setFromUserid(userIdFrom);
            tObjSharedWith.setToUserid(users.getId());
            tObjSharedWith.setUserRight(right);
            tObjSharedWith.setMsg(message);
            tObjSharedWith.setUserUpd(username);
            tObjSharedWith.setRemoteAddr("");
            tObjSharedWith.setDateUpd(LocalDateTime.now());

            tObjSharedWithRepository.save(tObjSharedWith);
        });
        log.debug("Leaving shareFolder ");

    }

    @Override
    public ShareFileDTO getShareObject(String path) {
        log.debug("Entering getShareObject String path {}", path);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<TObjShared> optionalTObjShared = tObjSharedRepository.findByVcKeyAndObj(lawfirmToken.getVcKey(), path);
        ShareFileDTO shareFileDTO = new ShareFileDTO();
        shareFileDTO.setRight(0);
        if (optionalTObjShared.isPresent()) {
            shareFileDTO.setId(optionalTObjShared.get().getId());
            shareFileDTO.setObj(optionalTObjShared.get().getObj());
            shareFileDTO.setSize(optionalTObjShared.get().getSize());
            shareFileDTO.setShared_with(optionalTObjShared.get().getObjSharedWithList().stream()
                    .map(tObjSharedWith -> tObjSharedWith.getToUsers().getEmail())
                    .collect(Collectors.toList()));

        }
        return shareFileDTO;
    }

    @Override
    public String getShareLink(String path) {
        log.debug("Entering getShareLink String path {}", path);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());
        return driveApi.getShareLink(lawfirmToken, path);
    }
}
