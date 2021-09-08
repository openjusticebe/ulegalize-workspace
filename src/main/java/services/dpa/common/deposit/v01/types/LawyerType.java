
package services.dpa.common.deposit.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LawyerType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="LawyerType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/deposit/v01/types}RecipientType">
 *       &lt;sequence>
 *         &lt;element name="officeID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="lawyerID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LawyerType", propOrder = {
        "officeID",
        "lawyerID"
})
public class LawyerType
        extends RecipientType {

    @XmlElement(required = true)
    protected String officeID;
    @XmlElement(required = true)
    protected String lawyerID;

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
