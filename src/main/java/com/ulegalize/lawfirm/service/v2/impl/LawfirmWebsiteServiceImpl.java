package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.LawfirmWebsiteDTO;
import com.ulegalize.lawfirm.model.converter.EntityToLawfirmWebsiteDTOConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmWebsiteEntity;
import com.ulegalize.lawfirm.repository.LawfirmWebsiteRepository;
import com.ulegalize.lawfirm.service.LawfirmWebsiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@Transactional
public class LawfirmWebsiteServiceImpl implements LawfirmWebsiteService {


    private final LawfirmWebsiteRepository lawfirmWebsiteRepository;
    private final EntityToLawfirmWebsiteDTOConverter entityToLawfirmWebsiteDTOConverter;

    public LawfirmWebsiteServiceImpl(LawfirmWebsiteRepository lawfirmWebsiteRepository, EntityToLawfirmWebsiteDTOConverter entityToLawfirmWebsiteDTOConverter) {
        this.lawfirmWebsiteRepository = lawfirmWebsiteRepository;
        this.entityToLawfirmWebsiteDTOConverter = entityToLawfirmWebsiteDTOConverter;
    }

    @Override
    public LawfirmWebsiteDTO getLawfirmWebsites(String vcKey) {

        Optional<LawfirmWebsiteEntity> optionalLawfirmWebsiteEntity = lawfirmWebsiteRepository.findAllByVcKey(vcKey);

        LawfirmWebsiteEntity lawfirmWebsiteEntity;
        if (!optionalLawfirmWebsiteEntity.isPresent()) {
            lawfirmWebsiteEntity = new LawfirmWebsiteEntity();
            lawfirmWebsiteEntity.setVcKey(vcKey);
            lawfirmWebsiteEntity.setActive(false);
            lawfirmWebsiteEntity.setAcceptAppointment(false);
        } else {
            lawfirmWebsiteEntity = optionalLawfirmWebsiteEntity.get();
        }

        return entityToLawfirmWebsiteDTOConverter.apply(lawfirmWebsiteEntity);
    }

    @Override
    public LawfirmWebsiteDTO updateLawfirmWebsite(String vcKey, LawfirmWebsiteDTO lawfirmWebsiteDTO) {
        log.debug("Entering updateLawfirmWebsite with vcKey : {}", vcKey);

        Optional<LawfirmWebsiteEntity> optionalLawfirmWebsiteEntity = lawfirmWebsiteRepository.findAllByVcKey(vcKey);

        AtomicReference<LawfirmWebsiteDTO> lawfirmWebsiteDTO1 = new AtomicReference<>(new LawfirmWebsiteDTO());
        optionalLawfirmWebsiteEntity.ifPresentOrElse(l -> {
            l.getLawfirmEntity().setAlias(l.getLawfirmEntity().getVckey().toLowerCase());
            log.debug("Lawfirm alias is ok : {}", l.getLawfirmEntity().getAlias());

            l.setTitle(lawfirmWebsiteDTO.getTitle());
            l.setIntro(lawfirmWebsiteDTO.getIntro());
            l.setAbout(lawfirmWebsiteDTO.getAbout());
            l.setPhilosophy(lawfirmWebsiteDTO.getPhilosophy());
            l.setActive(lawfirmWebsiteDTO.isActive());
            l.setAcceptAppointment(lawfirmWebsiteDTO.isAcceptAppointments());
            lawfirmWebsiteDTO1.set(entityToLawfirmWebsiteDTOConverter.apply(l));
        }, () -> {
            log.warn("LawfirmWebsite with vcKey {} does not exist !", vcKey);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "LawfirmWebsite does not exist");
        });
        return lawfirmWebsiteDTO1.get();
    }


}
