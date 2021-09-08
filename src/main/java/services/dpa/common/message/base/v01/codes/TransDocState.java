
package services.dpa.common.message.base.v01.codes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransDocState.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TransDocState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="REGISTERED"/>
 *     &lt;enumeration value="IN_PROCESSING"/>
 *     &lt;enumeration value="READY_FOR_DISPATCHING"/>
 *     &lt;enumeration value="DELIVERY_NOT_CONFIRMED"/>
 *     &lt;enumeration value="DELIVERY_CONFIRMED"/>
 *     &lt;enumeration value="REJECTED"/>
 *     &lt;enumeration value="DISPATCH_FAILURE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "TransDocState", namespace = "http://common.dpa.services/message/base/v01/codes")
@XmlEnum
public enum TransDocState {


    /**
     * when the deposit request is transformed into a message model and the document is first registered in the data store
     */
    REGISTERED,

    /**
     * when the message model is split into different channel model messages
     */
    IN_PROCESSING,

    /**
     * when the channel model message is delivered to the correct connector for further processing
     */
    READY_FOR_DISPATCHING,

    /**
     * when the document is delivered by the target system but confirmation is yet to be given by the target system
     */
    DELIVERY_NOT_CONFIRMED,

    /**
     * when the target system confirmed that the document was successfully received
     */
    DELIVERY_CONFIRMED,

    /**
     * when the document was rejected by the target system (due to the business rules).
     */
    REJECTED,

    /**
     * when an exception occurred when trying to send the document to the target system (IOException, ...)
     */
    DISPATCH_FAILURE;

    public static TransDocState fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
