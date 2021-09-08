
package services.dpa.common.message.box.v01.types;

import services.dpa.common.message.base.v01.codes.OrganisationType;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for SenderOrganisation complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SenderOrganisation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="organisationId" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="organisationType" type="{http://common.dpa.services/message/base/v01/codes}OrganisationType"/>
 *         &lt;element name="organisationName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="organisationEnterpriseNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SenderOrganisation", propOrder = {
        "organisationId",
        "organisationType",
        "organisationName",
        "organisationEnterpriseNumber"
})
public class SenderOrganisation {

    @XmlElement(required = true)
    protected String organisationId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected OrganisationType organisationType;
    @XmlElement(required = true)
    protected String organisationName;
    protected String organisationEnterpriseNumber;

    /**
     * Gets the value of the organisationId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOrganisationId() {
        return organisationId;
    }

    /**
     * Sets the value of the organisationId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOrganisationId(String value) {
        this.organisationId = value;
    }

    /**
     * Gets the value of the organisationType property.
     *
     * @return possible object is
     * {@link OrganisationType }
     */
    public OrganisationType getOrganisationType() {
        return organisationType;
    }

    /**
     * Sets the value of the organisationType property.
     *
     * @param value allowed object is
     *              {@link OrganisationType }
     */
    public void setOrganisationType(OrganisationType value) {
        this.organisationType = value;
    }

    /**
     * Gets the value of the organisationName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOrganisationName() {
        return organisationName;
    }

    /**
     * Sets the value of the organisationName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOrganisationName(String value) {
        this.organisationName = value;
    }

    /**
     * Gets the value of the organisationEnterpriseNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOrganisationEnterpriseNumber() {
        return organisationEnterpriseNumber;
    }

    /**
     * Sets the value of the organisationEnterpriseNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOrganisationEnterpriseNumber(String value) {
        this.organisationEnterpriseNumber = value;
    }

}
