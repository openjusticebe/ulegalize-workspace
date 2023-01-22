package com.ulegalize.lawfirm.service.validator;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class ContactValidator {

    public void validate(ContactSummary contactSummary) throws ResponseStatusException {
        log.debug("Entering validate() with contactSummary {} ", contactSummary);

        if (contactSummary == null) {
            log.warn("client is not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client is not found");
        }

        if (contactSummary.getTitle() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Title cannot be empty");
        }

        if (contactSummary.getEmail() != null && !contactSummary.getEmail().isEmpty()) {
            if (!Utils.validateEmail(contactSummary.getEmail())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact's email format is not valid ! ");
            }
        }

        if (contactSummary.getType() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client type cannot be empty");
        }

        if (contactSummary.getType() == 1) {

            if ((contactSummary.getFirstname() == null || contactSummary.getFirstname().isEmpty()) && (contactSummary.getLastname() == null || contactSummary.getLastname().isEmpty())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Firstname or lastname cannot be empty");
            }
        }

        if (contactSummary.getType() == 2) {
            if (contactSummary.getCompany() == null || contactSummary.getCompany().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company name cannot be empty");
            }
        }

        if (contactSummary.getJob() != null && contactSummary.getJob().length() > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client Job field cannot be longer than 50 characters");
        }

        log.debug("Leaving validate()");
    }
}
