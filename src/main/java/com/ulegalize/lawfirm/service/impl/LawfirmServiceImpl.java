package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.lawfirm.model.converter.EntityToLawfirmPrivateConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.service.LawfirmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class LawfirmServiceImpl implements LawfirmService {

    private final LawfirmRepository lawfirmRepository;
    private final EntityToLawfirmPrivateConverter entityToLawfirmPrivateConverter;

    public LawfirmServiceImpl(LawfirmRepository lawfirmRepository,
                              EntityToLawfirmPrivateConverter entityToLawfirmPrivateConverter) {
        this.lawfirmRepository = lawfirmRepository;
        this.entityToLawfirmPrivateConverter = entityToLawfirmPrivateConverter;
    }

    @Override
    public List<LawfirmDTO> getLawfirmList(String searchCriteria) {
        log.debug("Entering getLawfirmList");

        String search = searchCriteria != null && !searchCriteria.isEmpty() ? searchCriteria : "%";

        List<LawfirmEntity> lawfirmEntities = lawfirmRepository.findAllByLicenseIdAndTemporaryVc(search);

        return entityToLawfirmPrivateConverter.convertToList(lawfirmEntities);
    }

}
