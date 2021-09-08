
package services.dpa.common.addressbook.external.consult.lawyer.v01.messages;

import services.dpa.common.addressbook.external.consult.lawyer.v01.types.LawyerList;

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
 *         &lt;element name="lawyers" type="{http://common.dpa.services/addressbook/external/consult/lawyer/v01/types}LawyerList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "lawyers"
})
@XmlRootElement(name = "getLawyersForOfficeResponse")
public class GetLawyersForOfficeResponse {

    @XmlElement(required = true)
    protected LawyerList lawyers;

    /**
     * Gets the value of the lawyers property.
     *
     * @return possible object is
     * {@link LawyerList }
     */
    public LawyerList getLawyers() {
        return lawyers;
    }

    /**
     * Sets the value of the lawyers property.
     *
     * @param value allowed object is
     *              {@link LawyerList }
     */
    public void setLawyers(LawyerList value) {
        this.lawyers = value;
    }

}
