package com.ulegalize.lawfirm.controller;

import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawyerDuty;
import com.ulegalize.lawfirm.model.LawyerDutyRequest;
import com.ulegalize.lawfirm.model.converter.EntityToUserConverter;
import com.ulegalize.lawfirm.service.LawyerService;
import com.ulegalize.lawfirm.service.v2.CalendarV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/lawyers")
@Slf4j
public class LawyerPublicController {
    @Autowired
    CalendarV2Service calendarV2Service;
    @Autowired
    private LawyerService lawyerService;

    @Autowired
    EntityToUserConverter entityToUserConverter;

    @RequestMapping(method = RequestMethod.GET, path = "")
    public List<LawyerDTO> getFilterLawyer(@RequestParam(required = false) String search, @RequestParam(required = false) String name, @RequestParam(required = false, defaultValue = "") String pref) throws LawfirmBusinessException {
        log.debug("getFilterLawyer(search: {}, name {}, pref {}", search, name, pref);

        return lawyerService.getFilterLawyer(search, name, pref);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/{aliasPublic}")
    public LawyerDTO getLawyer(@PathVariable String aliasPublic) throws LawfirmBusinessException {
        log.debug("getLawyer(lawyer alias Public: {}", aliasPublic);

        return lawyerService.getPublicLawyer(aliasPublic);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/{lawyerAlias}/appointment")
    public LawyerDuty newLawyerAppointment(@PathVariable String lawyerAlias, @RequestBody LawyerDutyRequest appointment) throws LawfirmBusinessException {
        log.debug("newLawyerAppointment( lawyerAlias: {}", lawyerAlias);

        return calendarV2Service.newLawyerAppointment(lawyerAlias, appointment);
    }
}
