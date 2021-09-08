
package services.dpa.common.deposit.v01.types;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for RecipientType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RecipientType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recipientReference" type="{http://common.dpa.services/global/v01/types}StringMax64" minOccurs="0"/>
 *         &lt;element name="deliveryChannels" type="{http://common.dpa.services/deposit/v01/types}DeliveryChannelsType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="recipientID" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecipientType", propOrder = {
        "recipientReference",
        "deliveryChannels"
})
@XmlSeeAlso({
        CourtType.class,
        ThirdPartyType.class,
        LawyerType.class
})
public abstract class RecipientType {

    protected String recipientReference;
    @XmlElement(required = true)
    protected DeliveryChannelsType deliveryChannels;
    @XmlAttribute(name = "recipientID", required = true)
    protected int recipientID;

    /**
     * Gets the value of the recipientReference property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRecipientReference() {
        return recipientReference;
    }

    /**
     * Sets the value of the recipientReference property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRecipientReference(String value) {
        this.recipientReference = value;
    }

    /**
     * Gets the value of the deliveryChannels property.
     *
     * @return possible object is
     * {@link DeliveryChannelsType }
     */
    public DeliveryChannelsType getDeliveryChannels() {
        return deliveryChannels;
    }

    /**
     * Sets the value of the deliveryChannels property.
     *
     * @param value allowed object is
     *              {@link DeliveryChannelsType }
     */
    public void setDeliveryChannels(DeliveryChannelsType value) {
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
