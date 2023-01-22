package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToAssociatedWorkspaceDTOConverter;
import com.ulegalize.lawfirm.model.dto.AssociatedWorkspaceDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TWorkspaceAssociated;
import com.ulegalize.lawfirm.model.enumeration.EnumStatusAssociation;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.repository.OffsetBasedPageRequest;
import com.ulegalize.lawfirm.repository.TWorkspaceAssociatedRepository;
import com.ulegalize.lawfirm.service.MailService;
import com.ulegalize.lawfirm.service.v2.WorkspaceAssociatedService;
import com.ulegalize.lawfirm.utils.EmailUtils;
import com.ulegalize.lawfirm.utils.Utils;
import com.ulegalize.mail.transparency.EnumMailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class WorkspaceAssociatedServiceImpl implements WorkspaceAssociatedService {

    private final TWorkspaceAssociatedRepository tWorkspaceAssociatedRepository;
    private final LawfirmRepository lawfirmRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final MailService mailService;
    private final EntityToAssociatedWorkspaceDTOConverter entityToAssociatedWorkspaceDTOConverter;

    public WorkspaceAssociatedServiceImpl(TWorkspaceAssociatedRepository tWorkspaceAssociatedRepository, LawfirmRepository lawfirmRepository, LawfirmUserRepository lawfirmUserRepository, MailService mailService, EntityToAssociatedWorkspaceDTOConverter entityToAssociatedWorkspaceDTOConverter) {
        this.tWorkspaceAssociatedRepository = tWorkspaceAssociatedRepository;
        this.lawfirmRepository = lawfirmRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.mailService = mailService;
        this.entityToAssociatedWorkspaceDTOConverter = entityToAssociatedWorkspaceDTOConverter;
    }

    @Override
    public Boolean validateAssociation(Long id, String vcKeyRecipient, String hashkey, Boolean status) {
        log.debug("Entering validateAssociation with id : {} and vcKeyRecipient {} and status : {}", id, vcKeyRecipient, status);

        if (id == null || vcKeyRecipient == null || vcKeyRecipient.isEmpty() || status == null || hashkey == null || hashkey.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id or vcKeyRecipient or status or hashkey is null");
        }

        if (lawfirmRepository.findLawfirmByVckey(vcKeyRecipient).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "vcKeyRecipient is not found");
        }
        Optional<TWorkspaceAssociated> tWorkspaceAssociatedOptional = tWorkspaceAssociatedRepository.findByIdAndLawfirmRecipientAndStatus(id, vcKeyRecipient, List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING));

        if (tWorkspaceAssociatedOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The association doesn't exit");
        }
        if (!tWorkspaceAssociatedOptional.get().getHashkey().equals(hashkey)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error Hashkey");
        }

        tWorkspaceAssociatedOptional.get().setStatus(status ? EnumStatusAssociation.ACCEPTED : EnumStatusAssociation.REFUSED);

        tWorkspaceAssociatedRepository.save(tWorkspaceAssociatedOptional.get());

        log.debug("Saving status workspaceAssociated with id {}", tWorkspaceAssociatedOptional.get().getId());
        log.debug("Leaving approveWorkspace");

        return tWorkspaceAssociatedOptional.get().getStatus() == EnumStatusAssociation.ACCEPTED;
    }

    @Override
    public Boolean createAssociation(AssociatedWorkspaceDTO associatedWorkspaceDTO) {
        log.info("Entering createAssociation associatedWorkspaceDTO {}", associatedWorkspaceDTO);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String language = lawfirmToken.getLanguage() != null ? lawfirmToken.getLanguage().toLowerCase() : EnumLanguage.FR.getShortCode();

        if (associatedWorkspaceDTO.getVcKeyRecipient().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipient vcKey not found");
        }

        Optional<LawfirmEntity> lawfirmEntityOptionalSender = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntityOptionalSender.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm sender doesn't exist");
        }

        Optional<LawfirmEntity> lawfirmEntityOptionalRecipient = lawfirmRepository.findLawfirmByVckey(associatedWorkspaceDTO.getVcKeyRecipient());

        if (lawfirmEntityOptionalRecipient.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm recipient doesn't exist");
        }

        // Check if workspace accepted or pending
        Optional<TWorkspaceAssociated> tWorkspaceAssociatedOptional = tWorkspaceAssociatedRepository.findByLawfirmSenderAndLawfirmRecipientAndStatus(lawfirmToken.getVcKey(), associatedWorkspaceDTO.getVcKeyRecipient(), List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING));

        if (tWorkspaceAssociatedOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace association is either pending or accepted");
        }

        // Creation of an associated workspace
        TWorkspaceAssociated tWorkspaceAssociated = new TWorkspaceAssociated();

        tWorkspaceAssociated.setLawfirmSender(lawfirmEntityOptionalSender.get());
        tWorkspaceAssociated.setLawfirmRecipient(lawfirmEntityOptionalRecipient.get());
        tWorkspaceAssociated.setMessage(associatedWorkspaceDTO.getMessage());
        tWorkspaceAssociated.setStatus(EnumStatusAssociation.PENDING);
        tWorkspaceAssociated.setCreationDate(ZonedDateTime.now());
        tWorkspaceAssociated.setHashkey(Utils.generateHashkey());

        tWorkspaceAssociatedRepository.save(tWorkspaceAssociated);

        // Sending mail to recipient workspace
        if (lawfirmEntityOptionalRecipient.get().getEmail() != null) {
            log.debug("Lawfirm admin email {}", lawfirmEntityOptionalRecipient.get().getEmail());

            mailService.sendMailWithoutMeetingAndIcs(EnumMailTemplate.MAILWORKSPACEASSOCIATION,
                    EmailUtils.prepareContextCreateAssociation(
                            lawfirmEntityOptionalRecipient.get().getEmail(),
                            tWorkspaceAssociated,
                            "?id=" + tWorkspaceAssociated.getId() + "&vckey=" + lawfirmEntityOptionalRecipient.get().getVckey() + "&hashkey=" + tWorkspaceAssociated.getHashkey() + "&status=true",
                            "?id=" + tWorkspaceAssociated.getId() + "&vckey=" + lawfirmEntityOptionalRecipient.get().getVckey() + "&hashkey=" + tWorkspaceAssociated.getHashkey() + "&status=false",
                            lawfirmToken.getClientFrom()
                    ),
                    language
            );
        }

        return true;
    }

    @Override
    public Page<AssociatedWorkspaceDTO> getAllAssociatedWorkspace(int limit, int offset, String vcKey, Long userId, Boolean searchCriteriaType) {
        log.debug("Entering getAllAssociatedWorkspace() type with vcKey {} and user {}", vcKey, userId);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Lawfirm id {} user id {}", lawfirmUsers.get().getId(), userId);

            Pageable pageable = new OffsetBasedPageRequest(limit, offset);

            Page<TWorkspaceAssociated> workspaceAssociatedPage = tWorkspaceAssociatedRepository.findAllByLawfirmSenderOrLawfirmRecipientAndStatus(vcKey, searchCriteriaType, EnumStatusAssociation.REFUSED.name(), pageable);

            List<AssociatedWorkspaceDTO> workspaceDTOList = entityToAssociatedWorkspaceDTOConverter.convertToList(workspaceAssociatedPage.getContent(), vcKey);

            return new PageImpl<>(workspaceDTOList, Pageable.unpaged(), workspaceAssociatedPage.getTotalElements());
        }

        return Page.empty();
    }

    @Override
    public Boolean updateAssociation(String vcKeySender, Long userId, String vcKeyRecipient, Boolean typeAssociation, Boolean status) {
        log.debug("Entering updateAssociation with vcKeySender {} with vcKeyRecipient {} and status : {}", vcKeySender, vcKeyRecipient, status);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKeySender, userId);

        if (lawfirmUsers.isPresent()) {
            if (vcKeyRecipient == null || vcKeyRecipient.isEmpty() || typeAssociation == null || status == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "vcKeyRecipient or typeAssociation or status is null");
            }

            if (lawfirmRepository.findLawfirmByVckey(vcKeyRecipient).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "vcKeyRecipient is not found");
            }

            Optional<TWorkspaceAssociated> tWorkspaceAssociatedOptional = typeAssociation ? tWorkspaceAssociatedRepository.findByLawfirmSenderAndLawfirmRecipientAndStatus(vcKeyRecipient, vcKeySender, List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING)) : tWorkspaceAssociatedRepository.findByLawfirmSenderAndLawfirmRecipientAndStatus(vcKeySender, vcKeyRecipient, List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING));

            if (tWorkspaceAssociatedOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The association doesn't exit");
            }

            tWorkspaceAssociatedOptional.get().setStatus(status ? EnumStatusAssociation.ACCEPTED : EnumStatusAssociation.REFUSED);

            tWorkspaceAssociatedRepository.save(tWorkspaceAssociatedOptional.get());

            log.debug("Saving status workspaceAssociated with id {}", tWorkspaceAssociatedOptional.get().getId());
            log.debug("Leaving updateAssociation");

            return tWorkspaceAssociatedOptional.get().getStatus() == EnumStatusAssociation.ACCEPTED;
        }
        return null;
    }
}
