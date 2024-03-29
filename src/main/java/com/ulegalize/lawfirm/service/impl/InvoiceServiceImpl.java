package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.*;
import com.ulegalize.dto.template.Item;
import com.ulegalize.dto.template.*;
import com.ulegalize.enumeration.EnumAccountType;
import com.ulegalize.enumeration.EnumFactureType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.kafka.producer.template.IInvoiceProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.DTOToInvoiceDetailsEntityConverter;
import com.ulegalize.lawfirm.model.converter.DTOToInvoiceEntityConverter;
import com.ulegalize.lawfirm.model.converter.EntityToInvoiceConverter;
import com.ulegalize.lawfirm.model.dto.GroupVatDTO;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.repository.*;
import com.ulegalize.lawfirm.rest.DriveFactory;
import com.ulegalize.lawfirm.rest.v2.DriveApi;
import com.ulegalize.lawfirm.rest.v2.ReportApi;
import com.ulegalize.lawfirm.service.InvoiceService;
import com.ulegalize.lawfirm.service.InvoiceUtils;
import com.ulegalize.lawfirm.service.SearchService;
import com.ulegalize.lawfirm.utils.DriveUtils;
import com.ulegalize.lawfirm.utils.InvoicesUtils;
import com.ulegalize.utils.ClientsUtils;
import com.ulegalize.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final TFacturesRepository facturesRepository;
    private final LawfirmRepository lawfirmRepository;
    private final RefPosteRepository refPosteRepository;
    private final RefCompteRepository refCompteRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final TTimesheetRepository timesheetRepository;
    private final TDebourRepository tDebourRepository;
    private final SearchService searchService;
    private final EntityToInvoiceConverter entityToInvoiceConverter;
    private final DTOToInvoiceEntityConverter dtoToInvoiceEntityConverter;
    private final DTOToInvoiceDetailsEntityConverter dtoToInvoiceDetailsEntityConverter;
    private final DossierRepository dossierRepository;
    private final ClientRepository clientRepository;
    private final TFraisRepository fraisRepository;
    private final TFactureEcheanceRepository tfactureEcheanceRepository;
    private final ReportApi reportApi;
    private final DriveFactory driveFactory;
    private final IInvoiceProducer invoiceProducer;
    private final TFactureTimesheetRepository tFactureTimesheetRepository;
    private final TFactureDetailsRepository tFactureDetailsRepository;
    private final InvoiceUtils invoiceUtils;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${biglelegal.activation}")
    private Boolean bigleLegalActivation;

    @Override
    public Page<InvoiceDTO> getAllInvoices(int limit, int offset, String vcKey, Integer searchEcheance,
                                           ZonedDateTime searchDate, String searchNomenclature,
                                           String searchClient, Boolean sortFacture) {
        log.debug("Get all Invoices with user {} limit {} and offset {}", vcKey, limit, offset);

        Pageable pageable;

        if (sortFacture == null) {
            pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Direction.DESC, "idFacture"));
        } else if (sortFacture) {
            pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Direction.ASC, "factureRef"));
        } else {
            pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Direction.DESC, "factureRef"));
        }

        Page<TFactures> allInvoices;
        if (searchDate != null) {
            allInvoices = facturesRepository.findAllByDateWithPagination(vcKey, searchEcheance, searchDate, searchNomenclature, pageable);
        } else {
            allInvoices = facturesRepository.findAllWithPagination(vcKey, searchEcheance, searchNomenclature, searchClient, pageable);
        }

        List<InvoiceDTO> invoiceDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allInvoices.getContent())) {
            invoiceDTOList = entityToInvoiceConverter.convertToList(allInvoices.getContent(), true);
        }

        return new PageImpl<>(invoiceDTOList, Pageable.unpaged(), allInvoices.getTotalElements());
    }

    @Override
    public Page<InvoiceDTO> getAllInvoicesByDossierId(int limit, int offset, Long dossierId, String vcKey) {
        log.debug("Get all Invoices with user {} limit {} and offset {} and dossierId {}", vcKey, limit, offset, dossierId);

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idFactureType");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "factureRef");
        Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(order, order2));
        Page<TFactures> allInvoices = facturesRepository.findByDossierIdWithPagination(dossierId, vcKey, pageable);
        List<InvoiceDTO> invoiceDTOList = !CollectionUtils.isEmpty(allInvoices.getContent()) ? entityToInvoiceConverter.convertToList(allInvoices.getContent(), true) : new ArrayList<>();

        return new PageImpl<>(invoiceDTOList, Pageable.unpaged(), allInvoices.getTotalElements());
    }

    @Override
    public List<ItemLongDto> getInvoicesBySearchCriteria(String vcKey, String searchCriteria, Long dossierId) {

        log.debug("get invoices by search criteria {} and user {} ", searchCriteria, vcKey);

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(vcKey);

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm does not exist");
        }

        var search = searchCriteria;

        if (searchCriteria == null || searchCriteria.isEmpty()) {
            search = "%";
        }

        return facturesRepository.findInvoicesBySearchCriteria(vcKey, search, dossierId);
    }

    @Override
    public InvoiceDTO getDefaultInvoice(Long userId, String vcKey, String language) {
        EnumLanguage enumLanguage = EnumLanguage.FR;
        if (language != null) {
            enumLanguage = EnumLanguage.fromshortCode(language);
        }
        log.debug("Entering getDefaultInvoice with user id {} and vckey {}", userId, vcKey);

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setVcKey(vcKey);
        invoiceDTO.setValid(false);

        // facture type must be an enum
        invoiceDTO.setTypeId(EnumFactureType.TEMP.getId());

        invoiceDTO.setTypeItem(new ItemLongDto(
                EnumFactureType.TEMP.getId(),
                Utils.getLabel(enumLanguage,
                        EnumFactureType.TEMP.name(), null)
        ));

        invoiceDTO.setMontant(BigDecimal.ZERO);

        List<ItemDto> refCompte = refCompteRepository.findDTOByIdAndAccountTypeId(vcKey, EnumAccountType.PRO_ACCOUNT);

        if (!CollectionUtils.isEmpty(refCompte)) {
            invoiceDTO.setBankAccountId(refCompte.get(0).getValue());
            invoiceDTO.setBankAccountItem(new ItemIntegerDto(refCompte.get(0).getValue(), refCompte.get(0).getLabel()));
        }

        Optional<RefPoste> refPoste = refPosteRepository.findFirstByVcKeyAndHonoraires(vcKey, true);

        refPoste.ifPresent(poste -> {
            invoiceDTO.setPosteId(poste.getIdPoste());
            invoiceDTO.setPosteItem(new ItemDto(poste.getIdPoste(), poste.getRefPoste()));
        });

        List<ItemDto> tFactureEcheance = searchService.getFactureEcheances(vcKey);
        if (!tFactureEcheance.isEmpty()) {
            invoiceDTO.setEcheanceId(tFactureEcheance.get(0).getValue());
            invoiceDTO.setEcheanceItem(tFactureEcheance.get(0));
        }
        invoiceDTO.setDateValue(ZonedDateTime.now());

        invoiceDTO.setDateEcheance(invoiceDTO.getDateValue().plusDays(7));

        List<ItemVatDTO> vats = searchService.getVats(vcKey);

        int maxIndexVat = vats != null && !vats.isEmpty() ? vats.size() - 1 : 0;
        invoiceDTO.setInvoiceDetailsDTOList(new ArrayList<>());

        invoiceDTO.getInvoiceDetailsDTOList().add(new InvoiceDetailsDTO(
                null, null, "",
                vats.get(maxIndexVat).getValue(),
                vats.get(maxIndexVat), BigDecimal.ZERO,
                BigDecimal.ZERO
        ));

        log.debug("Leaving getDefaultInvoice with user id {} and vckey {}", userId, vcKey);

        return invoiceDTO;
    }

    @Override
    public InvoiceDTO getInvoiceById(Long invoiceId, String vcKey) {
        log.debug("Entering getInvoiceById with invoiceId {} ", invoiceId);
        InvoiceDTO invoiceDTO = null;
        Optional<TFactures> facturesOptional = facturesRepository.findByIdFactureAndVcKey(invoiceId, vcKey);

        if (facturesOptional.isEmpty()) {
            log.warn("invoice is not existing invoiceId {}", invoiceId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice is not existing");
        } else {

            invoiceDTO = entityToInvoiceConverter.apply(facturesOptional.get(), false);

            List<ItemVatDTO> vats = searchService.getVats(facturesOptional.get().getVcKey());

            // facture details
            if (facturesOptional.get().getTFactureDetailsList() != null && !facturesOptional.get().getTFactureDetailsList().isEmpty()) {

                for (TFactureDetails factureDetails : facturesOptional.get().getTFactureDetailsList()) {
                    List<ItemVatDTO> itemBigDecimalDto = vats.stream().filter(vat -> vat.getValue().compareTo(factureDetails.getTva()) == 0).collect(toList());

                    InvoiceDetailsDTO invoiceDetailsDTO = new InvoiceDetailsDTO(
                            factureDetails.getId(),
                            factureDetails.getTFactures().getIdFacture(),
                            factureDetails.getDescription(),
                            factureDetails.getTva(),
                            itemBigDecimalDto.get(0),
                            factureDetails.getTtc(),
                            factureDetails.getHtva()
                    );
                    invoiceDTO.getInvoiceDetailsDTOList().add(invoiceDetailsDTO);
                }
            } else {

                int maxIndexVat = vats != null && !vats.isEmpty() ? vats.size() - 1 : 0;
                invoiceDTO.setInvoiceDetailsDTOList(new ArrayList<>());

                invoiceDTO.getInvoiceDetailsDTOList().add(new InvoiceDetailsDTO(
                        null, null, "",
                        vats.get(maxIndexVat).getValue(),
                        vats.get(maxIndexVat), BigDecimal.ZERO,
                        BigDecimal.ZERO
                ));
            }

            // prestation
            if (facturesOptional.get().getTFactureTimesheetList() != null && !facturesOptional.get().getTFactureTimesheetList().isEmpty()) {

                for (TFactureTimesheet tFactureTimesheet : facturesOptional.get().getTFactureTimesheetList()) {
                    invoiceDTO.getPrestationIdList().add(tFactureTimesheet.getTsId());
                }
            }
            // frais admin
            if (facturesOptional.get().getFraisAdminList() != null && !facturesOptional.get().getFraisAdminList().isEmpty()) {

                for (FactureFraisAdmin factureFraisAdmin : facturesOptional.get().getFraisAdminList()) {
                    invoiceDTO.getFraisAdminIdList().add(factureFraisAdmin.getDeboursId());
                }
            }

            // debours
            if (facturesOptional.get().getFraisDeboursList() != null && !facturesOptional.get().getFraisDeboursList().isEmpty()) {

                for (FactureFraisDebours factureFraisDebours : facturesOptional.get().getFraisDeboursList()) {
                    invoiceDTO.getDeboursIdList().add(factureFraisDebours.getFraisId());
                }
            }
            // frais coll
            if (facturesOptional.get().getFraisDeboursList() != null && !facturesOptional.get().getFraisDeboursList().isEmpty()) {

                for (FactureFraisCollaboration factureFraisCollaboration : facturesOptional.get().getFraisCollaborationArrayList()) {
                    invoiceDTO.getFraisCollaborationIdList().add(factureFraisCollaboration.getFraisId());
                }
            }
        }
        log.debug("Leaving getInvoiceById with invoiceId {} ", invoiceId);
        return invoiceDTO;
    }

    @Override
    public Long createInvoice(InvoiceDTO invoiceDTO, String vcKey) {
        log.debug("Entering createInvoice with {}", invoiceDTO.toString());
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(vcKey);

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lawfirm is unknown");
        }

        commonRules(invoiceDTO, vcKey, lawfirmToken.getUserId());

        EnumFactureType enumFactureType = EnumFactureType.fromId(invoiceDTO.getTypeId());
        if (enumFactureType == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Facture type is not found");
        }

        invoiceDTO.setVcKey(vcKey);
        Integer yearFacture = invoiceDTO.getDateValue().getYear();
        // if it's credit there is no temporary so get max number directly
//        if (enumFactureType.equals(EnumFactureType.TEMP_NC)) {
//            maxNumFactTempByVcKey = facturesRepository.getMaxNumFacture(vcKey, EnumFactureType.CREDIT, yearFacture);
//        } else {
//        }
        Integer maxNumFactTempByVcKey = facturesRepository.getMaxNumFactTempByVcKey(vcKey, enumFactureType);


        log.info("maxNumFactTempByVcKey {}", maxNumFactTempByVcKey);
        Optional<LawfirmDTO> lawfirmDTOOptional = lawfirmRepository.findLawfirmDTOByVckey(vcKey);

        if (lawfirmDTOOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lawfirm is not found");
        }

        if (maxNumFactTempByVcKey == null) {
            maxNumFactTempByVcKey = lawfirmDTOOptional.get().getStartInvoiceNumber();
        } else {
            maxNumFactTempByVcKey++;
        }


        if (enumFactureType == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Facture type is not found");
        }

        String factureRef = InvoicesUtils.getInvoiceReference(yearFacture, maxNumFactTempByVcKey, enumFactureType);
        log.info("facture ref {}", factureRef);
        invoiceDTO.setYearFacture(yearFacture);
        invoiceDTO.setNumFacture(maxNumFactTempByVcKey);
        invoiceDTO.setReference(factureRef);

        TFactures tFactures = dtoToInvoiceEntityConverter.apply(invoiceDTO, new TFactures());


        log.info("Create facture details");
        invoiceDTO.getInvoiceDetailsDTOList().forEach(invoiceDetailsDTO -> {
            TFactureDetails tFactureDetails = dtoToInvoiceDetailsEntityConverter.apply(invoiceDetailsDTO, new TFactureDetails());
            tFactureDetails.setUserUpd(lawfirmToken.getUsername());
            tFactureDetails.setDateUpd(LocalDateTime.now());
            tFactures.addTFactureDetails(tFactureDetails);
            log.info("invoiceDetailsDTO : {} was added to tFacture : {} ", invoiceDetailsDTO, tFactures);
        });

        // prestations
        invoiceDTO.getPrestationIdList().forEach(prestationId -> {
            TFactureTimesheet tFactureTimesheet = new TFactureTimesheet();
            tFactureTimesheet.setTsId(prestationId);
            tFactureTimesheet.setCreUser(lawfirmToken.getUsername());
            Optional<TTimesheet> tTimesheetOptional = timesheetRepository.findById(prestationId);
            if (tTimesheetOptional.isPresent()) {
                tTimesheetOptional.get().setIdTs(prestationId);
                tFactureTimesheet.setTTimesheet(tTimesheetOptional.get());
            }
            tFactureTimesheet.setUpdUser(lawfirmToken.getUsername());
            tFactures.addTFactureTimesheet(tFactureTimesheet);
            log.info("TFactureTimesheet : {} was added to tFacture : {} ", prestationId, tFactures);
        });

        // frais admin
        invoiceDTO.getFraisAdminIdList().forEach(fraisAdminId -> {
            FactureFraisAdmin factureFraisAdmin = new FactureFraisAdmin();
            factureFraisAdmin.setDeboursId(fraisAdminId);
            factureFraisAdmin.setCreUser(lawfirmToken.getUsername());
            Optional<TDebour> debourOptional = tDebourRepository.findById(fraisAdminId);
            if (debourOptional.isPresent()) {
                debourOptional.get().setIdDebour(fraisAdminId);
                factureFraisAdmin.setTDebour(debourOptional.get());
            }
            factureFraisAdmin.setUpdUser(lawfirmToken.getUsername());
            tFactures.addFactureFraisAdmin(factureFraisAdmin);
            log.info("FactureFraisAdmin : {} was added to tFacture : {} ", fraisAdminId, tFactures);
        });

        // debours
        invoiceDTO.getDeboursIdList().forEach(fraisId -> {
            FactureFraisDebours factureFraisDebours = new FactureFraisDebours();
            factureFraisDebours.setFraisId(fraisId);
            factureFraisDebours.setCreUser(lawfirmToken.getUsername());
            factureFraisDebours.setUpdUser(lawfirmToken.getUsername());
            tFactures.addFactureFraisDebours(factureFraisDebours);
            log.info("FactureFrais debours : {} was added to tFacture : {} ", fraisId, tFactures);
        });

        // frais collaboration
        invoiceDTO.getFraisCollaborationIdList().forEach(fraisId -> {
            FactureFraisCollaboration factureFraisCollaboration = new FactureFraisCollaboration();
            factureFraisCollaboration.setFraisId(fraisId);
            factureFraisCollaboration.setCreUser(lawfirmToken.getUsername());
            factureFraisCollaboration.setUpdUser(lawfirmToken.getUsername());
            tFactures.addFactureFraisColl(factureFraisCollaboration);
            log.info("FactureFrais collabration : {} was added to tFacture : {} ", fraisId, tFactures);
        });

        tFactures.setDateUpd(LocalDateTime.now());
        tFactures.setUserUpd(lawfirmToken.getUsername());

        facturesRepository.save(tFactures);
        invoiceDTO.setId(tFactures.getIdFacture());
        String communication = invoiceUtils.communication(invoiceDTO.getId(), invoiceDTO.getYearFacture());
        tFactures.setCommunicationStruct(communication);
        invoiceDTO.setCommunication(communication);
        log.debug("invoice communication struct {}: ", communication);

        facturesRepository.save(tFactures);

        log.info("invoice saved in repo with id {}: ", tFactures.getIdFacture());
        log.debug("Leaving createInvoice with {}", invoiceDTO);

        // Create invoice with Template API
        if (bigleLegalActivation) {
            invoiceProducer.createDocument(lawfirmToken, createInvoiceTemplateDTO(invoiceDTO, lawfirmEntityOptional.get()));
        }

        return tFactures.getIdFacture();
    }

    @Override
    public InvoiceDTO updateInvoice(InvoiceDTO invoiceDTO, String vcKey) {
        log.debug("Entering updateInvoice with invoice id : {}", invoiceDTO.getId());
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(vcKey);

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lawfirm is unknown");
        }

        commonRules(invoiceDTO, vcKey, lawfirmToken.getUserId());


        if (invoiceDTO.getId() == null) {
            log.warn("invoice is not filled in {}", invoiceDTO.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice is not filled in");
        }

        Optional<TFactures> tFacturesOptional = facturesRepository.findByIdFactureAndVcKey(invoiceDTO.getId(), vcKey);

        if (tFacturesOptional.isEmpty()) {
            log.warn("invoice does not exist {}", invoiceDTO.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice does not exist");
        }

        if (tFacturesOptional.get().getValid()) {
            log.warn("invoice cannot be modified {}", invoiceDTO.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice cannot be modified");
        }

        if (invoiceDTO.getInvoiceDetailsDTOList() != null) {

            for (InvoiceDetailsDTO invoiceDetailsDTO : invoiceDTO.getInvoiceDetailsDTOList()) {
                if (invoiceDetailsDTO.getMontantHt() == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Montant ht cannot be empty !");
                }
            }

            TFactureDetails factureDetails;
            boolean factureDetailsExist;

            List<TFactureDetails> difference = new ArrayList<>();
            boolean exist = false;
            for (TFactureDetails tFactureDetails : tFacturesOptional.get().getTFactureDetailsList()) {
                for (InvoiceDetailsDTO invoiceDetailsDTO : invoiceDTO.getInvoiceDetailsDTOList()) {
                    if (tFactureDetails.getId().equals(invoiceDetailsDTO.getId())) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    difference.add(tFactureDetails);
                }
                exist = false;
            }
            tFacturesOptional.get().getTFactureDetailsList().removeAll(difference);

            for (InvoiceDetailsDTO invoiceDetailsDTO : invoiceDTO.getInvoiceDetailsDTOList()) {
                factureDetailsExist = false;
                for (TFactureDetails factureDetailsExisting : tFacturesOptional.get().getTFactureDetailsList()) {
                    if (invoiceDetailsDTO.getId() != null && factureDetailsExisting.getId().equals(invoiceDetailsDTO.getId())) {
                        factureDetailsExist = true;
                        factureDetailsExisting.setTFactures(tFacturesOptional.get());
                        factureDetailsExisting.setDescription(invoiceDetailsDTO.getDescription());
                        factureDetailsExisting.setHtva(invoiceDetailsDTO.getMontantHt());
                        factureDetailsExisting.setTtc(invoiceDetailsDTO.getMontant());
                        factureDetailsExisting.setTva(invoiceDetailsDTO.getTva());
                        factureDetailsExisting.setDateUpd(LocalDateTime.now());
                        factureDetailsExisting.setUserUpd(lawfirmToken.getUsername());
                    }
                }
                if (!factureDetailsExist) {
                    factureDetails = new TFactureDetails();
                    factureDetails.setDescription(invoiceDetailsDTO.getDescription());
                    factureDetails.setHtva(invoiceDetailsDTO.getMontantHt());
                    factureDetails.setTtc(invoiceDetailsDTO.getMontant());
                    factureDetails.setTva(invoiceDetailsDTO.getTva());
                    factureDetails.setDateUpd(LocalDateTime.now());
                    factureDetails.setUserUpd(lawfirmToken.getUsername());
                    tFacturesOptional.get().addTFactureDetails(factureDetails);
                }
            }
        } else {
            log.warn("InvoiceDetailsList is empty {}", invoiceDTO.getInvoiceDetailsDTOList());
            tFacturesOptional.get().getTFactureDetailsList().clear();
        }


        // prestation
        if (invoiceDTO.getPrestationIdList() != null) {
            List<TFactureTimesheet> tFactureTimesheetList = new ArrayList<>();
            tFacturesOptional.get().getTFactureTimesheetList().clear();
            facturesRepository.save(tFacturesOptional.get());

            for (Long prestationId : invoiceDTO.getPrestationIdList()) {
                TFactureTimesheet tFactureTimesheet = new TFactureTimesheet();
                tFactureTimesheet.setTsId(prestationId);
                tFactureTimesheet.setCreUser(lawfirmToken.getUsername());
                Optional<TTimesheet> tTimesheetOptional = timesheetRepository.findById(prestationId);
                if (tTimesheetOptional.isPresent()) {
                    tTimesheetOptional.get().setIdTs(prestationId);
                    tFactureTimesheet.setTTimesheet(tTimesheetOptional.get());
                }
                tFactureTimesheetList.add(tFactureTimesheet);
                tFactureTimesheet.setTFactures(tFacturesOptional.get());
            }
            tFacturesOptional.get().getTFactureTimesheetList().addAll(tFactureTimesheetList);

        } else {
            log.warn("PrestationIdList is empty {}", invoiceDTO.getPrestationIdList());
            tFacturesOptional.get().getTFactureTimesheetList().clear();
        }

        // frais admin
        if (!CollectionUtils.isEmpty(invoiceDTO.getFraisAdminIdList())) {
            List<FactureFraisAdmin> tFactureTimesheetList = new ArrayList<>();
            tFacturesOptional.get().getFraisAdminList().clear();
            facturesRepository.save(tFacturesOptional.get());

            log.debug("frais admin clear");
            for (Long deboursId : invoiceDTO.getFraisAdminIdList()) {
                FactureFraisAdmin factureFraisAdmin = new FactureFraisAdmin();
                factureFraisAdmin.setDeboursId(deboursId);
                factureFraisAdmin.setCreUser(lawfirmToken.getUsername());
                Optional<TDebour> debourOptional = tDebourRepository.findById(deboursId);
                if (debourOptional.isPresent()) {
                    debourOptional.get().setIdDebour(deboursId);
                    factureFraisAdmin.setTDebour(debourOptional.get());
                }
                tFactureTimesheetList.add(factureFraisAdmin);
                factureFraisAdmin.setTFactures(tFacturesOptional.get());
            }
            tFacturesOptional.get().getFraisAdminList().addAll(tFactureTimesheetList);
            log.debug("frais admin added");

        } else {
            log.warn("FraisAdminIdList is empty {}", invoiceDTO.getFraisAdminIdList());
            tFacturesOptional.get().getFraisAdminList().clear();
        }

        // debours
        if (!CollectionUtils.isEmpty(invoiceDTO.getDeboursIdList())) {
            List<FactureFraisDebours> factureFraisDeboursList = new ArrayList<>();
            tFacturesOptional.get().getFraisDeboursList().clear();
            facturesRepository.save(tFacturesOptional.get());

            log.debug("frais debours clear");
            for (Long fraisId : invoiceDTO.getDeboursIdList()) {
                FactureFraisDebours factureFraisDebours = new FactureFraisDebours();
                factureFraisDebours.setFraisId(fraisId);
                factureFraisDebours.setCreUser(lawfirmToken.getUsername());
                factureFraisDeboursList.add(factureFraisDebours);
                factureFraisDebours.setTFactures(tFacturesOptional.get());
            }
            tFacturesOptional.get().getFraisDeboursList().addAll(factureFraisDeboursList);
            log.debug("frais debours added");

        } else {
            log.warn("DeboursIdList is empty {}", invoiceDTO.getDeboursIdList());
            tFacturesOptional.get().getFraisDeboursList().clear();
        }


        // frais collaboration
        if (!CollectionUtils.isEmpty(invoiceDTO.getFraisCollaborationIdList())) {
            List<FactureFraisCollaboration> factureFraisDeboursList = new ArrayList<>();
            tFacturesOptional.get().getFraisCollaborationArrayList().clear();
            facturesRepository.save(tFacturesOptional.get());

            log.debug("frais coll clear");
            for (Long fraisId : invoiceDTO.getFraisCollaborationIdList()) {
                FactureFraisCollaboration factureFraisCollaboration = new FactureFraisCollaboration();
                factureFraisCollaboration.setFraisId(fraisId);
                factureFraisCollaboration.setCreUser(lawfirmToken.getUsername());
                factureFraisDeboursList.add(factureFraisCollaboration);
                factureFraisCollaboration.setTFactures(tFacturesOptional.get());
            }
            tFacturesOptional.get().getFraisCollaborationArrayList().addAll(factureFraisDeboursList);
            log.debug("frais coll added");

        } else {
            log.warn("FraisCollaborationIdList is empty {}", invoiceDTO.getFraisCollaborationIdList());
            tFacturesOptional.get().getFraisCollaborationArrayList().clear();
        }

        tFacturesOptional.get().setMontant(invoiceDTO.getMontant() != null ? invoiceDTO.getMontant() : BigDecimal.ZERO);

        tFacturesOptional.get().setDateValue(invoiceDTO.getDateValue());
        tFacturesOptional.get().setDateEcheance(invoiceDTO.getDateEcheance());

        if (invoiceDTO.getEcheanceId() != null) {
            Optional<TFactureEcheance> factureEcheance = tfactureEcheanceRepository.findById(invoiceDTO.getEcheanceId());

            factureEcheance.ifPresent(tFactureEcheance -> tFacturesOptional.get().setIdEcheance(invoiceDTO.getEcheanceId()));
        }
        EnumFactureType enumFactureType = EnumFactureType.fromId(invoiceDTO.getTypeId());
        if (enumFactureType == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice type cannot be nuull");
        }
        tFacturesOptional.get().setIdFactureType(enumFactureType);

        if (invoiceDTO.getClientId() != null) {
            Optional<TClients> tClientsOptional = clientRepository.findById(invoiceDTO.getClientId());

            tClientsOptional.ifPresent(tclient -> tFacturesOptional.get().setIdTiers(invoiceDTO.getClientId()));
        }
        if (invoiceDTO.getDossierId() != null) {
            Optional<TDossiers> tDossiersOptional = dossierRepository.findById(invoiceDTO.getDossierId());

            tDossiersOptional.ifPresent(dossiers -> tFacturesOptional.get().setIdDoss(invoiceDTO.getDossierId()));
        }
        facturesRepository.save(tFacturesOptional.get());
        if (bigleLegalActivation) {
            invoiceProducer.createDocument(lawfirmToken, createInvoiceTemplateDTO(invoiceDTO, lawfirmEntityOptional.get()));
        }

        log.debug("Leaving updateInvoice with invoice id : {}", invoiceDTO.getId());
        return invoiceDTO;
    }

    public void commonRules(InvoiceDTO invoiceDTO, String vcKey, Long userId) throws ResponseStatusException {
        if (invoiceDTO == null) {
            log.warn("Invoice is not filled in");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invoice is not filled in");
        }

        if (CollectionUtils.isEmpty(invoiceDTO.getInvoiceDetailsDTOList())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "facture details cannot be empty");
        }

        if (invoiceDTO.getDossierId() != null) {
            Optional<TDossiers> tDossiers = dossierRepository.findById(invoiceDTO.getDossierId());
            if (tDossiers.isEmpty()) {
                log.warn("Dossier is not found {} ", invoiceDTO.getDossierId());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier is not found");
            } else {
                // check if the prestation are coming from vckey and dossier
                Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

                if (lawfirmUsers.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "this dossier id " + invoiceDTO.getDossierId() + " is not linked with the vckey");
                }

                if (!CollectionUtils.isEmpty(invoiceDTO.getPrestationIdList())) {
                    log.debug("Prestation {} for the invoice", invoiceDTO.getPrestationIdList());

                    Long prestationCount = timesheetRepository.countAllByIdAndDossierId(invoiceDTO.getPrestationIdList(), invoiceDTO.getDossierId(), lawfirmUsers.get().getId());

                    // if the count is different then a prestation is not correctly linked to dossier id
                    if (prestationCount != invoiceDTO.getPrestationIdList().size()) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The TS list is not linked with the dossier");
                    }
                }
                if (!CollectionUtils.isEmpty(invoiceDTO.getFraisAdminIdList())) {
                    log.debug("Frais admin {} for the invoice", invoiceDTO.getFraisAdminIdList());

                    Long deboursCount = tDebourRepository.countAllByIdAndDossierId(invoiceDTO.getFraisAdminIdList(), invoiceDTO.getDossierId(), lawfirmUsers.get().getId());

                    // if the count is different then a prestation is not correctly linked to dossier id
                    if (deboursCount != invoiceDTO.getFraisAdminIdList().size()) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The frais admin list is not linked with the dossier");
                    }
                }
                if (!CollectionUtils.isEmpty(invoiceDTO.getDeboursIdList())) {
                    log.debug("Debours {} for the invoice", invoiceDTO.getDeboursIdList());

                    Long fraisDebours = fraisRepository.countAllFraisDeboursByIdAndDossierId(invoiceDTO.getDeboursIdList(), invoiceDTO.getDossierId(), lawfirmUsers.get().getId());

                    // if the count is different then a prestation is not correctly linked to dossier id
                    if (fraisDebours != invoiceDTO.getDeboursIdList().size()) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The debours list is not linked with the dossier");
                    }
                }
                if (!CollectionUtils.isEmpty(invoiceDTO.getFraisCollaborationIdList())) {
                    log.debug("frais collaborat {} for the invoice", invoiceDTO.getFraisCollaborationIdList());

                    Long fraisCollabCount = fraisRepository.countAllFraisCollaByIdAndDossierId(invoiceDTO.getFraisCollaborationIdList(), invoiceDTO.getDossierId(), lawfirmUsers.get().getId());

                    // if the count is different then a prestation is not correctly linked to dossier id
                    if (fraisCollabCount != invoiceDTO.getFraisCollaborationIdList().size()) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The frais collab list is not linked with the dossier");
                    }
                }
            }

        }

        if (invoiceDTO.getClientId() != null) {
            Optional<TClients> tClients = clientRepository.findById(invoiceDTO.getClientId());
            if (tClients.isEmpty()) {
                log.warn("CLient is not found {} ", invoiceDTO.getClientId());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client is not found");
            }
        }

        Optional<RefPoste> refPoste = refPosteRepository.findById(invoiceDTO.getPosteId());
        if (refPoste.isEmpty()) {
            log.warn("Post is not found {} ", invoiceDTO.getPosteId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post is not found");
        }

        if (invoiceDTO.getEcheanceId() != null) {
            Optional<TFactureEcheance> tFactureEcheanceOptional = tfactureEcheanceRepository.findById(invoiceDTO.getEcheanceId());
            if (tFactureEcheanceOptional.isEmpty()) {
                log.warn("Facture Echeance is not found {}", invoiceDTO.getEcheanceId());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Facture Echeance is not found");
            }
        }

        Optional<EnumFactureType> enumFactureType = Optional.ofNullable(EnumFactureType.fromId(invoiceDTO.getTypeId()));
        if (enumFactureType.isEmpty()) {
            log.warn("FactureType is not found {} ", invoiceDTO.getTypeId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "FactureType is not found");
        }

    }

    @Override
    public Long countInvoiceDetailsByVat(BigDecimal vat) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering countInvoiceDetailsByVat {} and vckey {}", vat, lawfirmToken.getVcKey());

        // chek if it's used in t_factures
        Long vatNb = facturesRepository.countAllByVcKeyAndVat(lawfirmToken.getVcKey(), vat);
        log.debug("Number of invoices {} found in the vckey {}", vatNb, lawfirmToken.getVcKey());

        return vatNb;
    }

    @Override
    public InvoiceDTO validateInvoice(Long invoiceId, String vcKey) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Entering validateInvoice implementation with invoiceId = {} and vcKey {}", invoiceId, lawfirmToken.getVcKey());

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(vcKey);

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lawfirm is unknown");
        }

        InvoiceDTO invoiceDTO = null;
        if (invoiceId == null) {
            log.warn("invoiceId is null {}", invoiceId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoiceId is null");
        }

        Optional<TFactures> tFacturesOptional = facturesRepository.findByIdFactureAndVcKey(invoiceId, vcKey);

        if (tFacturesOptional.isEmpty()) {
            log.warn("invoice does not exist {}", invoiceId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice does not exist");
        }

        if (tFacturesOptional.get().getValid()) {
            log.warn("invoice is already valid {}", invoiceId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice is already valid");
        } else {
            if (tFacturesOptional.get().getIdFactureType().equals(EnumFactureType.TEMP)
                    || tFacturesOptional.get().getIdFactureType().equals(EnumFactureType.TEMP_NC)) {

                Integer yearFacture = tFacturesOptional.get().getDateValue().getYear();

                Long alreadyInvoiceForCurrentYear = facturesRepository.countInvoiceByVcAndYear(vcKey, EnumFactureType.SELL, yearFacture);

                Integer numFacture;

                if (tFacturesOptional.get().getIdFactureType().equals(EnumFactureType.TEMP)) {
                    tFacturesOptional.get().setIdFactureType(EnumFactureType.SELL);
                } else if (tFacturesOptional.get().getIdFactureType().equals(EnumFactureType.TEMP_NC)) {
                    tFacturesOptional.get().setIdFactureType(EnumFactureType.CREDIT);
                }

                if (alreadyInvoiceForCurrentYear == 0) {
                    numFacture = 0;
                } else {
                    numFacture = facturesRepository.getMaxNumFactureAndValid(vcKey, tFacturesOptional.get().getIdFactureType(), yearFacture);
                }

                Optional<LawfirmDTO> lawfirmDTOOptional = lawfirmRepository.findLawfirmDTOByVckey(vcKey);

                if (lawfirmDTOOptional.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lawfirm is not found");
                }

                if (numFacture == null) {
                    numFacture = lawfirmDTOOptional.get().getStartInvoiceNumber();
                } else {
                    numFacture++;
                }
                tFacturesOptional.get().setNumFacture(numFacture);

                String factureRef = InvoicesUtils.getInvoiceReference(yearFacture, tFacturesOptional.get().getNumFacture(), tFacturesOptional.get().getIdFactureType());
                tFacturesOptional.get().setFactureRef(factureRef);
                tFacturesOptional.get().setYearFacture(yearFacture);
            }

            tFacturesOptional.get().setNumFactTemp(0);
            tFacturesOptional.get().setValid(true);
        }

        facturesRepository.save(tFacturesOptional.get());

        invoiceDTO = getInvoiceById(invoiceId, vcKey);

        if (bigleLegalActivation) {
            // Create invoice with Template API
            invoiceProducer.createDocument(lawfirmToken, createInvoiceTemplateDTO(invoiceDTO, lawfirmEntityOptional.get()));
        } else {
            sendInvoiceToDrive(lawfirmToken, invoiceId, tFacturesOptional.get().getFactureRef(), tFacturesOptional.get().getYearFacture());
        }

        log.debug("Leaving validateInvoice implementation with isValid = {}", invoiceDTO.getValid());

        return invoiceDTO;
    }

    @Override
    public ByteArrayResource downloadInvoice(Long invoiceId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Entering downloadInvoice implementation with invoiceId = {} and vcKey {}", invoiceId, lawfirmToken.getVcKey());
        if (invoiceId == null) {
            log.warn("invoiceId is null {}", invoiceId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoiceId is null");
        }

        Optional<TFactures> tFacturesOptional = facturesRepository.findByIdFactureAndVcKey(invoiceId, lawfirmToken.getVcKey());

        if (tFacturesOptional.isEmpty()) {
            log.warn("invoice does not exist {}", invoiceId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice does not exist");
        }
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());
        return driveApi.downloadFile(lawfirmToken, DriveUtils.INVOICE_PATH + tFacturesOptional.get().getYearFacture() + "/" + tFacturesOptional.get().getFactureRef() + ".pdf");
    }

    @Override
    public Long deleteInvoiceById(Long invoiceId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<TFactures> tFacturesOptional = facturesRepository.findByIdFactureAndVcKey(invoiceId, lawfirmToken.getVcKey());
        if (tFacturesOptional.isEmpty()) {
            log.warn("invoice is not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invoice is not found");
        }
        facturesRepository.delete(tFacturesOptional.get());
        return invoiceId;
    }

    @Override
    public InvoiceDTO totalInvoiceByDossierId(Long dossierId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering totalInvoiceByDossierId {} and vckey {}", dossierId, lawfirmToken.getVcKey());
        InvoiceDTO invoiceDTO = new InvoiceDTO();

        // sum of all invoice per dossier
        BigDecimal tvacInvoiceByVcKey = facturesRepository.sumTvacInvoiceByVcKey(lawfirmToken.getVcKey(), dossierId);
        invoiceDTO.setMontant(tvacInvoiceByVcKey);
        log.debug("Total {} (invoice) found in the vckey {} and dossierId {}", tvacInvoiceByVcKey, lawfirmToken.getVcKey(), dossierId);

        // sum of all invoice paid (honoraire) per dossier
        BigDecimal sumAllHonoByVcKey = fraisRepository.sumAllHonoTtcOnlyInvoiceByVcKey(dossierId, lawfirmToken.getVcKey());
        invoiceDTO.setTotalHonoraire(sumAllHonoByVcKey);
        log.debug("Total {} (invoice) found in the vckey {} and dossierId {}", sumAllHonoByVcKey, lawfirmToken.getVcKey(), dossierId);

        return invoiceDTO;
    }

    @Override
    public List<PrestationSummary> getPrestationByDossierId(Long invoiceId, Long dossierId, Long userId, String vcKey, Boolean filterInvoicePrestation) {
        log.debug("getPrestationByDossierId with user {} and dossierId {}", userId, dossierId);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            List<Object[]> prestationSummaryList = timesheetRepository.findAllByInvoiceIdDossierId(invoiceId, dossierId, lawfirmUsers.get().getId(), filterInvoicePrestation);

            return prestationSummaryList.stream().map(r -> {
                BigInteger longConvertId = (BigInteger) r[0];
                BigInteger longConvertDossierId = (BigInteger) r[1];
                Integer longConvertNumDossier = (Integer) r[3];
                BigInteger longConvertIdGest = (BigInteger) r[4];
                Short longConvertTsType = (Short) r[6];
                Long longConvertTime = r[9] != null ? ((Date) r[9]).getTime() : null;
                String convertDh = (String) r[10];
                String convertDm = (String) r[11];
                BigDecimal convertVAT = (BigDecimal) r[13];
                Integer convertForfait = (Integer) r[14];
                boolean boolForfait = convertForfait != null && convertForfait == 1;
                ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(longConvertTime),
                        ZoneId.systemDefault());
                BigDecimal longConvertForfaitHt = (BigDecimal) r[15];
                BigInteger longConvertFactureTimesheetId = (BigInteger) r[16];
                //object is a BigInteger
                BigInteger convertInvoiceChecked = (BigInteger) r[17];
                boolean testInvoiceChecked = convertInvoiceChecked != null && convertInvoiceChecked.longValue() != 0;
                BigInteger convertAlreadyInvoiced = (BigInteger) r[18];
                boolean testAlreadyInvoiced = convertAlreadyInvoiced != null && convertAlreadyInvoiced.longValue() != 0;
                String factExtRef = (r[19] != null ? (String) r[19] : null);
                Long factId = r[20] != null ? ((BigInteger) r[20]).longValue() : null;

                PrestationSummary prestationSummary = new PrestationSummary(
                        (longConvertId != null ? longConvertId.longValue() : null),
                        (longConvertDossierId != null ? longConvertDossierId.longValue() : null),
                        (r[2] != null ? (String) r[2] : null),
                        (longConvertNumDossier != null ? longConvertNumDossier.longValue() : null),
                        (longConvertIdGest != null ? longConvertIdGest.longValue() : null),
                        (r[5] != null ? (String) r[5] : null),
                        (longConvertTsType != null ? longConvertTsType.intValue() : null),
                        (r[7] != null ? (String) r[7] : null),
                        (r[8] != null ? (Integer) r[8] : null),
                        zdt,
                        (convertDh != null ? new BigDecimal(convertDh) : null),
                        (convertDm != null ? new BigDecimal(convertDm) : null),
                        (r[12] != null ? (String) r[12] : null),
                        (convertVAT),
                        boolForfait,
                        longConvertForfaitHt,
                        longConvertFactureTimesheetId != null ? longConvertFactureTimesheetId.longValue() : null,
                        testInvoiceChecked,
                        testAlreadyInvoiced,
                        factId,
                        factExtRef
                );
                return prestationSummary;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<FraisAdminDTO> getFraisAdminByDossierId(Long invoiceId, Long dossierId, Long userId, String vcKey, Boolean filterInvoicePrestation) {
        log.debug("getFraisAdminByDossierId with user {} and dossierId {}", userId, dossierId);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            List<Object[]> fraisAdminDTOS = tDebourRepository.findAllByInvoiceIdDossierId(invoiceId, dossierId, lawfirmUsers.get().getId(), filterInvoicePrestation);

            return fraisAdminDTOS.stream().map(r -> {
                        Long longConvertId = r[0] != null ? ((Integer) r[0]).longValue() : null;
                        Long longConvertIdDebourType = r[1] != null ? ((Short) r[1]).longValue() : null;
                        BigDecimal longConvertPricePerUnit = r[3] != null ? BigDecimal.valueOf(((Double) r[3])) : null;
                        Integer longConvertUnit = r[4] != null ? Integer.valueOf(((Short) r[4])) : null;
                        Integer longConvertMesureTypeId = r[5] != null ? Integer.valueOf(((Byte) r[5])) : null;
                        Long longConvertIdDoss = r[7] != null ? ((BigInteger) r[7]).longValue() : null;
                        Long longConvertTime = r[10] != null ? ((Date) r[10]).getTime() : null;
                        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(longConvertTime),
                                ZoneId.systemDefault());

                        String convertComment = r[11] != null ? ((String) r[11]) : null;
                        Long convertFactureFraisId = r[12] != null ? ((BigInteger) r[12]).longValue() : null;
                        boolean testInvoiceChecked = r[13] != null && ((BigInteger) r[13]).longValue() != 0;
                        boolean testAlreadyInvoiced = r[14] != null && ((BigInteger) r[14]).longValue() != 0;
                        String factExtRef = (r[15] != null ? (String) r[15] : null);
                        Long factId = r[16] != null ? ((BigInteger) r[16]).longValue() : null;

                        return new FraisAdminDTO(
                                longConvertId,
                                longConvertIdDebourType,
                                (r[2] != null ? (String) r[2] : null),
                                longConvertPricePerUnit,
                                longConvertUnit,
                                longConvertMesureTypeId,
                                (r[6] != null ? (String) r[6] : null),
                                longConvertIdDoss,
                                (r[8] != null ? (String) r[8] : null),
                                zdt,
                                convertComment,
                                convertFactureFraisId,
                                testInvoiceChecked,
                                testAlreadyInvoiced,
                                factId,
                                factExtRef
                        );
                    })
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public List<ComptaDTO> getDeboursByDossierId(Long invoiceId, Long dossierId, Long userId, String vcKey, Boolean filterInvoicePrestation) {
        log.debug("getDeboursByDossierId with user {} and dossierId {}", userId, dossierId);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            List<Object[]> comptaDTOList = fraisRepository.findAllDeboursByInvoiceIdDossierId(invoiceId, dossierId, lawfirmUsers.get().getId(), filterInvoicePrestation);

            return comptaDTOList.stream().map(r -> {
                BigInteger longConvertId = (BigInteger) r[0];
                BigInteger longFactureFraisId = (BigInteger) r[7];
                BigInteger longConvertInvoiceChecked = (BigInteger) r[9];
                boolean testInvoiceChecked = longConvertInvoiceChecked != null && longConvertInvoiceChecked.longValue() != 0;
                BigInteger longConvertAlreadyInvoiced = (BigInteger) r[10];
                boolean testAlreadyInvoiced = longConvertAlreadyInvoiced != null && longConvertAlreadyInvoiced.longValue() != 0;
                String factExtRef = (r[11] != null ? (String) r[11] : null);
                Long factId = r[12] != null ? ((BigInteger) r[12]).longValue() : null;
                Integer idTransaction = r[13] != null ? ((Integer) r[13]) : null;

                String language = lawfirmUsers.get().getUser().getLanguage();

                return new ComptaDTO(
                        (longConvertId != null ? longConvertId.longValue() : null),
                        (r[1] != null ? (String) r[1] : null),
                        (r[2] != null ? (Integer) r[2] : null),
                        (r[3] != null ? (String) r[3] : null),
                        (r[4] != null ? (BigDecimal) r[4] : null),
                        (r[5] != null ? (BigDecimal) r[5] : null),
                        (r[6] != null ? (String) r[6] : null),
                        (longFactureFraisId != null ? longFactureFraisId.longValue() : null),
                        (testInvoiceChecked),
                        (testAlreadyInvoiced),
                        factId,
                        factExtRef,
                        idTransaction,
                        language
                );

            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ComptaDTO> getFraisCollabByDossierId(Long invoiceId, Long dossierId, Long userId, String vcKey, Boolean filterInvoicePrestation) {
        log.debug("getFraisCollabByDossierId with user {} and dossierId {}", userId, dossierId);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            List<Object[]> comptaDTOList = fraisRepository.findAllCollabByInvoiceIdDossierId(invoiceId, dossierId, lawfirmUsers.get().getId(), filterInvoicePrestation);
            return comptaDTOList.stream().map(r -> {
                BigInteger longConvertId = (BigInteger) r[0];
                BigInteger longFactureFraisId = (BigInteger) r[7];
                BigInteger longConvertInvoiceChecked = (BigInteger) r[9];
                boolean testInvoiceChecked = longConvertInvoiceChecked != null && longConvertInvoiceChecked.longValue() != 0;
                BigInteger longConvertAlreadyInvoiced = (BigInteger) r[10];
                boolean testAlreadyInvoiced = longConvertAlreadyInvoiced != null && longConvertAlreadyInvoiced.longValue() != 0;
                String factExtRef = (r[11] != null ? (String) r[11] : null);
                Long factId = r[12] != null ? ((BigInteger) r[12]).longValue() : null;
                Integer idTransaction = r[13] != null ? (Integer) r[13] : null;

                String language = lawfirmUsers.get().getUser().getLanguage();

                return new ComptaDTO(
                        (longConvertId != null ? longConvertId.longValue() : null),
                        (r[1] != null ? (String) r[1] : null),
                        (r[2] != null ? (Integer) r[2] : null),
                        (r[3] != null ? (String) r[3] : null),
                        (r[4] != null ? (BigDecimal) r[4] : null),
                        (r[5] != null ? (BigDecimal) r[5] : null),
                        (r[6] != null ? (String) r[6] : null),
                        (longFactureFraisId != null ? longFactureFraisId.longValue() : null),
                        (testInvoiceChecked),
                        (testAlreadyInvoiced),
                        factId,
                        factExtRef,
                        idTransaction,
                        language
                );

            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public Long countAllActiveByVcKey(String vckey) {
        log.info("Entering countAllActiveByVcKey with vckey {}", vckey);

        return facturesRepository.countAllActiveByVcKey(vckey);
    }

    private void sendInvoiceToDrive(LawfirmToken lawfirmToken, Long invoiceId, String factureRef, Integer yearFacture) {
        log.debug("Entering sendInvoiceToDrive invoiceId = {} for facture ref = {}", invoiceId, factureRef);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                // get invoice pdf
                ByteArrayResource resourceInvoice = reportApi.getInvoice(lawfirmToken, invoiceId);

                log.debug("Getting invoice pdf factureRef = {} ", factureRef);
                DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());
                // send it to Udrive
                driveApi.uploadFile(lawfirmToken, resourceInvoice.getByteArray(), factureRef + ".pdf", DriveUtils.INVOICE_PATH + yearFacture + "/");
                log.debug("Leaving sendInvoiceToDrive with factureRef = {}", factureRef);
            } catch (Exception e) {
                log.error("Error wghile sending invoice (report or drive)", e);
                throw new RuntimeException("Error wghile sending invoice (report or drive)");
            }
        }
    }

    private InvoiceTemplateDTO createInvoiceTemplateDTO(InvoiceDTO invoiceDTO, LawfirmEntity lawfirmEntity) {
        log.debug("Entering createInvoiceTemplateDTO invoiceDTO = {} and lawfirmEntity = {}", invoiceDTO, lawfirmEntity);
        InvoiceTemplateDTO main = new InvoiceTemplateDTO();

        main.setId(invoiceDTO.getId());

        //TODO Set logo
        main.setLogo(null);

        main.setCompanyName(lawfirmEntity.getObjetsocial());

        main.setAddress(lawfirmEntity.getStreet());
        main.setPostalCode(lawfirmEntity.getPostalCode());
        main.setCity(lawfirmEntity.getCity());
        main.setCountry(lawfirmEntity.getCountryCode());
        main.setEmail(lawfirmEntity.getEmail());
        main.setInvoiceNumber(Long.valueOf(invoiceDTO.getNumFacture()));

        main.setRefInvoice(invoiceDTO.getReference());
        if (invoiceDTO.getDossierItem() != null) {
            main.setDossierNumber(invoiceDTO.getDossierItem().label);
        }

        main.setInvoiceDate(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(invoiceDTO.getDateValue()));

        main.setEcheanceDate(invoiceDTO.getDateEcheance() == null ? null : DateTimeFormatter.ofPattern("dd/MM/yyyy").format(invoiceDTO.getDateEcheance()));

        main.setEcheanceVisible(invoiceDTO.getDateEcheance() == null ? 0 : 1);

        //Client
        Optional<TClients> clientsEntity = clientRepository.findById(invoiceDTO.getClientId());
        if (clientsEntity.isPresent()) {
            String fullname = ClientsUtils.getFullname(clientsEntity.get().getF_nom(), clientsEntity.get().getF_prenom(), clientsEntity.get().getF_company());
            main.setClientName(fullname);
            main.setClientStreet(clientsEntity.get().getF_rue() == null ? "" : clientsEntity.get().getF_rue());
            main.setClientCp(clientsEntity.get().getF_cp() == null ? "" : clientsEntity.get().getF_cp());
            main.setClientCity(clientsEntity.get().getF_ville() == null ? "" : clientsEntity.get().getF_ville());
            main.setClientCountry(clientsEntity.get().getId_country_alpha3() == null ? "" : clientsEntity.get().getId_country_alpha3());
        }

        // Invoice Details
        List<Item> itemList = new ArrayList<>();
        for (InvoiceDetailsDTO dto : invoiceDTO.getInvoiceDetailsDTOList()) {
            Item item = new Item();
            item.setDescription(dto.getDescription());
            item.setAmount(dto.getMontantHt().setScale(2, RoundingMode.HALF_UP));
            item.setAmountTax(dto.getMontant().setScale(2, RoundingMode.HALF_UP));
            item.setVat(dto.getTva().setScale(2, RoundingMode.HALF_UP));

            itemList.add(item);
        }
        main.setItem(itemList);

        // Prestation
        List<TFactureTimesheet> tFactureTimesheetList = tFactureTimesheetRepository.findAllByTFactures(invoiceDTO.getId());
        List<ItemPrestation> itemPrestationList = new ArrayList<>();
        for (TFactureTimesheet tFactureTimesheet : tFactureTimesheetList) {
            if (tFactureTimesheet.getTTimesheet() != null) {
                ItemPrestation itemPrestation = new ItemPrestation();

                itemPrestation.setDescriptionPrestation(tFactureTimesheet.getTTimesheet().getTTimesheetType().getDescription());
                if (!tFactureTimesheet.getTTimesheet().getForfait()) {
                    itemPrestation.setAmountPrestation(BigDecimal.valueOf(((((tFactureTimesheet.getTTimesheet().getDh().doubleValue() * 60) + tFactureTimesheet.getTTimesheet().getDm().doubleValue()) / 60) * tFactureTimesheet.getTTimesheet().getCouthoraire())).setScale(2, RoundingMode.HALF_UP));
                    itemPrestation.setAmountTaxPrestation(BigDecimal.valueOf((((((tFactureTimesheet.getTTimesheet().getDh().doubleValue() * 60) + tFactureTimesheet.getTTimesheet().getDm().doubleValue()) / 60) * tFactureTimesheet.getTTimesheet().getCouthoraire()) * (1 + (tFactureTimesheet.getTTimesheet().getVat().doubleValue() / 100)))).setScale(2, RoundingMode.HALF_UP));
                } else {
                    itemPrestation.setAmountPrestation(tFactureTimesheet.getTTimesheet().getForfaitHt());
                    itemPrestation.setAmountTaxPrestation(BigDecimal.valueOf((tFactureTimesheet.getTTimesheet().getForfaitHt().doubleValue() * (1 + (tFactureTimesheet.getTTimesheet().getVat().doubleValue() / 100)))).setScale(2, RoundingMode.HALF_UP));
                }

                itemPrestation.setVatPrestation(tFactureTimesheet.getTTimesheet().getVat());
                itemPrestation.setDatePrestation(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(tFactureTimesheet.getTTimesheet().getDateAction()));
                itemPrestationList.add(itemPrestation);
            }
        }
        main.setItemPrestation(itemPrestationList);

        // Frais Admin
        List<Object[]> fraisAdminDTOS = tDebourRepository.findAllByInvoiceIdDossierId(invoiceDTO.getId());

        List<ItemFraisAdmin> itemFraisAdminList = fraisAdminDTOS.stream().map(r -> {
                    String debourTypeDescription = (r[0] != null ? (String) r[0] : null);
                    String mesureDesc = r[1] != null ? (String) r[1] : null;
                    BigDecimal longConvertPricePerUnit = r[2] != null ? BigDecimal.valueOf(((Double) r[2])) : null;
                    Integer unit = r[3] != null ? Integer.valueOf(((Short) r[3])) : null;
                    BigDecimal unit2 = BigDecimal.valueOf((unit));

                    return new ItemFraisAdmin(debourTypeDescription, mesureDesc, longConvertPricePerUnit, unit2);
                })
                .collect(Collectors.toList());
        main.setItemFraisAdmins(itemFraisAdminList);

        // Debours
        List<ItemDebours> itemDeboursList = fraisRepository.findAllDeboursByIdPosteAndFactureId(invoiceDTO.getId());
        main.setItemDebours(itemDeboursList);

        // Collaboration
        List<ItemFraisColla> itemFraisCollaList = fraisRepository.findAllCollaByIdPosteAndFactureId(invoiceDTO.getId());
        main.setItemFraisCollas(itemFraisCollaList);


        // ItemVat
        List<ItemVat> itemVatList = new ArrayList<>();
        List<GroupVatDTO> groupVatDTOList = tFactureDetailsRepository.findItemVat(invoiceDTO.getId());

        for (GroupVatDTO groupVatDTO : groupVatDTOList) {
            ItemVat itemVat = new ItemVat();
            itemVat.setVatVat(groupVatDTO.getVatVat().setScale(2, RoundingMode.HALF_UP));
            itemVat.setGroupAmountVat(groupVatDTO.getGroupAmountVat().setScale(2, RoundingMode.HALF_UP));
            itemVat.setDescriptionVat("");
            itemVatList.add(itemVat);
        }

        main.setItemVat(itemVatList);

        main.setCurrency(lawfirmEntity.getCurrency().getSymbol());
        main.setTotalHtAmount(BigDecimal.valueOf(invoiceDTO.getInvoiceDetailsDTOList().stream().mapToDouble(i -> i.getMontantHt().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP));
        main.setTotalAmount(invoiceDTO.getMontant() == null ? null : invoiceDTO.getMontant().setScale(2, RoundingMode.HALF_UP));

        main.setTitleName(invoiceDTO.getTypeItem().getLabel());

        main.setIsValid(invoiceDTO.getValid());

        log.debug("Leaving createInvoiceTemplateDTO");
        return main;
    }

}
