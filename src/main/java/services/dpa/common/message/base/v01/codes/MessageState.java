
package services.dpa.common.message.base.v01.codes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageState.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="REGISTERED"/>
 *     &lt;enumeration value="IN_PROCESSING"/>
 *     &lt;enumeration value="READY_FOR_DISPATCHING"/>
 *     &lt;enumeration value="DELIVERY_NOT_CONFIRMED"/>
 *     &lt;enumeration value="DELIVERY_CONFIRMED"/>
 *     &lt;enumeration value="REJECTED"/>
 *     &lt;enumeration value="DISPATCH_FAILURE"/>
 *     &lt;enumeration value="PARTIALLY_DELIVERED"/>
 *     &lt;enumeration value="PARTIALLY_DISPATCHED"/>
 *     &lt;enumeration value="PROCESSING_FAILURE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "MessageState", namespace = "http://common.dpa.services/message/base/v01/codes")
@XmlEnum
public enum MessageState {


    /**
     * when the deposit request is transformed into a message model and the message is first registered in the data store
     */
    REGISTERED,

    /**
     * when the message model is split into different channel model messages
     */
    IN_PROCESSING,

    /**
     * when all channel model messages (transactions) are delivered to the correct connectors for further processing
     */
    READY_FOR_DISPATCHING,

    /**
     * when one or more transactions are in the state DELIVERY_NOT_CONFIRMED
     */
    DELIVERY_NOT_CONFIRMED,

    /**
     * when all transaction are in the state DELIVERY_CONFIRMED
     */
    DELIVERY_CONFIRMED,

    /**
     * when all transactions are in the state REJECTED
     */
    REJECTED,

    /**
     * when all transactions are in the state DISPATCH_FAILURE
     */
    DISPATCH_FAILURE,

    /**
     * when one or more transactions are in the state DELIVERY_CONFIRMED and one or more transactions are in the state REJECTED
     */
    PARTIALLY_DELIVERED,

    /**
     * when there exists a combination of transaction states READY_FOR_DISPATCHING, DELIVERY_NOT_CONFIRMED, DELIVERY_CONFIRMED, DISPATCH_FAILURE
     */
    PARTIALLY_DISPATCHED,

    /**
     * when an exception occurred during the processing of the message
     */
    PROCESSING_FAILURE;

    public static MessageState fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
