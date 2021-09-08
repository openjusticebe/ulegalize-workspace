
package services.dpa.common.addressbook.external.consult.lawyer.v01.messages;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="officeID" type="{http://common.dpa.services/global/v01/types}OrganisationID"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "officeID"
})
@XmlRootElement(name = "getLawyersForOfficeRequest")
public class GetLawyersForOfficeRequest {

    @XmlElement(required = true)
    protected String officeID;

    /**
     * Gets the value of the officeID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOfficeID() {
        return officeID;
    }

    /**
     * Sets the value of the officeID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOfficeID(String value) {
        this.officeID = value;
    }

}
