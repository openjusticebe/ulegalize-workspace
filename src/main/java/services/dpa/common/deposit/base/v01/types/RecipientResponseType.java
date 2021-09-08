
package services.dpa.common.deposit.base.v01.types;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for RecipientResponseType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RecipientResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deliveryChannels" type="{http://common.dpa.services/deposit/base/v01/types}DeliveryChannelsResponseType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="recipientID" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecipientResponseType", propOrder = {
        "deliveryChannels"
})
public class RecipientResponseType {

    @XmlElement(required = true)
    protected DeliveryChannelsResponseType deliveryChannels;
    @XmlAttribute(name = "recipientID", required = true)
    protected int recipientID;

    /**
     * Gets the value of the deliveryChannels property.
     *
     * @return possible object is
     * {@link DeliveryChannelsResponseType }
     */
    public DeliveryChannelsResponseType getDeliveryChannels() {
        return deliveryChannels;
    }

    /**
     * Sets the value of the deliveryChannels property.
     *
     * @param value allowed object is
     *              {@link DeliveryChannelsResponseType }
     */
    public void setDeliveryChannels(DeliveryChannelsResponseType value) {
        this.deliveryChannels = value;
    }

    /**
     * Gets the value of the recipientID property.
     */
    public int getRecipientID() {
        return recipientID;
    }

    /**
     * Sets the value of the recipientID property.
     */
    public void setRecipientID(int value) {
        this.recipientID = value;
    }

}
