package com.ulegalize.lawfirm.controller;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.CalendarToEntityConverter;
import com.ulegalize.lawfirm.model.converter.EntityToCalendarConverter;
import com.ulegalize.lawfirm.model.converter.EntityToDossierConverter;
import com.ulegalize.lawfirm.model.converter.EntityToLawfirmPrivateConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.LawfirmWebsiteEntity;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@ApiIgnore
@RequestMapping("/lawfirm")
@Slf4j
public class LawfirmController {
    @Autowired
    EntityToDossierConverter entityToDossierConverter;

    @Autowired
    private LawfirmRepository lawfirmRepository;

    @Autowired
    EntityToLawfirmPrivateConverter entityToLawfirmPrivateConverter;
    @Autowired
    EntityToCalendarConverter entityToCalendarConverter;
    @Autowired
    CalendarToEntityConverter calendarToEntityConverter;


    //TODO replace the explicit rest pathparam by a security/jwt param
    @RequestMapping(method = RequestMethod.GET, path = "/{vckey}")
    public LawfirmDTO getLawfirm(@PathVariable String vckey) throws LawfirmBusinessException {

        log.debug("getLawfirm(vckey: " + vckey + ")");
        LawfirmEntity lawfirm = checkLawfirm(vckey);

        return entityToLawfirmPrivateConverter.apply(lawfirm);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/{vckey}/website")
    public LawfirmWebsiteEntity getLawfirmWebsite(@PathVariable String vckey) throws LawfirmBusinessException {

        log.debug("getLawfirmWebsite(vckey: {})", vckey);
        LawfirmEntity lawfirm = checkLawfirm(vckey);

        if (lawfirm.getLawfirmWebsite() == null) {
            LawfirmWebsiteEntity website = new LawfirmWebsiteEntity();
            website.setVcKey(vckey);
            return website;
        }

            return lawfirm.getLawfirmWebsite();
    }

    @PutMapping(path = "/{vckey}/website")
    public LawfirmWebsiteEntity saveLawfirmWebsite(@PathVariable String vckey, LawfirmWebsiteEntity website) throws LawfirmBusinessException {
        log.debug("saveLawfirmWebsite(vckey: {})", vckey);

        LawfirmEntity lawfirm = checkLawfirm(vckey);

        if (website.getVcKey() == null) {
            throw new LawfirmBusinessException("no virtual cab to save");
        }
        website.setActive(lawfirm.getLawfirmWebsite().isActive());
        website.setUpdDate(new Date());
        website.setUpdUser("ulegalize"); //TODO fix me
        lawfirm.setLawfirmWebsite(website);

        lawfirmRepository.save(lawfirm);

        return website;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{vckey}/website/activate")
    public LawfirmWebsiteEntity activateLawfirmWebsite(@PathVariable String vckey, LawfirmWebsiteEntity website) throws LawfirmBusinessException {
        log.debug("activateLawfirmWebsite(vckey: {})", vckey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LawfirmEntity lawfirm = checkLawfirm(vckey);

        lawfirm.setAlias(vckey.toLowerCase());
        if (lawfirm.getLawfirmWebsite() == null) {
            LawfirmWebsiteEntity lawfirmWebsiteEntity = new LawfirmWebsiteEntity();
            lawfirmWebsiteEntity.setVcKey(vckey.toUpperCase());
            lawfirm.setLawfirmWebsite(lawfirmWebsiteEntity);
        }
        lawfirm.getLawfirmWebsite().setActive(website.isActive());
        lawfirm.getLawfirmWebsite().setUpdUser(lawfirmToken.getUsername());

        lawfirmRepository.save(lawfirm);

        return lawfirm.getLawfirmWebsite();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{vckey}/lawyers")
    public List<LawfirmUsers> getLawfirmLawyers(@PathVariable String vckey) throws LawfirmBusinessException {
        log.debug("getLawfirmLawyers(vckey: {})", vckey);

        LawfirmEntity lawfirm = checkLawfirm(vckey);

        return lawfirm.getLawfirmUsers();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{vckey}/lawyers/{lawyerId}/activate")
    public LawfirmUsers activateLawfirmLawyer(@PathVariable String vckey, @PathVariable Long lawyerId) throws LawfirmBusinessException {
        log.debug("activateLawfirmLawyer(vckey: {}, lawyerId {})", vckey, lawyerId);

        LawfirmEntity lawfirm = checkLawfirm(vckey);
        LawfirmUsers lawfirmUser = lawfirm.getLawfirmUsers().stream()
                .filter(user -> (user.getUser().getId().compareTo(lawyerId) == 0))
                .findAny()
                .orElseThrow(() -> new LawfirmBusinessException("Lawfirm user not found"));
        lawfirmUser.setPublic(!lawfirmUser.isPublic());

        if (lawfirmUser.getUser().getAliasPublic() == null || lawfirmUser.getUser().getAliasPublic().isEmpty()) {
            throw new LawfirmBusinessException("Alias must be filled in. Go to your Profile ");
        }

        lawfirmRepository.save(lawfirm);
        return lawfirmUser;
    }

    private LawfirmEntity checkLawfirm(String vckey) throws LawfirmBusinessException {
        log.debug("checkLawfirm(vckey: {})", vckey);

        Optional<LawfirmEntity> res = lawfirmRepository.findById(vckey.toUpperCase());
        if (!res.isPresent()) {
            throw new LawfirmBusinessException("Lawfirm with key: " + vckey + " not found.");
        }
        return res.get();
    }

}
