package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.dto.InvoiceDTO;
import com.ulegalize.dto.ItemBigDecimalDto;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefTransaction;
import com.ulegalize.enumeration.EnumTType;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.DTOToFraisEntityConverter;
import com.ulegalize.lawfirm.model.converter.EntityToComptaDTOConverter;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.repository.*;
import com.ulegalize.lawfirm.service.ComptaService;
import com.ulegalize.lawfirm.service.SearchService;
import com.ulegalize.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ComptaServiceImpl implements ComptaService {
    private final TFraisRepository tFraisRepository;
    private final TGridRepository tGridRepository;
    private final RefCompteRepository refCompteRepository;
    private final DossierRepository dossierRepository;
    private final TFacturesRepository tFacturesRepository;
    private final ClientRepository clientRepository;
    private final RefPosteRepository refPosteRepository;
    private final TVirtualcabVatRepository tVirtualcabVatRepository;
    private final EntityToComptaDTOConverter entityToComptaDTOConverter;
    private final DTOToFraisEntityConverter dtoToFraisEntityConverter;

    private final SearchService searchService;

    public ComptaServiceImpl(TFraisRepository tFraisRepository, TGridRepository tGridRepository, RefCompteRepository refCompteRepository, DossierRepository dossierRepository, TFacturesRepository tFacturesRepository, ClientRepository clientRepository, RefPosteRepository refPosteRepository, TVirtualcabVatRepository tVirtualcabVatRepository, EntityToComptaDTOConverter entityToComptaDTOConverter, DTOToFraisEntityConverter dtoToFraisEntityConverter, SearchService searchService) {
        this.tFraisRepository = tFraisRepository;
        this.tGridRepository = tGridRepository;
        this.refCompteRepository = refCompteRepository;
        this.dossierRepository = dossierRepository;
        this.tFacturesRepository = tFacturesRepository;
        this.clientRepository = clientRepository;
        this.refPosteRepository = refPosteRepository;
        this.tVirtualcabVatRepository = tVirtualcabVatRepository;
        this.entityToComptaDTOConverter = entityToComptaDTOConverter;
        this.dtoToFraisEntityConverter = dtoToFraisEntityConverter;
        this.searchService = searchService;
    }

    @Override
    public ComptaDTO getComptaById(Long fraisId, String vcKey, String language) {
        Optional<TFrais> fraisOptional = tFraisRepository.findByIdFraisAndVcKey(fraisId, vcKey);

        if (fraisOptional.isPresent()) {
            // todo convertor
            return entityToComptaDTOConverter.apply(fraisOptional.get(), EnumLanguage.fromshortCode(language));
        }
        return null;
    }

    @Override
    public List<ItemDto> getGridList() {
        return tGridRepository.findAllList();
    }

    @Override
    public Page<ComptaDTO> getAllComptaByDossierId(int limit, int offset, Long dossierId, String vcKey,
                                                   Boolean isDebours, Boolean isFraiCollaboration, Boolean honoraire, Boolean tiers, String language) {
        log.debug("Get all Compta with user {} limit {} and offset {} and dossierId {}", vcKey, limit, offset, dossierId);

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idPoste");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "idFrais");
        Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(order, order2));
        Page<TFrais> allCompta;

        if (isDebours != null && isDebours) {
            allCompta = tFraisRepository.findByDossierIAndDeboursdWithPagination(dossierId, vcKey, pageable);

        } else if (isFraiCollaboration != null && isFraiCollaboration) {
            allCompta = tFraisRepository.findByDossierIdAndFraisCollaborationWithPagination(dossierId, vcKey, pageable);

        } else if (honoraire != null && honoraire) {
            allCompta = tFraisRepository.findByDossierIdAndHonoraireWithPagination(dossierId, vcKey, pageable);

        } else if (tiers != null && tiers) {
            allCompta = tFraisRepository.findByDossierIdAndTiersWithPagination(dossierId, vcKey, pageable);

        } else {
            allCompta = tFraisRepository.findByDossierIdWithPagination(dossierId, vcKey, pageable);
        }

        List<ComptaDTO> comptaDTOList = entityToComptaDTOConverter.convertToList(allCompta.getContent(), EnumLanguage.fromshortCode(language));

        return new PageImpl<>(comptaDTOList, Pageable.unpaged(), allCompta.getTotalElements());
    }

    @Override
    public Page<ComptaDTO> getAllCompta(int limit, int offset, String vcKey, String searchCriteriaClient, String searchCriteriaNomenclature, String searchCriteriaPoste, Integer typeId, Integer searchCriteriaCompte, String language) {
        log.debug("Get all Compta with user {} limit {} and offset {}", vcKey, limit, offset);

        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id_frais");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "id_poste");
        Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(order, order2));
        String client = searchCriteriaClient != null && !searchCriteriaClient.isEmpty() ? searchCriteriaClient : "";
        String nomenclature = searchCriteriaNomenclature != null && !searchCriteriaNomenclature.isEmpty() ? searchCriteriaNomenclature : "";
        String poste = searchCriteriaPoste != null && !searchCriteriaPoste.isEmpty() ? searchCriteriaPoste : "%";
        String typeCompta = typeId != null ? String.valueOf(typeId) : "%";
        String compte = searchCriteriaCompte != null ? String.valueOf(searchCriteriaCompte) : "%";

        Page<TFrais> allCompta = tFraisRepository.findAllWithPagination(vcKey, client, nomenclature, poste, typeCompta, compte, pageable);

        List<ComptaDTO> comptaDTOList = entityToComptaDTOConverter.convertToList(allCompta.getContent(), EnumLanguage.fromshortCode(language));

        return new PageImpl<>(comptaDTOList, Pageable.unpaged(), allCompta.getTotalElements());
    }

    @Override
    public ComptaDTO updateCompta(ComptaDTO comptaDTO, String vcKey) {
        commonRuleToSave(comptaDTO);

        if (comptaDTO.getId() == null) {
            log.warn("Compta is not filled in {}", comptaDTO.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Compta is not filled in");
        }
        Optional<TFrais> tFraisOptional = tFraisRepository.findByIdFraisAndVcKey(comptaDTO.getId(), vcKey);
        if (tFraisOptional.isEmpty()) {
            log.warn("Compta does not exist {}", comptaDTO.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Compta does not exist");
        }
        comptaDTO.setVcKey(vcKey);

        TFrais tFrais = dtoToFraisEntityConverter.apply(comptaDTO, tFraisOptional.get());

        tFraisRepository.save(tFrais);

        return comptaDTO;
    }

    @Override
    public Long createCompta(ComptaDTO comptaDTO, String vcKey) {
        commonRuleToSave(comptaDTO);

        comptaDTO.setVcKey(vcKey);

        TFrais tFrais = dtoToFraisEntityConverter.apply(comptaDTO, new TFrais());
        tFrais.setDateUpd(LocalDateTime.now());

        tFraisRepository.save(tFrais);

        return tFrais.getIdFrais();
    }

    @Override
    public ItemBigDecimalDto getTvaDefaultCompta(Long userId, String vcKey) {
        Optional<TVirtualcabVat> tVirtualcabVatOptional = tVirtualcabVatRepository.findByVcKeyAndIsDefaultAndVATIsNotNull(vcKey, true);

        return tVirtualcabVatOptional.map(tVirtualcabVat -> new ItemBigDecimalDto(tVirtualcabVat.getVAT(), tVirtualcabVat.getVAT().toString())).orElse(null);
    }

    @Override
    public InvoiceDTO totalHonoraireByDossierId(Long dossierId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering totalHonoraireByDossierId {} and vckey {}", dossierId, lawfirmToken.getVcKey());
        InvoiceDTO invoiceDTO = new InvoiceDTO();

        // sum of all invoice paid (honoraire) per dossier
        BigDecimal sumAllHonoByVcKey = tFraisRepository.sumAllHonoTtcByVcKey(dossierId, lawfirmToken.getVcKey());
        invoiceDTO.setTotalHonoraire(sumAllHonoByVcKey);
        log.debug("Total {} (invoice) found in the vckey {} and dossierId {}", sumAllHonoByVcKey, lawfirmToken.getVcKey(), dossierId);

        return invoiceDTO;
    }

    @Override
    public ComptaDTO totalThirdPartyByDossierId(Long dossierId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering totalThirdPartyByDossierId {} and vckey {}", dossierId, lawfirmToken.getVcKey());

        // sum of all invoice paid (honoraire) per dossier
        BigDecimal sumTiers = tFraisRepository.sumAllTiersByVcKey(dossierId, lawfirmToken.getVcKey());

        ComptaDTO comptaDTO = new ComptaDTO();
        comptaDTO.setMontant(sumTiers);
        log.debug("Total tiers {} found in the vckey {} and dossierId {}", sumTiers, lawfirmToken.getVcKey(), dossierId);

        return comptaDTO;
    }

    @Override
    public void deactivateCompta(Long fraisId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Entering deactivateCompta with fraisId {}", fraisId);
        log.info("deactivateCompta vckey {} ", lawfirmToken.getVcKey());

        Optional<TFrais> tFraisOptional = tFraisRepository.findByIdFraisAndVcKey(fraisId, lawfirmToken.getVcKey());

        if (tFraisOptional.isPresent()) {
            tFraisOptional.get().setIsDeleted(true);
            tFraisRepository.save(tFraisOptional.get());
        } else {
            log.warn("Compta with fraisId {} already deactivated", fraisId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Compta already deactivated");
        }
    }

    private void commonRuleToSave(ComptaDTO comptaDTO) {
        if (comptaDTO == null) {
            log.warn("Compta is not filled in");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Compta is not filled in");
        }

        Optional<TGrid> gridOptional = tGridRepository.findById(comptaDTO.getGridId());
        if (gridOptional.isEmpty()) {
            log.warn("Grid is not found {} ", comptaDTO.getGridId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grid is not found");
        }

        Optional<RefCompte> refCompte = refCompteRepository.findById(comptaDTO.getIdCompte());
        if (refCompte.isEmpty()) {
            log.warn("Account is not found {} ", comptaDTO.getIdCompte());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account is not found");
        }

        if (comptaDTO.getIdDoss() != null) {
            Optional<TDossiers> tDossiers = dossierRepository.findById(comptaDTO.getIdDoss());
            if (tDossiers.isEmpty()) {
                log.warn("Dossier is not found {} ", comptaDTO.getIdDoss());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier is not found");
            }
        }

        if (comptaDTO.getIdFacture() != null) {
            Optional<TFactures> tFactures = tFacturesRepository.findById(comptaDTO.getIdFacture());
            if (tFactures.isEmpty()) {
                log.warn("Invoice is not found {} ", comptaDTO.getIdFacture());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice is not found");
            }
        }

        if (comptaDTO.getIdUser() != null) {
            Optional<TClients> tClients = clientRepository.findById(comptaDTO.getIdUser());
            if (tClients.isEmpty()) {
                log.warn("CLient is not found {} ", comptaDTO.getIdUser());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client is not found");
            }
        }

        Optional<RefPoste> refPoste = refPosteRepository.findById(comptaDTO.getIdPost());
        if (refPoste.isEmpty()) {
            log.warn("Post is not found {} ", comptaDTO.getIdPost());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post is not found");
        }

        Optional<EnumRefTransaction> enumRefTransaction = Optional.ofNullable(EnumRefTransaction.fromId(comptaDTO.getTransactionTypeItem().value));
        if (enumRefTransaction.isEmpty()) {
            log.warn("Transaction is not found {} ", comptaDTO.getIdTransaction());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction is not found");
        }

        Optional<EnumTType> enumTType = Optional.ofNullable(EnumTType.fromId(comptaDTO.getIdType()));
        if (enumTType.isEmpty()) {
            log.warn("Type is not found {} ", comptaDTO.getIdType());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Type is not found");
        }

    }

    @Override
    public ComptaDTO getDefaultCompta(Long userId, String vcKey, String language) {

        ComptaDTO compta = new ComptaDTO();
        compta.setVcKey(vcKey);
        // must be an enum
        compta.setGridId(3);
        List<ItemDto> refCompte = refCompteRepository.findAllOrderBy(vcKey);

        if (refCompte != null && !refCompte.isEmpty()) {
            compta.setIdCompte(refCompte.get(0).value);

        }
        compta.setMontant(BigDecimal.ZERO);
        Optional<RefPoste> refPoste = refPosteRepository.findFirstByVcKeyAndHonoraires(vcKey, true);

        refPoste.ifPresent(poste -> {
            compta.setIdPost(poste.getIdPoste());
            compta.setPoste(new ItemDto(poste.getIdPoste(), poste.getRefPoste()));
        });
        compta.setLanguage(language);

        compta.setTransactionTypeItem(new ItemDto(EnumRefTransaction.VIREMENT.getId(), Utils.getLabel(EnumLanguage.fromshortCode(compta.getLanguage()), EnumRefTransaction.VIREMENT.name(), null)));

        compta.setIdTransaction(EnumRefTransaction.VIREMENT.getId());
        compta.setIdType(EnumTType.ENTREE.getIdType());
        compta.setMontantHt(BigDecimal.ZERO);
        compta.setRatio(BigDecimal.valueOf(100));
        compta.setDateValue(LocalDate.now());

        return compta;
    }


}
