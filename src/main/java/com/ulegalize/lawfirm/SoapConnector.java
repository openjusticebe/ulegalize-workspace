package com.ulegalize.lawfirm;

import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class SoapConnector extends WebServiceGatewaySupport {
    public Object callWebService(String url, Object request, WebServiceMessageCallback webServiceMessageCallback) {
        return getWebServiceTemplate().marshalSendAndReceive(url, request, webServiceMessageCallback);
    }
}
