package com.ulegalize.lawfirm.controller;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawyerDuty;
import com.ulegalize.lawfirm.model.converter.EntityToLawfirmPublicConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@ApiIgnore
@RequestMapping("/public/lawfirms")
@Slf4j
public class LawfirmPublicController {
	@Autowired
	private LawfirmRepository lawfirmRepository;
	@Autowired
	private EntityToLawfirmPublicConverter entityToLawfirmPublicConverter;


	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.GET, path = "/{alias}")
	public LawfirmDTO getLawfirm(@PathVariable String alias) throws LawfirmBusinessException {
		log.debug("getLawfirm(alias: {}", alias);
		if (alias == null) {
			throw new LawfirmBusinessException("alias is empty ");
		}
		return buildLawfirm(alias);

	}

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

	@RequestMapping(method = RequestMethod.GET, path="/{alias}/lawyers/{lawyerAlias}")
	public LawyerDTO getLawyer(@PathVariable String alias, @PathVariable String lawyerAlias) throws LawfirmBusinessException {
		log.debug("getLawfirm(alias: {}, lawyerAlias: {}", alias, lawyerAlias);
		LawfirmDTO lawfirmDTO = buildLawfirm(alias);

		String finalLawyerAlias = lawyerAlias.replaceAll(" ", "-").toLowerCase();
		log.debug("lawyerAlias with replace {}", finalLawyerAlias);
		Optional<LawyerDTO> opt = lawfirmDTO.getLawyers().stream().filter(l -> l.getFullName().replaceAll(" ", "-").toLowerCase().equals(finalLawyerAlias)).findFirst();

		if (opt.isPresent()) {
			return opt.get();
		}

		throw new LawfirmBusinessException("No lawyer found with alias = " + lawyerAlias);
	}

		@RequestMapping(method = RequestMethod.PUT, path="/{alias}/lawyers/{lawyerAlias}/availabilities")
	public List<LawyerDuty> getLawyerAvailabilities(@PathVariable String alias, @PathVariable String lawyerAlias,
													@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
													@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

			List<LawyerDuty> duties = new ArrayList<>();
			//duties.add(new LawyerDuty(3L, new Date("2019-08-05"), new Date("2019-08-05"), "Something to remember"));
			//duties.add(new LawyerDuty(4L, new Date("2019-08-06"), new Date("2019-08-06"), "Something to remember"));

			return duties;

		}

	private LawfirmDTO buildLawfirm(String lawfirmAlias) throws LawfirmBusinessException {

		LawfirmEntity entity = getActiveLawfirmByAlias(lawfirmAlias);

		return entityToLawfirmPublicConverter.apply(entity, true);
	}

	private LawfirmEntity getActiveLawfirmByAlias(String alias) throws LawfirmBusinessException {
		LawfirmEntity entity = lawfirmRepository.findLawfirmByAlias(alias.toLowerCase());
		if (entity == null) {
			throw new LawfirmBusinessException("Did not find an active lawfirm with alias = " + alias);
		}

		if (entity.getLawfirmWebsite() == null ||
				(entity.getLawfirmWebsite() != null && !entity.getLawfirmWebsite().isActive())) {
			throw new LawfirmBusinessException("Did not find an active website with alias= " + alias);
		}

		log.debug("website public fond");

		return entity;
	}

	private List<LawfirmEntity> getActiveLawfirms(String alias) throws LawfirmBusinessException {
		List<LawfirmEntity> entities = lawfirmRepository.findLawfirmLikeByAlias(alias);
		if (entities == null) {
			throw new LawfirmBusinessException("Did not find an active lawfirm");
		}

		return entities;
	}


	private List<LawyerDTO> getLawyersTemp() {
		List<LawyerDTO> lawyers = new ArrayList<>();


		return lawyers;
	}
}
