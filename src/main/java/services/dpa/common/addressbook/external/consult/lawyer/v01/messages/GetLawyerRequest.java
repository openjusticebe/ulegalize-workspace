
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
 *         &lt;element name="lawyerID" type="{http://common.dpa.services/global/v01/types}PersonID"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "lawyerID"
})
@XmlRootElement(name = "getLawyerRequest")
public class GetLawyerRequest {

    @XmlElement(required = true)
    protected String lawyerID;

    /**
     * Gets the value of the lawyerID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLawyerID() {
        return lawyerID;
    }

    /**
     * Sets the value of the lawyerID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLawyerID(String value) {
        this.lawyerID = value;
    }

}
