package com.ulegalize.lawfirm.service.dpa.impl;

import com.ulegalize.lawfirm.SoapConnector;
import com.ulegalize.lawfirm.service.dpa.EDepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import services.dpa.common.message.box.v01.messages.GetBoxesRequest;
import services.dpa.common.message.box.v01.messages.GetBoxesResponse;

import javax.xml.transform.TransformerException;
import java.io.IOException;

@Slf4j
@Service
public class EDepositServiceImpl implements EDepositService {

    @Value("${dpa.webservice.client.url}")
    private String clientUrl;

    @Autowired
    private SoapConnector soapConnector;

//  @Autowired
//  private DepositService eDepositServiceClient;
//  @Autowired
//  private BoxService boxServiceClient;

    @Override
    public void getCourt() {
//    ObjectFactory objectFactory = new ObjectFactory();
//    com.ulegalize.lawfirm.service.dpa.common.addressbook.external.consult.court.v01.messages.ObjectFactory factory2 = new com.ulegalize.lawfirm.service.dpa.common.addressbook.external.consult.court.v01.messages.ObjectFactory();
//
//    GetCourtsRequest getCourtsRequest = factory2.createGetCourtsRequest();
//    // request
////    GetCourtsRequest getCourtsRequest = new GetCourtsRequest();
//    getCourtsRequest.setLanguagecode("fr");
//    getCourtsRequest.setJurisdiction(1);
//    Ping request = objectFactory.createPing();
////    factory2.create
//    request.setMessage("Vaffa");
////    SoapHeader soapHeader = ((SoapMessage) message).getSoapHeader();
//    JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
//    factory.setServiceClass(BoxService.class);
//    factory.setWsdlURL(urlWsdl);
//    factory.setAddress(addressUrl);
//    BoxPort client = (BoxPort) factory.create();

        GetBoxesRequest getBoxesRequest = new GetBoxesRequest();
        getBoxesRequest.setIncludeInactive(false);
        GetBoxesResponse response = (GetBoxesResponse) soapConnector.callWebService(clientUrl, getBoxesRequest, new WebServiceMessageCallback() {
            @Override
            public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {

            }
        });
        log.info("Calling SOAP service with URL: '{}'", clientUrl);
        log.info("get boxes {}", response.getBoxes());
        log.info("Successfully called SOAP service!");


    }
}
