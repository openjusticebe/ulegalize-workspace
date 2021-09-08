
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Fax complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Fax">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/message/box/v01/types}DeliveryChannel">
 *       &lt;sequence>
 *         &lt;element name="ToFaxNr" type="{http://common.dpa.services/global/v01/types}PhoneNumber"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Fax", propOrder = {
        "toFaxNr"
})
public class Fax
        extends DeliveryChannel {

    @XmlElement(name = "ToFaxNr", required = true)
    protected String toFaxNr;

    /**
     * Gets the value of the toFaxNr property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getToFaxNr() {
        return toFaxNr;
    }

    /**
     * Sets the value of the toFaxNr property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setToFaxNr(String value) {
        this.toFaxNr = value;
    }

}
