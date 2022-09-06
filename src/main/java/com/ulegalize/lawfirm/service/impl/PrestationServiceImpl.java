package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.ItemBigDecimalDto;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.dto.PrestationSummary;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.DTOToTimesheetEntityConverter;
import com.ulegalize.lawfirm.model.converter.EntityToPrestationConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TTimesheet;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.repository.OffsetBasedPageRequest;
import com.ulegalize.lawfirm.repository.TTimesheetRepository;
import com.ulegalize.lawfirm.service.ComptaService;
import com.ulegalize.lawfirm.service.PrestationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class PrestationServiceImpl implements PrestationService {
    private final TTimesheetRepository timesheetRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final EntityToPrestationConverter entityToPrestationConverter;
    private final DTOToTimesheetEntityConverter dtoToTimesheetEntityConverter;
    private final ComptaService comptaService;

    public PrestationServiceImpl(TTimesheetRepository timesheetRepository,
                                 LawfirmUserRepository lawfirmUserRepository,
                                 EntityToPrestationConverter entityToPrestationConverter, DTOToTimesheetEntityConverter dtoToTimesheetEntityConverter, ComptaService comptaService) {
        this.timesheetRepository = timesheetRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.entityToPrestationConverter = entityToPrestationConverter;
        this.dtoToTimesheetEntityConverter = dtoToTimesheetEntityConverter;
        this.comptaService = comptaService;
    }

    public Page<PrestationSummary> getAllPrestations(int limit, int offset, Long userId, String vcKey, String searchCriteriaYear, Long searchCriteriaNumber, Integer searchCriteriaIdTsType) {
        log.debug("Get all presations with user {} limit {} and offset {}", userId, limit, offset);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            Long numberDossier = searchCriteriaNumber != null && searchCriteriaNumber == 0 ? null : searchCriteriaNumber;
            Integer idTsType = searchCriteriaIdTsType != null && searchCriteriaIdTsType == 0 ? null : searchCriteriaIdTsType;

            Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Direction.DESC, "idTs"));
            Page<TTimesheet> allPrestations = timesheetRepository.findAllWithPagination(lawfirmUsers.get().getId(), searchCriteriaYear, numberDossier, idTsType, pageable);

            List<PrestationSummary> prestationSummaryList = entityToPrestationConverter.convertToList(allPrestations.getContent());
            return new PageImpl<>(prestationSummaryList, Pageable.unpaged(), allPrestations.getTotalElements());

        }

        return Page.empty();
    }

    @Override
    public Page<PrestationSummary> getAllPrestationsByDossierId(int limit, int offset, Long dossierId, Long userId, String vcKey) {
        log.debug("Get all presations with user {} limit {} , offset {} and dossierId {}", userId, limit, offset, dossierId);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Direction.DESC, "idTs"));
            Page<TTimesheet> allPrestations = timesheetRepository.findAllByDossierIdWithPagination(dossierId, lawfirmUsers.get().getId(), pageable);

            List<PrestationSummary> prestationSummaryList = entityToPrestationConverter.convertToList(allPrestations.getContent());
            return new PageImpl<>(prestationSummaryList, Pageable.unpaged(), allPrestations.getTotalElements());

        }

        return Page.empty();
    }

    @Override
    public List<PrestationSummary> getPrestationsByDossierId(Long dossierId, Long userId, String vcKey) {
        log.debug("Get all presations with user {} and dossierId {}", userId, dossierId);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            List<TTimesheet> allPrestations = timesheetRepository.findAllByDossierId(dossierId, lawfirmUsers.get().getId());

            return entityToPrestationConverter.convertToList(allPrestations);

        }

        return new ArrayList<>();
    }

    @Override
    public Long countAllPrestationByVcKey(Long dossierId, Long userId, String vcKey) {
        log.debug("countAllInvoicesByVcKey vckey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);
            return timesheetRepository.countAllByIdDossAndVcKey(dossierId, lawfirmUsers.get().getId());

        }

        return 0L;
    }

    @Override
    public PrestationSummary getDefaultPrestations(Long userId, String vcKey) {
        log.debug("getDefaultPrestations with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm is present {}", vcKey);

            PrestationSummary prestationSummary = new PrestationSummary();

            prestationSummary.setIdGest(userId);
            prestationSummary.setDateAction(ZonedDateTime.now());
            prestationSummary.setCouthoraire(lawfirmUsers.get().getLawfirm().getCouthoraire());
            prestationSummary.setIdGestItem(new ItemLongDto(userId, lawfirmUsers.get().getUser().getEmail()));
            prestationSummary.setForfait(false);

            ItemBigDecimalDto tvaDefaultCompta = comptaService.getTvaDefaultCompta(userId, vcKey);
            if (tvaDefaultCompta != null) {
                prestationSummary.setVat(tvaDefaultCompta.getValue());
                prestationSummary.setVatItem(tvaDefaultCompta);
            }
            return prestationSummary;
        }

        return null;
    }

    @Override
    public Long savePrestation(PrestationSummary prestationSummary) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String vcKey = lawfirmToken.getVcKey();
        log.debug("savePrestation -> Vckey {}", vcKey);
        Long userId = lawfirmToken.getUserId();
        log.debug("savePrestation -> userId {}", userId);
        String username = lawfirmToken.getUsername();
        log.debug("savePrestation -> username {}", username);
        if (prestationSummary == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestation not found");
        }
        if (prestationSummary.getDateAction() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestation action date not found");
        }
        if (prestationSummary.getDossierId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestation dossier not found");
        }
        if (prestationSummary.isForfait()) {
            if (prestationSummary.getForfaitHt() == null || prestationSummary.getForfaitHt().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestation forfait not found");
            }
        } else {
            if ((prestationSummary.getDh() == null || prestationSummary.getDh().compareTo(BigDecimal.ZERO) <= 0)
                    && (prestationSummary.getDm() == null || prestationSummary.getDm().compareTo(BigDecimal.ZERO) <= 0)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestation forfait not found");
            }
        }

        TTimesheet tTimesheet = new TTimesheet();
        if (prestationSummary.getId() != null) {
            Optional<TTimesheet> tTimesheetOptional = timesheetRepository.findById(prestationSummary.getId());

            if (tTimesheetOptional.isPresent()) {
                tTimesheet = tTimesheetOptional.get();
            }
        }
        TTimesheet tTimesheetToCreate = dtoToTimesheetEntityConverter.apply(prestationSummary, tTimesheet);
        tTimesheetToCreate.setUserUpd(username);

        TTimesheet saved = timesheetRepository.save(tTimesheetToCreate);
        return saved.getIdTs();
    }

    @Override
    public PrestationSummary getPrestationById(Long userId, String vcKey, Long prestationId) {
        log.debug("Get presations with user {} vcKey {} , prestationId {}", userId, vcKey, prestationId);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            Optional<TTimesheet> optionalTTimesheet = timesheetRepository.findById(prestationId);

            if (optionalTTimesheet.isPresent()) {
                return (entityToPrestationConverter.apply(optionalTTimesheet.get()));
            }

        }

        return null;
    }

    @Override
    public Long deletePrestation(Long userId, String vcKey, Long prestationId) {
        log.debug("Get presations with user {} vcKey {} , prestationId {}", userId, vcKey, prestationId);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            Optional<TTimesheet> optionalTTimesheet = timesheetRepository.findById(prestationId);

            if (!optionalTTimesheet.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestation is not found");
            }

            timesheetRepository.delete(optionalTTimesheet.get());

            return prestationId;
        }

        return null;
    }
}
