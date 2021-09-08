
package services.dpa.common.addressbook.external.consult.court.v01.messages;

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
 *         &lt;element name="court" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "court"
})
@XmlRootElement(name = "CourtNotFoundFault")
public class CourtNotFoundFault
        extends GeneralFault {

    @XmlElement(required = true)
    protected String court;

    /**
     * Gets the value of the court property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCourt() {
        return court;
    }

    /**
     * Sets the value of the court property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCourt(String value) {
        this.court = value;
    }

}
