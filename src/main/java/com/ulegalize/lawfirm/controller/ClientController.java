package com.ulegalize.lawfirm.controller;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@ApiIgnore
@RequestMapping("/client")
@Slf4j
public class ClientController {

    @Autowired
    ClientService clientService;

    //TODO replace the explicit rest pathparam by a security/jwt param
    @RequestMapping(method = RequestMethod.GET, path = "")
    public List<ContactSummary> getContacts(@RequestParam(required = false) String searchCriteria) throws LawfirmBusinessException {
        log.debug("Entering getContacts()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getContacts(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return clientService.getAllCientByUserId(lawfirmToken.getUserId(), searchCriteria);
    }

}
