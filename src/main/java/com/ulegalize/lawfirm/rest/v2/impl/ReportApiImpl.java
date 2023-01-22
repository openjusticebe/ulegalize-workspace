package com.ulegalize.lawfirm.rest.v2.impl;

import com.ulegalize.lawfirm.exception.RestTemplateResponseErrorHandler;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.rest.v2.ReportApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class ReportApiImpl implements ReportApi {
    @Value("${app.lawfirm-report.url}")
    String REPORT_URL;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final RestTemplate restTemplate;

    public ReportApiImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    }


    @Override
    public ByteArrayResource getInvoice(LawfirmToken lawfirmToken, Long invoiceId) {
        log.info("Entering getInvoice FOR VCKEY : {} and invoice id {}", lawfirmToken.getVcKey(), invoiceId);
        if (!activeProfile.equalsIgnoreCase("integrationtest")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());

            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                    = new HttpEntity<>(body, headers);

            ResponseEntity<byte[]> result = restTemplate.exchange(REPORT_URL + "invoice/" + invoiceId, HttpMethod.GET, requestEntity, byte[].class);

            if (result.getBody() != null) {
                log.debug("Decode the base64 invoice");
                byte[] decode = Base64.getDecoder().decode(result.getBody());
                log.info("Leaving downloadFile and decode the base64 invoice ok");
                return new ByteArrayResource(decode);
            }

        }
        log.info("Leaving getInvoice not ok");
        byte[] emptyArray = new byte[0];
        return new ByteArrayResource(emptyArray);
    }

}
