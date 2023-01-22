package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.converter.EntityToLawfirmPublicConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v2/public/lawfirms")
@Slf4j
public class LawfirmPublicV2Controller {
    @Autowired
    private LawfirmRepository lawfirmRepository;
    @Autowired
    private EntityToLawfirmPublicConverter entityToLawfirmPublicConverter;

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.GET, path = "")
    public List<LawfirmDTO> getFilterLawfirm(@RequestParam String search) throws LawfirmBusinessException {
        log.debug("getFilterLawfirm(search: {}", search);
        if (search == null) {
            throw new LawfirmBusinessException("search is empty ");
        }

        List<LawfirmDTO> lawfirmDTOS = new ArrayList<>();

        if (search.equalsIgnoreCase("all")) {
            List<LawfirmEntity> lawfirmEntities = lawfirmRepository.findAllByLicenseId();
            lawfirmDTOS = entityToLawfirmPublicConverter.convertToList(lawfirmEntities, false);
        } else {
            List<LawfirmEntity> lawfirmEntities = lawfirmRepository.findLawfirmLikeByAlias(search);
            lawfirmDTOS = entityToLawfirmPublicConverter.convertToList(lawfirmEntities, false);
        }

        return lawfirmDTOS;
    }

}