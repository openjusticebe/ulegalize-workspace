
package services.dpa.common.message.box.v01.types;

import services.dpa.common.addressbook.base.v01.types.Address;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for Letter complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Letter">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/message/box/v01/types}DeliveryChannel">
 *       &lt;sequence>
 *         &lt;element name="address" type="{http://common.dpa.services/addressbook/base/v01/types}Address"/>
 *       &lt;/sequence>
 *       &lt;attribute name="registered" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="color" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Letter", propOrder = {
        "address"
})
public class Letter
        extends DeliveryChannel {

    @XmlElement(required = true)
    protected Address address;
    @XmlAttribute(name = "registered")
    protected Boolean registered;
    @XmlAttribute(name = "color")
    protected Boolean color;

    /**
     * Gets the value of the address property.
     *
     * @return possible object is
     * {@link Address }
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     *
     * @param value allowed object is
     *              {@link Address }
     */
    public void setAddress(Address value) {
        this.address = value;
    }

    /**
     * Gets the value of the registered property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public boolean isRegistered() {
        if (registered == null) {
            return false;
        } else {
            return registered;
        }
    }

    /**
     * Sets the value of the registered property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setRegistered(Boolean value) {
        this.registered = value;
    }

    /**
     * Gets the value of the color property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public boolean isColor() {
        if (color == null) {
            return false;
        } else {
            return color;
        }
    }

    /**
     * Sets the value of the color property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setColor(Boolean value) {
        this.color = value;
    }

}
