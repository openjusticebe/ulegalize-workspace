
package services.dpa.common.deposit.consult.v01.messages;

import services.dpa.common.deposit.consult.v01.types.Lawyer;

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
 *         &lt;element name="lawyer" type="{http://common.dpa.services/deposit/consult/v01/types}Lawyer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "lawyer"
})
@XmlRootElement(name = "getLawyerChannelInfoResponse")
public class GetLawyerChannelInfoResponse {

    @XmlElement(required = true)
    protected Lawyer lawyer;

    /**
     * Gets the value of the lawyer property.
     *
     * @return possible object is
     * {@link Lawyer }
     */
    public Lawyer getLawyer() {
        return lawyer;
    }

    /**
     * Sets the value of the lawyer property.
     *
     * @param value allowed object is
     *              {@link Lawyer }
     */
    public void setLawyer(Lawyer value) {
        this.lawyer = value;
    }

}
