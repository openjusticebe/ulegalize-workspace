
package services.dpa.common.global.v01.messages;

import javax.xml.bind.annotation.*;


/**
 * Explicit exception declaration, general exception for any operation
 *
 * <p>Java class for GeneralFault complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="GeneralFault">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="detail" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="incidentnumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeneralFault", propOrder = {
        "code",
        "description",
        "detail",
        "incidentnumber"
})
@XmlSeeAlso({
        BasicFault.class
})
public class GeneralFault {

    @XmlElement(required = true)
    protected String code;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected String detail;
    protected String incidentnumber;

    /**
     * Gets the value of the code property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the detail property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Sets the value of the detail property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDetail(String value) {
        this.detail = value;
    }

    /**
     * Gets the value of the incidentnumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getIncidentnumber() {
        return incidentnumber;
    }

    /**
     * Sets the value of the incidentnumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIncidentnumber(String value) {
        this.incidentnumber = value;
    }

}
