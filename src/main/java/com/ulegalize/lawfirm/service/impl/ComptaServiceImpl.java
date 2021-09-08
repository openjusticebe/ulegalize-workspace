package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.dto.ItemBigDecimalDto;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.lawfirm.model.converter.DTOToFraisEntityConverter;
import com.ulegalize.lawfirm.model.converter.EntityToComptaDTOConverter;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.model.enumeration.EnumRefTransaction;
import com.ulegalize.lawfirm.model.enumeration.EnumTType;
import com.ulegalize.lawfirm.repository.*;
import com.ulegalize.lawfirm.service.ComptaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

    public ComptaServiceImpl(TFraisRepository tFraisRepository, TGridRepository tGridRepository, RefCompteRepository refCompteRepository, DossierRepository dossierRepository, TFacturesRepository tFacturesRepository, ClientRepository clientRepository, RefPosteRepository refPosteRepository, TVirtualcabVatRepository tVirtualcabVatRepository, EntityToComptaDTOConverter entityToComptaDTOConverter, DTOToFraisEntityConverter dtoToFraisEntityConverter) {
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
    }

    @Override
    public ComptaDTO getComptaById(Long fraisId, String vcKey) {
        Optional<TFrais> fraisOptional = tFraisRepository.findByIdFraisAndVcKey(fraisId, vcKey);

        if (fraisOptional.isPresent()) {
            // todo convertor

            return entityToComptaDTOConverter.apply(fraisOptional.get());
        }
        return null;
    }

    @Override
    public List<ItemDto> getGridList() {
        return tGridRepository.findAllList();
    }

    @Override
    public List<ComptaDTO> getAllComptaByDossierId(int limit, int offset, Long dossierId, String vcKey,
                                                   Boolean isDebours, Boolean isFraiCollaboration, Boolean honoraire, Boolean tiers) {
        log.debug("Get all Compta with user {} limit {} and offset {} and dossierId {}", vcKey, limit, offset, dossierId);

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idPoste");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "idFrais");
        Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(order, order2));
        List<TFrais> allCompta;

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


        return entityToComptaDTOConverter.convertToList(allCompta);
    }

    @Override
    public List<ComptaDTO> getAllCompta(int limit, int offset, String vcKey) {
        log.debug("Get all Compta with user {} limit {} and offset {}", vcKey, limit, offset);

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idPoste");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "idFrais");
        Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(order, order2));
        List<TFrais> allCompta = tFraisRepository.findAllWithPagination(vcKey, pageable);

        return entityToComptaDTOConverter.convertToList(allCompta);
    }

    @Override
    public Long countAllComptaByVcKey(Long dossierId, String vcKey, Boolean isDebours, Boolean isFraiCollaboration, Boolean honoraire, Boolean tiers) {
        log.debug("countAllComptaByVcKey vckey {}", vcKey);

        if (isDebours != null && isDebours) {
            return tFraisRepository.countAllByIdDossAndVcKeyAndDebours(dossierId, vcKey);

        } else if (isFraiCollaboration != null && isFraiCollaboration) {
            return tFraisRepository.countAllByIdDossAndVcKeyAndFraisCollaboration(dossierId, vcKey);

        } else if (honoraire != null && honoraire) {
            return tFraisRepository.countAllByIdDossAndVcKeyAndHonoraire(dossierId, vcKey);

        } else if (tiers != null && tiers) {
            return tFraisRepository.countAllByIdDossAndVcKeyAndTiers(dossierId, vcKey);

        } else {
            return tFraisRepository.countAllByIdDossAndVcKey(dossierId, vcKey);
        }

    }

    @Override
    public Long countAllCompta(String vcKey) {
        return tFraisRepository.countAllByVcKey(vcKey);
    }

    @Override
    public ComptaDTO updateCompta(ComptaDTO comptaDTO, String vcKey) {
        commonRuleToSave(comptaDTO);

        if (comptaDTO.getId() == null) {
            log.warn("Compta is not filled in {}", comptaDTO.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Compta is not filled in");
        }
        Optional<TFrais> tFraisOptional = tFraisRepository.findByIdFraisAndVcKey(comptaDTO.getId(), vcKey);
        if (!tFraisOptional.isPresent()) {
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

    private void commonRuleToSave(ComptaDTO comptaDTO) {
        if (comptaDTO == null) {
            log.warn("Compta is not filled in");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Compta is not filled in");
        }

        Optional<TGrid> gridOptional = tGridRepository.findById(comptaDTO.getGridId());
        if (!gridOptional.isPresent()) {
            log.warn("Grid is not found {} ", comptaDTO.getGridId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grid is not found");
        }

        Optional<RefCompte> refCompte = refCompteRepository.findById(comptaDTO.getIdCompte());
        if (!refCompte.isPresent()) {
            log.warn("Account is not found {} ", comptaDTO.getIdCompte());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account is not found");
        }

        if (comptaDTO.getIdDoss() != null) {
            Optional<TDossiers> tDossiers = dossierRepository.findById(comptaDTO.getIdDoss());
            if (!tDossiers.isPresent()) {
                log.warn("Dossier is not found {} ", comptaDTO.getIdDoss());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier is not found");
            }
        }

        if (comptaDTO.getIdFacture() != null) {
            Optional<TFactures> tFactures = tFacturesRepository.findById(comptaDTO.getIdFacture());
            if (!tFactures.isPresent()) {
                log.warn("Invoice is not found {} ", comptaDTO.getIdFacture());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice is not found");
            }
        }

        if (comptaDTO.getIdUser() != null) {
            Optional<TClients> tClients = clientRepository.findById(comptaDTO.getIdUser());
            if (!tClients.isPresent()) {
                log.warn("CLient is not found {} ", comptaDTO.getIdUser());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client is not found");
            }
        }

        Optional<RefPoste> refPoste = refPosteRepository.findById(comptaDTO.getIdPost());
        if (!refPoste.isPresent()) {
            log.warn("Post is not found {} ", comptaDTO.getIdPost());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post is not found");
        }

        Optional<EnumRefTransaction> enumRefTransaction = Optional.ofNullable(EnumRefTransaction.fromId(comptaDTO.getIdTransaction()));
        if (!enumRefTransaction.isPresent()) {
            log.warn("Transaction is not found {} ", comptaDTO.getIdTransaction());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction is not found");
        }

        Optional<EnumTType> enumTType = Optional.ofNullable(EnumTType.fromId(comptaDTO.getIdType()));
        if (!enumTType.isPresent()) {
            log.warn("Type is not found {} ", comptaDTO.getIdType());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Type is not found");
        }

    }

    @Override
    public ComptaDTO getDefaultCompta(Long userId, String vcKey) {

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
        compta.setIdTransaction(EnumRefTransaction.VIREMENT.getId());
        compta.setIdType(EnumTType.ENTREE.getIdType());
        compta.setMontantHt(BigDecimal.ZERO);
        compta.setRatio(BigDecimal.valueOf(100));
        compta.setDateValue(LocalDate.now());

        return compta;
    }
}
