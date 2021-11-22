package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.enumeration.EnumTType;
import com.ulegalize.lawfirm.service.SearchService;
import com.ulegalize.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@ApiIgnore
@RequestMapping("/v2/search")
@Slf4j
public class SearchV2Controller {

    @Autowired
    SearchService searchService;

    @GetMapping(path = "/users")
    public ResponseEntity<List<ItemLongDto>> getUserResponsableList(@RequestParam(required = false) String vcKeyToSearch,
                                                                    @RequestParam(required = false) String vcKey) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getUserResponsableList( for vckey {}", lawfirmToken.getVcKey());
        List<ItemLongDto> itemLongDtos;
        if (vcKeyToSearch == null || vcKeyToSearch.isEmpty()) {
            itemLongDtos = searchService.getUserResponsableByVcKey(lawfirmToken.getVcKey());
        } else {
            itemLongDtos = searchService.getUserResponsableByVcKey(vcKeyToSearch);
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        if (vcKey != null && vcKey.equalsIgnoreCase(lawfirmToken.getVcKey())) {
            responseBuilder
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS));
        }
        return responseBuilder
                .body(itemLongDtos);
    }

    @GetMapping(path = "/users/full")
    public ResponseEntity<List<ItemLongDto>> getFullUserResponsableList(@RequestParam(required = false) String search) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getFullUserResponsableList( for vckey {} and search {}", lawfirmToken.getVcKey(), search);

        return ResponseEntity
                .ok()
                .body(searchService.getUsers(search));
    }

    @GetMapping(path = "/matieres")
    public ResponseEntity<List<ItemDto>> getMatieres() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getMatieres() for vckey {}", lawfirmToken.getVcKey());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(searchService.getMatieres());
    }

    @GetMapping(path = "/languages")
    public ResponseEntity<List<ItemStringDto>> getLanguages() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getLanguages() for vckey {}", lawfirmToken.getVcKey());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(searchService.getLanguages());
    }

    @GetMapping(path = "/countries")
    public ResponseEntity<List<ItemStringDto>> getCountries() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getCountries() for vckey {}", lawfirmToken.getVcKey());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(searchService.getCountries());
    }

    @GetMapping(path = "/alpha2countries")
    public ResponseEntity<List<ItemStringDto>> getAlpha2Countries() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getAlpha2Countries() for vckey {}", lawfirmToken.getVcKey());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(searchService.getAlpha2Countries());
    }

    @GetMapping(path = "/tiers")
    public ResponseEntity<List<ItemDto>> getRefCompte(@RequestParam(required = false) Boolean honoraires,
                                                      @RequestParam(required = false) Boolean tiers,
                                                      @RequestParam(required = false) Boolean fraisCollaborative,
                                                      @RequestParam(required = false) Boolean debours) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getRefCompte() for vckey {}", lawfirmToken.getVcKey());

        List<ItemDto> itemDtos;

        if ((honoraires != null && honoraires)
                || (fraisCollaborative != null && fraisCollaborative)
                || (debours != null && debours)) {
            itemDtos = searchService.getRefCompteByAccountType(lawfirmToken.getVcKey(), EnumAccountType.PRO_ACCOUNT);
        } else if (tiers != null && tiers) {
            itemDtos = searchService.getRefCompteByAccountType(lawfirmToken.getVcKey(), EnumAccountType.ACCOUNT_TIERS);
        } else {
            itemDtos = searchService.getRefCompte(lawfirmToken.getVcKey());
        }

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(itemDtos);
    }

    @GetMapping(path = "/types")
    public ResponseEntity<List<ItemDto>> getTypes() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getRefCompte() for vckey {}", lawfirmToken.getVcKey());

        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(Arrays.stream(EnumTType.values()).map(enumTType -> {
                            return new ItemDto(enumTType.getIdType(),
                                    Utils.getLabel(enumLanguage,
                                            enumTType.getDescriptionFr(),
                                            enumTType.getDescriptionEn(),
                                            enumTType.getDescriptionNl()));
                        })
                        .collect(Collectors.toList()));
    }

    @GetMapping(path = "/postes")
    public ResponseEntity<List<ItemDto>> getPostes(@RequestParam(required = false) Boolean frais,
                                                   @RequestParam(required = false) Boolean debours,
                                                   @RequestParam(required = false) Boolean honoraires) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getRefCompte() for vckey {}", lawfirmToken.getVcKey());

        List<ItemDto> itemDtos;

        // frais collaboration
        if (frais != null && frais) {
            itemDtos = searchService.getPostesFraisCollaboration(lawfirmToken.getVcKey());

            // frais collaboration
        } else if (debours != null && debours) {
            itemDtos = searchService.getPostesDebours(lawfirmToken.getVcKey());

        } else if (honoraires != null && honoraires) {
            itemDtos = searchService.getPostesHonoraire(lawfirmToken.getVcKey());

        } else {
            itemDtos = searchService.getPostes(lawfirmToken.getVcKey());
        }

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(itemDtos);
    }

    @GetMapping(path = "/timesheetTypes")
    public ResponseEntity<List<ItemDto>> getTimesheetTypes() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getTimesheetTypes() for vckey {}", lawfirmToken.getVcKey());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(searchService.getTimesheetTypes(lawfirmToken.getVcKey()));
    }

    @GetMapping(path = "/facturesTypes")
    public ResponseEntity<List<ItemLongDto>> getFacturesTypes(@RequestParam Boolean isCreated) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getFacturesTypes() for vckey {}", lawfirmToken.getVcKey());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(searchService.getFacturesTypes(lawfirmToken.getVcKey(), isCreated));
    }

    @GetMapping(path = "/deboursTypes")
    public ResponseEntity<List<ItemLongDto>> getDeboursType() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getDeboursType() for vckey {}", lawfirmToken.getVcKey());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(searchService.getDeboursType(lawfirmToken.getVcKey()));
    }

    @GetMapping(path = "/vats")
    public ResponseEntity<List<ItemVatDTO>> getVats() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getVats() for vckey {}", lawfirmToken.getVcKey());

        return ResponseEntity
                .ok()
                .body(searchService.getVats(lawfirmToken.getVcKey()));
    }

    @GetMapping(path = "/vats/country/{countryCode}")
    public ResponseEntity<List<ItemVatDTO>> getDefaultVatsByCountryCode(@PathVariable String countryCode) {
        log.debug("getDefaultVatsByCountryCode() for vckey {}", countryCode);

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(searchService.getDefaultVatsByCountryCode(countryCode));
    }

    @GetMapping(path = "/factureEcheances")
    public ResponseEntity<List<ItemDto>> getFactureEcheances() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getFacturesTypes() for vckey {}", lawfirmToken.getVcKey());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(searchService.getFactureEcheances(lawfirmToken.getVcKey()));
    }

    @GetMapping(path = "/calendarEventType")
    public ResponseEntity<List<ItemEventDto>> getCalendarEventType() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getCalendarEventType() for language {}", lawfirmToken.getLanguage());

        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(Arrays.stream(EnumCalendarEventType.values()).map(enumCalendarEventType -> {
                            return new ItemEventDto(enumCalendarEventType.getCode(),
                                    Utils.getLabel(enumLanguage,
                                            enumCalendarEventType.getLabelFr(),
                                            enumCalendarEventType.getLabelEn(),
                                            enumCalendarEventType.getLabelNl()),
                                    enumCalendarEventType.getColor());
                        })
                        .collect(Collectors.toList()));

    }

    @GetMapping(path = "/currencies")
    public ResponseEntity<List<ItemStringDto>> getCurrencies() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getCurrencies() for language {}", lawfirmToken.getLanguage());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(Arrays.stream(EnumRefCurrency.values()).map(enumRefCurrency -> {
                            return new ItemStringDto(enumRefCurrency.getCode(), enumRefCurrency.getCode());
                        })
                        .collect(Collectors.toList()));
    }

    @GetMapping(path = "/accountType")
    public ResponseEntity<List<ItemDto>> getAccountType() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getCalendarEventType() for language {}", lawfirmToken.getLanguage());

        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(Arrays.stream(EnumAccountType.values()).map(enumAccountType -> {
                            return new ItemDto(enumAccountType.getId(),
                                    Utils.getLabel(enumLanguage,
                                            enumAccountType.getLabelFr(),
                                            enumAccountType.getLabelEn(),
                                            enumAccountType.getLabelNl()));
                        })
                        .collect(Collectors.toList()));
    }

    @GetMapping(path = "/roles")
    public ResponseEntity<List<ItemDto>> getRoles() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getCalendarEventType() for language {}", lawfirmToken.getLanguage());

        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(Arrays.stream(EnumRole.values()).map(enumRole -> {
                            return new ItemDto(enumRole.getIdRole(),
                                    Utils.getLabel(enumLanguage,
                                            enumRole.getLabelFr(),
                                            enumRole.getLabelEn(),
                                            enumRole.getLabelNl()));
                        })
                        .collect(Collectors.toList()));
    }

    @GetMapping(path = "/contextModel")
    public ResponseEntity<List<ItemStringDto>> getContextModel() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("contextModel() for language {}", lawfirmToken.getLanguage());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(Arrays.stream(EnumContextTemplate.values()).map(enumContextTemplate -> {
                            return new ItemStringDto(enumContextTemplate.name(), enumContextTemplate.name());
                        })
                        .collect(Collectors.toList()));

    }

    @GetMapping(path = "/templateModel")
    public ResponseEntity<List<ItemStringDto>> getTemplateModel() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("contextModel() for language {}", lawfirmToken.getLanguage());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(searchService.getTemplateModel());
    }

    @GetMapping(path = "/dossierType")
    public ResponseEntity<List<ItemStringDto>> getDossierType() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("contextModel() for language {}", lawfirmToken.getLanguage());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(searchService.getDossierType());
    }
}
