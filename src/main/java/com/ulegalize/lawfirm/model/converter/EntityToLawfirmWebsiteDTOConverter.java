package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.LawfirmWebsiteDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmWebsiteEntity;
import com.ulegalize.lawfirm.utils.SuperConverter;
import org.springframework.stereotype.Component;

@Component
public class EntityToLawfirmWebsiteDTOConverter implements SuperConverter<LawfirmWebsiteEntity, LawfirmWebsiteDTO> {

    @Override
    public LawfirmWebsiteDTO apply(LawfirmWebsiteEntity lawfirmWebsiteEntity) {

        LawfirmWebsiteDTO lawfirmWebSiteDTO = new LawfirmWebsiteDTO();

        lawfirmWebSiteDTO.setVckey(lawfirmWebsiteEntity.getVcKey());
        lawfirmWebSiteDTO.setTitle(lawfirmWebsiteEntity.getTitle());
        lawfirmWebSiteDTO.setIntro(lawfirmWebsiteEntity.getIntro());
        lawfirmWebSiteDTO.setPhilosophy(lawfirmWebsiteEntity.getPhilosophy());
        lawfirmWebSiteDTO.setAbout(lawfirmWebsiteEntity.getAbout());
        lawfirmWebSiteDTO.setAcceptAppointments(lawfirmWebsiteEntity.isAcceptAppointment());
        lawfirmWebSiteDTO.setActive(lawfirmWebsiteEntity.isActive());

        return lawfirmWebSiteDTO;
    }

}
