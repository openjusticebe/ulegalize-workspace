package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.model.enumeration.EnumMatiere;
import com.ulegalize.lawfirm.model.enumeration.EnumMatiereRubrique;
import com.ulegalize.lawfirm.repository.*;
import com.ulegalize.lawfirm.rest.DriveFactory;
import com.ulegalize.lawfirm.rest.v2.DriveApi;
import com.ulegalize.lawfirm.service.SearchService;
import com.ulegalize.lawfirm.utils.DriveUtils;
import com.ulegalize.utils.ClientsUtils;
import com.ulegalize.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class SearchServiceImpl implements SearchService {
    private final LawfirmUserRepository lawfirmUserRepository;
    private final TUsersRepository usersRepository;
    private final ClientRepository clientRepository;
    private final TLanguesRepository languesRepository;
    private final RefCompteRepository refCompteRepository;
    private final RefPosteRepository refPosteRepository;
    private final TTimesheetTypeRepository timesheetTypeRepository;
    private final TVirtualcabVatRepository tVirtualcabVatRepository;
    private final TDebourTypeRepository tDebourTypeRepository;
    private final TFactureEcheanceRepository tFactureEcheanceRepository;
    private final VatCountryRepository vatCountryRepository;
    private final DriveFactory driveFactory;

    public SearchServiceImpl(LawfirmUserRepository lawfirmUserRepository, TUsersRepository usersRepository, ClientRepository clientRepository, TLanguesRepository languesRepository,
                             RefCompteRepository refCompteRepository, RefPosteRepository refPosteRepository,
                             TTimesheetTypeRepository timesheetTypeRepository, TVirtualcabVatRepository tVirtualcabVatRepository,
                             TDebourTypeRepository tDebourTypeRepository,
                             TFactureEcheanceRepository tFactureEcheanceRepository,
                             VatCountryRepository vatCountryRepository, DriveFactory driveFactory) {
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.usersRepository = usersRepository;
        this.clientRepository = clientRepository;
        this.languesRepository = languesRepository;
        this.refCompteRepository = refCompteRepository;
        this.refPosteRepository = refPosteRepository;
        this.timesheetTypeRepository = timesheetTypeRepository;
        this.tVirtualcabVatRepository = tVirtualcabVatRepository;
        this.tDebourTypeRepository = tDebourTypeRepository;
        this.tFactureEcheanceRepository = tFactureEcheanceRepository;
        this.vatCountryRepository = vatCountryRepository;
        this.driveFactory = driveFactory;
    }

    @Override
    public List<ItemLongDto> getUserResponsableByVcKey(String vcKey) {
        log.info("Entering getUserResponsableByVcKey vckey {}", vcKey);

        List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findLawfirmUsersByVcKey(vcKey);

        return lawfirmUsersList.stream()
                .map(lawfirmUsers -> {
                    log.debug("Lawfirm user is active {}", lawfirmUsers.isActive());
                    log.debug("Lawfirm user id role {}", lawfirmUsers.getIdRole());
                    if (lawfirmUsers.isActive()
                            && lawfirmUsers.getIdRole().equals(EnumRole.AVOCAT)) {
                        return new ItemLongDto(lawfirmUsers.getUser().getId(), lawfirmUsers.getUser().getEmail());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemLongDto> getMatieres(String dossierType, String language) {
        log.info("Entering getMatieres, dossierType :{}, language :{}", dossierType, language);

        EnumLanguage enumLanguage = EnumLanguage.FR;

        if (language != null) {
            enumLanguage = EnumLanguage.fromshortCode(language);
        }
        EnumLanguage finalEnumLanguage = enumLanguage;

        EnumDossierType enumDossierType = EnumDossierType.fromdossType(dossierType);

        // Check if dossier type is not null and enumDossiertype is mediation
        if (dossierType != null && enumDossierType.equals(EnumDossierType.MD)) {
            return Arrays.stream(EnumMatiereRubrique.values())
                    .map(enumMatiereRubrique -> {
                        EnumMatiere enumMatiere = EnumMatiere.MEDIATION;

                        if (enumMatiereRubrique.getMatiereId().equals(enumMatiere.getId())) {
                            return enumMatiereConcat(enumMatiereRubrique, enumMatiere, finalEnumLanguage);
                        } else {
                            return null;
                        }

                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        // If dossierType is null or enumDossierType is not mediation return all values of EnumMatiereRubrique
        return Arrays.stream(EnumMatiereRubrique.values())
                .map(enumMatiereRubrique -> {
                    EnumMatiere enumMatiere = EnumMatiere.fromId(enumMatiereRubrique.getMatiereId());
                    return enumMatiereConcat(enumMatiereRubrique, enumMatiere, finalEnumLanguage);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    private ItemLongDto enumMatiereConcat(EnumMatiereRubrique enumMatiereRubrique, EnumMatiere enumMatiere, EnumLanguage finalEnumLanguage) {
        return new ItemLongDto(enumMatiereRubrique.getId(), Utils.getLabel(finalEnumLanguage,
                enumMatiere.getDescriptionFr().concat(" - ").concat(enumMatiereRubrique.getDescriptionFr()),
                enumMatiere.getDescriptionEn().concat(" - ").concat(enumMatiereRubrique.getDescriptionEn()),
                enumMatiere.getDescriptionNl().concat(" - ").concat(enumMatiereRubrique.getDescriptionNl()),
                enumMatiere.getDescriptionDe().concat(" - ").concat(enumMatiereRubrique.getDescriptionDe())));
    }

    @Override
    public List<ItemStringDto> getLanguages() {
        log.info("Entering getLanguages");

        return languesRepository.findAllOrderByLgDesc();
    }

    @Override
    public List<ItemStringDto> getCountries() {
        log.info("Entering getCountries");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> countryCodes = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA3);
        List<ItemStringDto> countries = new ArrayList<>();

        for (String countryCode : countryCodes) {

            Locale obj = new Locale(lawfirmToken.getLanguage(), countryCode);

            ItemStringDto itemStringDto = new ItemStringDto(obj.getCountry(), obj.getDisplayCountry());
            countries.add(itemStringDto);
        }

        return countries;
    }

    @Override
    public List<ItemStringDto> getAlpha2Countries() {
        log.info("Entering getAlpha2Countries");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ItemStringDto> countries = new ArrayList<>();

        for (String countryCode : Locale.getISOCountries()) {

            Locale obj = new Locale(lawfirmToken.getLanguage(), countryCode);

            ItemStringDto itemStringDto = new ItemStringDto(obj.getCountry(), obj.getDisplayCountry());
            countries.add(itemStringDto);
        }

        return countries;
    }

    @Override
    public List<ItemDto> getRefCompte(String vcKey) {
        log.info("Entering getRefCompte");

        return refCompteRepository.findAllOrderBy(vcKey);
    }

    @Override
    public List<ItemDto> getRefCompteByAccountType(String vcKey, EnumAccountType accountType) {
        log.info("Entering getRefCompte");

        return refCompteRepository.findDTOByIdAndAccountTypeId(vcKey, accountType);
    }

    @Override
    public List<ItemDto> getPostes(String vcKey) {
        return refPosteRepository.findAllByVcKeyAndArchived(vcKey, false);
    }

    @Override
    public List<ItemDto> getPostesDebours(String vcKey) {
        return refPosteRepository.findAllDeboursByVcKeyAndArchived(vcKey, false);
    }

    @Override
    public List<ItemDto> getPostesFraisCollaboration(String vcKey) {
        return refPosteRepository.findAllFraisCollaByVcKeyAndArchived(vcKey, false);
    }

    @Override
    public List<ItemDto> getPostesHonoraire(String vcKey) {
        return refPosteRepository.findAllHonoraireByVcKeyAndArchived(vcKey, false);
    }

    @Override
    public List<ItemDto> getTimesheetTypes(String vcKey) {
        return timesheetTypeRepository.findAllByVcKeyAndArchived(vcKey, false);
    }

    @Override
    public List<ItemLongDto> getFacturesTypes(String vcKey, Boolean isCreated, String language) {
        EnumLanguage enumLanguage = EnumLanguage.FR;
        if (language != null) {
            enumLanguage = EnumLanguage.fromshortCode(language);
        }
        EnumLanguage finalEnumLanguage = enumLanguage;
        return Arrays.stream(EnumFactureType.values())
                .map(enumFactureType -> {
                    // don't show invoice sell into the list when it's in create state
                    if (isCreated != null && isCreated && EnumFactureType.SELL.equals(enumFactureType)) {
                        return null;
                    }
                    return new ItemLongDto(enumFactureType.getId(), Utils.getLabel(finalEnumLanguage,
                            enumFactureType.getDescriptionFr(),
                            enumFactureType.getDescriptionEn(),
                            enumFactureType.getDescriptionNl(),
                            enumFactureType.getDescriptionDe()));
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getFactureEcheances(String vcKey) {
        List<TFactureEcheance> tFactureEcheances = tFactureEcheanceRepository.findAll();
        return tFactureEcheances.stream()
                .map(factureEcheance -> {
                    return new ItemDto(
                            factureEcheance.getID(),
                            factureEcheance.getDESCRIPTION());
                }).collect(Collectors.toList());
    }

    @Override
    public List<ItemLongDto> getUsers(String searchValue) {

        List<TUsers> tUsers = usersRepository.findBySearchAndIdValidAndValid(searchValue, EnumValid.VERIFIED, true);

        return tUsers.stream()
                .map(users -> {
                    return new ItemLongDto(users.getId(), users.getEmail());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemStringDto> getCientByVcKey(String searchCriteria) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getAllCientByVcKey searchCriteria {} vcKey {} user id {}", searchCriteria, lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        List<TClients> lawfirmClientOptional = clientRepository.findBySearchAndUserIdOrVcKey(Collections.singletonList(lawfirmToken.getVcKey()), lawfirmToken.getUserId(), searchCriteria);

        return lawfirmClientOptional.stream()
                .map(users -> {
                    return new ItemStringDto(users.getF_email(),
                            ClientsUtils.getEmailFullname(users.getF_email(), users.getF_nom(), users.getF_prenom(), users.getF_company()));
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemStringDto> getTemplateModel() {
        log.info("Entering getTemplateModel");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());
        List<ObjectResponseDTO> templates = driveApi.getObjects(lawfirmToken, DriveUtils.TEMPLATE_PATH);
        return templates.stream()
                .map(template -> {
                    // file && extension .doc
                    if (template.getSize() != null
                            && (template.getName().contains(".doc")
                            || template.getName().contains(".docx"))) {
                        String name = template.getName().substring(DriveUtils.TEMPLATE_PATH.length());
                        return new ItemStringDto(template.getName(), name);
                    } else {
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemVatDTO> getDefaultVatsByCountryCode(String countryCode) {
        log.debug("Entering getDefaultVatsByCountryCode by code {}", countryCode);

        List<VatCountry> vatCountryList = vatCountryRepository.findByIdCountryAlpha2(countryCode);

        if (CollectionUtils.isEmpty(vatCountryList)) {
            vatCountryList = vatCountryRepository.findByIdCountryAlpha2("BE");
        }

        return vatCountryList.stream().map(virtual -> {
            return new ItemVatDTO(virtual.getVat(), virtual.getVat().setScale(2, RoundingMode.HALF_UP) + " %", virtual.getIsDefault());
        }).collect(Collectors.toList());
    }

    @Override
    public List<ItemStringDto> getDossierType() {
        log.info("Entering getDossierType");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Arrays.stream(EnumDossierType.values())
                .map(enumDossierType -> {
                    EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());
                    String label = Utils.getLabel(enumLanguage, enumDossierType.getLabelFr(), enumDossierType.getLabelEn(), enumDossierType.getLabelNl(), enumDossierType.getLabelNl());
                    return new ItemStringDto(enumDossierType.getDossType(), label);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemVatDTO> getVats(String vcKey) {
        List<TVirtualcabVat> virtualcabVatList = tVirtualcabVatRepository.findAllByVcKeyAndVATIsNotNull(vcKey);
        return virtualcabVatList.stream().map(virtual -> {
            return new ItemVatDTO(virtual.getVAT(), virtual.getVAT().setScale(2, RoundingMode.HALF_UP) + " %", virtual.getIsDefault());
        }).collect(Collectors.toList());
    }

    @Override
    public List<ItemLongDto> getDeboursType(String vcKey) {
        return tDebourTypeRepository.findAllByVcKeyAndArchived(vcKey, false);

    }

    @Override
    @Cacheable("CalendarEventType")
    public List<ItemEventDto> getCalendarEventType(EnumLanguage enumLanguage) {
        return Arrays.stream(EnumCalendarEventType.values()).map(enumCalendarEventType -> {
                    return new ItemEventDto(enumCalendarEventType.getCode(),
                            Utils.getLabel(enumLanguage,
                                    enumCalendarEventType.getLabelFr(),
                                    enumCalendarEventType.getLabelEn(),
                                    enumCalendarEventType.getLabelNl(),
                                    enumCalendarEventType.getLabelNl()),
                            enumCalendarEventType.getColor());
                })
                .collect(Collectors.toList());

    }

}
