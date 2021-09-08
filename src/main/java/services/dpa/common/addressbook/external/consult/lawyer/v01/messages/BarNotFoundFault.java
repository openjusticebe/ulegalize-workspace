
package services.dpa.common.addressbook.external.consult.lawyer.v01.messages;

import services.dpa.common.global.v01.messages.GeneralFault;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/global/v01/messages}GeneralFault">
 *       &lt;sequence>
 *         &lt;element name="bar" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "bar"
})
@XmlRootElement(name = "BarNotFoundFault")
public class BarNotFoundFault
        extends GeneralFault {

    @XmlElement(required = true)
    protected String bar;

    /**
     * Gets the value of the bar property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBar() {
        return bar;
    }

    /**
     * Sets the value of the bar property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBar(String value) {
        this.bar = value;
    }

}
