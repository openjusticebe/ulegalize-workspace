package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.dto.LawfirmDriveDTO;
import com.ulegalize.dto.ProfileDTO;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.DefaultLawfirmDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface LawfirmV2Service {
    /**
     * @param userEmail
     * @param clientFrom
     * @param isEmailVerified
     * @return the new temp vc
     * @throws ResponseStatusException
     */
    String createTempVc(String userEmail, String clientFrom, boolean isEmailVerified) throws ResponseStatusException;

    void createSingleVcKey(String userEmail, String tempVcKey, String clientFrom, boolean fullLawfirm, EnumLanguage enumLanguage, String countryCode, boolean verified);

    Boolean deleteTempVcKey(String vcKey);

    ProfileDTO validateVc(String newVcKey, Long userId, String userEmail);

    ProfileDTO updateTempVcKey(ProfileDTO userProfile, DefaultLawfirmDTO defaultLawfirmDTO);

    LawfirmDTO getLawfirmInfoByVcKey(String vckey);

    LawfirmDTO updateLawfirmInfoByVcKey(LawfirmDTO lawfirmDTO);

    List<LawfirmDTO> searchLawfirmInfoByVcKey(String name);

    List<LawfirmDTO> searchLawfirmInfoByVcKeyAndStatusAssociation(String name);

    String uploadImageVirtualcab(byte[] bytes);

    LawfirmDriveDTO updateToken(LawfirmDriveDTO lawfirmDTO);

    String registerUser(String userEmail, String clientFrom, boolean isVerified);

    Long getTotalWorkspace();

    void createDefaultVirtualcabNomenclature(LawfirmEntity lawfirmEntity);

    LawfirmDTO updateLawfirmDriveInfo(LawfirmDTO lawfirmDTO);
}
