package com.ulegalize.lawfirm.service;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.SoapConnector;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import services.dpa.common.message.box.v01.messages.GetBoxesRequest;
import services.dpa.common.message.box.v01.messages.GetBoxesResponse;

import javax.xml.transform.TransformerException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class EDepositServiceImplTest extends EntityTest {

    @Autowired
    private SoapConnector soapConnector;

    @Value("${dpa.webservice.client.url}")
    private String clientUrl;


    @Test
    @Disabled
    public void test_A_getCourt() {
        GetBoxesRequest getBoxesRequest = new GetBoxesRequest();
        getBoxesRequest.setIncludeInactive(false);
        GetBoxesResponse response = (GetBoxesResponse) soapConnector.callWebService(clientUrl, getBoxesRequest, new WebServiceMessageCallback() {
            @Override
            public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {

            }
        });

        assertNotNull(response.getBoxes());
    }
}
