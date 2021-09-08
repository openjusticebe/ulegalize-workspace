
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Lawyer complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Lawyer">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/message/box/v01/types}Recipient">
 *       &lt;sequence>
 *         &lt;element name="officeID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="officeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lawyerID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="lawyerFirstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lawyerLastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Lawyer", propOrder = {
        "officeID",
        "officeName",
        "lawyerID",
        "lawyerFirstName",
        "lawyerLastName"
})
public class Lawyer
        extends Recipient {

    @XmlElement(required = true)
    protected String officeID;
    @XmlElement(required = true)
    protected String officeName;
    @XmlElement(required = true)
    protected String lawyerID;
    @XmlElement(required = true)
    protected String lawyerFirstName;
    @XmlElement(required = true)
    protected String lawyerLastName;

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
     * Gets the value of the officeName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOfficeName() {
        return officeName;
    }

    /**
     * Sets the value of the officeName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOfficeName(String value) {
        this.officeName = value;
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

    /**
     * Gets the value of the lawyerFirstName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLawyerFirstName() {
        return lawyerFirstName;
    }

    /**
     * Sets the value of the lawyerFirstName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLawyerFirstName(String value) {
        this.lawyerFirstName = value;
    }

    /**
     * Gets the value of the lawyerLastName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLawyerLastName() {
        return lawyerLastName;
    }

    /**
     * Sets the value of the lawyerLastName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLawyerLastName(String value) {
        this.lawyerLastName = value;
    }

}
