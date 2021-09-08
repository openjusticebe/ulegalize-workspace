
package services.dpa.common.deposit.base.v01.types;

import services.dpa.common.deposit.price.v01.types.PriceResponseType;
import services.dpa.common.deposit.v01.types.DepositResponseType;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for BaseResponseType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BaseResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="totalPrice" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="basePrice" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="recipients" type="{http://common.dpa.services/deposit/base/v01/types}RecipientsResponseType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseResponseType", propOrder = {
        "totalPrice",
        "basePrice",
        "recipients"
})
@XmlSeeAlso({
        DepositResponseType.class,
        PriceResponseType.class
})
public abstract class BaseResponseType {

    protected double totalPrice;
    protected double basePrice;
    @XmlElement(required = true)
    protected RecipientsResponseType recipients;

    /**
     * Gets the value of the totalPrice property.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the value of the totalPrice property.
     */
    public void setTotalPrice(double value) {
        this.totalPrice = value;
    }

    /**
     * Gets the value of the basePrice property.
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Sets the value of the basePrice property.
     */
    public void setBasePrice(double value) {
        this.basePrice = value;
    }

    /**
     * Gets the value of the recipients property.
     *
     * @return possible object is
     * {@link RecipientsResponseType }
     */
    public RecipientsResponseType getRecipients() {
        return recipients;
    }

    /**
     * Sets the value of the recipients property.
     *
     * @param value allowed object is
     *              {@link RecipientsResponseType }
     */
    public void setRecipients(RecipientsResponseType value) {
        this.recipients = value;
    }

}
