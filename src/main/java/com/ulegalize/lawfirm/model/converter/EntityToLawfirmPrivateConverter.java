package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.utils.SuperConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class EntityToLawfirmPrivateConverter implements SuperConverter<LawfirmEntity, LawfirmDTO> {

    @Autowired
    EntityToUserConverter entityToUserConverter;

    @Override
    public LawfirmDTO apply(LawfirmEntity entity) {

        LawfirmDTO lawfirmDTO = new LawfirmDTO();
        lawfirmDTO.setAlias(entity.getAlias());
        lawfirmDTO.setVckey(entity.getVckey());
        lawfirmDTO.setName(entity.getName());

        if (entity.getLawfirmWebsite() != null) {
            lawfirmDTO.setTitle(entity.getLawfirmWebsite().getTitle());
            lawfirmDTO.setIntro(entity.getLawfirmWebsite().getIntro());
            lawfirmDTO.setPhilosophy(entity.getLawfirmWebsite().getPhilosophy() != null ? entity.getLawfirmWebsite().getPhilosophy().replaceAll("\n", "<br/>") : "");
            lawfirmDTO.setAbout(entity.getLawfirmWebsite().getAbout() != null ? entity.getLawfirmWebsite().getAbout().replaceAll("\n", "<br/>") : "");
            lawfirmDTO.setAcceptAppointments(entity.getLawfirmWebsite().isAcceptAppointment());
        }
        lawfirmDTO.setLogo(entity.getLogo());

        lawfirmDTO.setStreet(entity.getStreet());
        lawfirmDTO.setCity(entity.getCity());
        lawfirmDTO.setCountryCode(entity.getCountryCode());
        lawfirmDTO.setEmail(entity.getEmail());
        lawfirmDTO.setPhoneNumber(entity.getPhoneNumber());
        lawfirmDTO.setPostalCode(entity.getPostalCode());
        lawfirmDTO.setDriveType(entity.getDriveType());
        lawfirmDTO.setIsNotification(entity.getNotification());

        lawfirmDTO.setLawyers(new ArrayList<LawyerDTO>());

        entity.getLawfirmUsers().stream().forEach(user -> {
            LawyerDTO l = entityToUserConverter.apply(user.getUser(), false);

            lawfirmDTO.getLawyers().add(l);
        });

        return lawfirmDTO;
    }
}
