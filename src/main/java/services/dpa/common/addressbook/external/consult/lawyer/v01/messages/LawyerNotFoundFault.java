
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
 *         &lt;element name="lawyer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "lawyer"
})
@XmlRootElement(name = "LawyerNotFoundFault")
public class LawyerNotFoundFault
        extends GeneralFault {

    @XmlElement(required = true)
    protected String lawyer;

    /**
     * Gets the value of the lawyer property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLawyer() {
        return lawyer;
    }

    /**
     * Sets the value of the lawyer property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLawyer(String value) {
        this.lawyer = value;
    }

}
