package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.ProfileDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.EmailsEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.model.enumeration.EnumSlackUrl;
import com.ulegalize.lawfirm.repository.EmailsEntityRepository;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.rest.v2.SlackApi;
import com.ulegalize.lawfirm.service.EmailService;
import com.ulegalize.lawfirm.service.MailService;
import com.ulegalize.lawfirm.utils.EmailUtils;
import com.ulegalize.mail.transparency.EnumMailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    private static final int EMAIL_AFTER_VALID = 300;
    private final MailService mailService;
    private final EmailsEntityRepository emailsEntityRepository;
    private final LawfirmRepository lawfirmRepository;

    private final SlackApi slackApi;
    @Value("${app.email.supportUrl}")
    private String supportUrl;


    public EmailServiceImpl(MailService mailService, EmailsEntityRepository emailsEntityRepository, LawfirmRepository lawfirmRepository, SlackApi slackApi) {
        this.mailService = mailService;
        this.emailsEntityRepository = emailsEntityRepository;
        this.lawfirmRepository = lawfirmRepository;
        this.slackApi = slackApi;
    }


    @Override
    @Transactional(rollbackFor = ResponseStatusException.class)
    public EmailsEntity registeredUser(String vcKey, ProfileDTO userProfile) {
        log.debug("Entering registeredUser vcKey {} and email {}", vcKey, userProfile.getEmail());

        Optional<LawfirmEntity> optionalLawfirm = lawfirmRepository.findLawfirmByVckey(vcKey);

        if (optionalLawfirm.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm not found");
        }
        EmailsEntity emailsEntity = new EmailsEntity();
        emailsEntity.setLawfirm(optionalLawfirm.get());
        emailsEntity.setReminderDate(ZonedDateTime.now().plusDays(7));
        emailsEntity.setCreUser(userProfile.getUserLoginId());

        EmailsEntity updatedEmail = emailsEntityRepository.save(emailsEntity);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // welcome email
        mailService.sendMailWithoutMeetingAndIcs(EnumMailTemplate.MAILAUTOMATICREGISTER,
                EmailUtils.prepareContextWelcomeEmail(
                        userProfile.getEmail(),
                        lawfirmToken.getClientFrom(), supportUrl),
                userProfile.getLanguage());

        updatedEmail.setWelcomeSent(true);
        updatedEmail.setUpdUser(userProfile.getUserLoginId());

        updatedEmail = emailsEntityRepository.save(updatedEmail);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        log.debug("Welcome email sent {}", userProfile.getEmail());

        Runnable runner = () -> {
            try {
                mailService.sendMailWithoutMeetingAndIcs(EnumMailTemplate.MAILAUTOMATICSUPPORT,
                        EmailUtils.prepareContextSupportEmail(userProfile.getEmail(), lawfirmToken.getClientFrom(), supportUrl),
                        userProfile.getLanguage()
                );
            } catch (Exception e) {
                // handle exception
                throw new RuntimeException(e);
            }
        };

        log.debug("starting email support BEFORE 5 min {}", userProfile.getEmail());
        // after 300 seconds => 5 min
        executorService.schedule(runner, EMAIL_AFTER_VALID, TimeUnit.SECONDS);

        updatedEmail.setSupportSent(true);
        updatedEmail.setUpdUser(userProfile.getUserLoginId());

        updatedEmail = emailsEntityRepository.save(updatedEmail);

        log.debug("email support after 5 min sent {}", userProfile.getEmail());
        log.debug("Entering registeredUser {}", userProfile.getEmail());

        return updatedEmail;
    }

    @Override
    @Scheduled(cron = "#{getSchedulerReminderEmail}", zone = "Europe/Brussels")
    @Transactional(rollbackFor = ResponseStatusException.class)
    public void reminderSignup() throws RuntimeException {
        log.debug("Start Scheduler");
        List<EmailsEntity> emailsEntityList = emailsEntityRepository.findEmailsEntitiesByReminderDate(ZonedDateTime.now());

        if (!CollectionUtils.isEmpty(emailsEntityList)) {
            emailsEntityList.forEach(emailsEntity -> {
                try {
                    log.debug("Email to be sent {}", emailsEntity.getVcKey());
                    // get the first user into lawfirm
                    if (emailsEntity.getLawfirm() != null && !CollectionUtils.isEmpty(emailsEntity.getLawfirm().getLawfirmUsers())) {
                        TUsers user = emailsEntity.getLawfirm().getLawfirmUsers().get(0).getUser();
                        log.debug("Scheduler email sent to {} and client from {}", emailsEntity.getLawfirm().getEmail(), user.getClientFrom());
                        mailService.sendMailWithoutMeetingAndIcs(EnumMailTemplate.MAILAUTOMATICREMINDER,
                                EmailUtils.prepareContextReminderEmail(emailsEntity.getLawfirm().getEmail(), user.getClientFrom()),
                                user.getLanguage()
                        );
                    }
                    emailsEntity.setReminderSent(true);
                    emailsEntity.setUpdUser("scheduler");

                    emailsEntityRepository.save(emailsEntity);
                } catch (RuntimeException e) {
                    log.error("Error while trying to remind by email ", e);
                    slackApi.sendSensitiveNotification("Support Reminder Email  failed to '" + emailsEntity.getLawfirm().getEmail() + "client from "
                            + emailsEntity.getLawfirm().getLawfirmUsers().get(0).getUser().getClientFrom(), emailsEntity.getLawfirm().getEmail(), EnumSlackUrl.REMINDER_EMAIL_SUPPORT);

                    throw e;

                }
            });
        }
        log.debug("End Scheduler");
    }
}
