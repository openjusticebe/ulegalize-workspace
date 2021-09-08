
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for Recipient complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Recipient">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recipientReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliveryChannelList" type="{http://common.dpa.services/message/box/v01/types}DeliveryChannelList"/>
 *       &lt;/sequence>
 *       &lt;attribute name="recipientID" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Recipient", propOrder = {
        "recipientReference",
        "deliveryChannelList"
})
@XmlSeeAlso({
        Court.class,
        Lawyer.class,
        ThirdParty.class
})
public abstract class Recipient {

    protected String recipientReference;
    @XmlElement(required = true)
    protected DeliveryChannelList deliveryChannelList;
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
     * Gets the value of the deliveryChannelList property.
     *
     * @return possible object is
     * {@link DeliveryChannelList }
     */
    public DeliveryChannelList getDeliveryChannelList() {
        return deliveryChannelList;
    }

    /**
     * Sets the value of the deliveryChannelList property.
     *
     * @param value allowed object is
     *              {@link DeliveryChannelList }
     */
    public void setDeliveryChannelList(DeliveryChannelList value) {
        this.deliveryChannelList = value;
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
