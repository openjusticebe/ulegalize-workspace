
package services.dpa.common.deposit.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SenderType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SenderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lawyerID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="enterpriseNumber" type="{http://common.dpa.services/global/v01/types}EntrepriseNumber"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SenderType", propOrder = {
        "lawyerID",
        "enterpriseNumber"
})
public class SenderType {

    @XmlElement(required = true)
    protected String lawyerID;
    @XmlElement(required = true)
    protected String enterpriseNumber;

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

    /**
     * Gets the value of the enterpriseNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEnterpriseNumber() {
        return enterpriseNumber;
    }

    /**
     * Sets the value of the enterpriseNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEnterpriseNumber(String value) {
        this.enterpriseNumber = value;
    }

}
