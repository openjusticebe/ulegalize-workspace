
package services.dpa.common.deposit.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DeliveryChannelResponseType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DeliveryChannelResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/deposit/base/v01/types}DeliveryChannelResponseType">
 *       &lt;sequence>
 *         &lt;element name="transactionID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeliveryChannelResponseType", propOrder = {
        "transactionID"
})
public class DeliveryChannelResponseType
        extends services.dpa.common.deposit.base.v01.types.DeliveryChannelResponseType {

    @XmlElement(required = true)
    protected String transactionID;

    /**
     * Gets the value of the transactionID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTransactionID() {
        return transactionID;
    }

    /**
     * Sets the value of the transactionID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTransactionID(String value) {
        this.transactionID = value;
    }

}
