
package services.dpa.common.deposit.v01.ws;

import jakarta.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 */
@WebFault(name = "LawyerNotFoundFault", targetNamespace = "http://common.dpa.services/addressbook/external/consult/lawyer/v01/messages")
public class LawyerNotFoundFault
        extends Exception {

    /**
     * Java type that goes as soapenv:Fault detail element.
     */
    private final services.dpa.common.addressbook.external.consult.lawyer.v01.messages.LawyerNotFoundFault faultInfo;

    /**
     * @param faultInfo
     * @param message
     */
    public LawyerNotFoundFault(String message, services.dpa.common.addressbook.external.consult.lawyer.v01.messages.LawyerNotFoundFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * @param faultInfo
     * @param cause
     * @param message
     */
    public LawyerNotFoundFault(String message, services.dpa.common.addressbook.external.consult.lawyer.v01.messages.LawyerNotFoundFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * @return returns fault bean: services.dpa.common.addressbook.external.consult.lawyer.v01.messages.LawyerNotFoundFault
     */
    public services.dpa.common.addressbook.external.consult.lawyer.v01.messages.LawyerNotFoundFault getFaultInfo() {
        return faultInfo;
    }

}
