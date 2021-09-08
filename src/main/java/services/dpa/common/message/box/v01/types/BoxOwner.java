
package services.dpa.common.message.box.v01.types;

import services.dpa.common.addressbook.base.v01.codes.Capacity;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for BoxOwner complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BoxOwner">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lawyerId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="organisationName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="personFirstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="personLastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="capacity" type="{http://common.dpa.services/addressbook/base/v01/codes}Capacity"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BoxOwner", propOrder = {
        "lawyerId",
        "organisationName",
        "personFirstName",
        "personLastName",
        "capacity"
})
public class BoxOwner {

    @XmlElement(required = true)
    protected String lawyerId;
    @XmlElement(required = true)
    protected String organisationName;
    @XmlElement(required = true)
    protected String personFirstName;
    @XmlElement(required = true)
    protected String personLastName;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected Capacity capacity;

    /**
     * Gets the value of the lawyerId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLawyerId() {
        return lawyerId;
    }

    /**
     * Sets the value of the lawyerId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLawyerId(String value) {
        this.lawyerId = value;
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
     * Gets the value of the personFirstName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPersonFirstName() {
        return personFirstName;
    }

    /**
     * Sets the value of the personFirstName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPersonFirstName(String value) {
        this.personFirstName = value;
    }

    /**
     * Gets the value of the personLastName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPersonLastName() {
        return personLastName;
    }

    /**
     * Sets the value of the personLastName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPersonLastName(String value) {
        this.personLastName = value;
    }

    /**
     * Gets the value of the capacity property.
     *
     * @return possible object is
     * {@link Capacity }
     */
    public Capacity getCapacity() {
        return capacity;
    }

    /**
     * Sets the value of the capacity property.
     *
     * @param value allowed object is
     *              {@link Capacity }
     */
    public void setCapacity(Capacity value) {
        this.capacity = value;
    }

}
