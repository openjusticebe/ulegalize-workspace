package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.EnumAccountType;
import com.ulegalize.enumeration.EnumLanguage;

import java.util.List;

public interface SearchService {
    List<ItemLongDto> getUserResponsableByVcKey(String vcKey);

    List<ItemDto> getMatieres();

    List<ItemStringDto> getLanguages();

    List<ItemStringDto> getCountries();

    List<ItemStringDto> getAlpha2Countries();

    List<ItemDto> getRefCompte(String vcKey);

    List<ItemDto> getRefCompteByAccountType(String vcKey, EnumAccountType enumAccountType);

    List<ItemDto> getPostes(String vcKey);

    List<ItemDto> getPostesDebours(String vcKey);

    List<ItemDto> getPostesFraisCollaboration(String vcKey);

    List<ItemDto> getPostesHonoraire(String vcKey);

    List<ItemDto> getTimesheetTypes(String vcKey);

    List<ItemVatDTO> getVats(String vcKey);

    List<ItemLongDto> getDeboursType(String vcKey);

    List<ItemLongDto> getFacturesTypes(String vcKey, Boolean isCreated);

    List<ItemDto> getFactureEcheances(String vcKey);

    List<ItemLongDto> getUsers(String searchValue);

    public List<ItemStringDto> getCientByVcKey(String searchCriteria);

    List<ItemStringDto> getTemplateModel();

    List<ItemVatDTO> getDefaultVatsByCountryCode(String countryCode);

    List<ItemStringDto> getDossierType();

    List<ItemEventDto> getCalendarEventType(EnumLanguage enumLanguage);
}
