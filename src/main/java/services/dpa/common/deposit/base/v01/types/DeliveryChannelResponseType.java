
package services.dpa.common.deposit.base.v01.types;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for DeliveryChannelResponseType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DeliveryChannelResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *       &lt;attribute name="deliveryChannelID" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeliveryChannelResponseType", propOrder = {
        "price"
})
@XmlSeeAlso({
        services.dpa.common.deposit.v01.types.DeliveryChannelResponseType.class,
        services.dpa.common.deposit.price.v01.types.DeliveryChannelResponseType.class
})
public abstract class DeliveryChannelResponseType {

    protected double price;
    @XmlAttribute(name = "deliveryChannelID", required = true)
    protected int deliveryChannelID;

    /**
     * Gets the value of the price property.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     */
    public void setPrice(double value) {
        this.price = value;
    }

    /**
     * Gets the value of the deliveryChannelID property.
     */
    public int getDeliveryChannelID() {
        return deliveryChannelID;
    }

    /**
     * Sets the value of the deliveryChannelID property.
     */
    public void setDeliveryChannelID(int value) {
        this.deliveryChannelID = value;
    }

}
