
package services.dpa.common.message.box.v01.types;

import services.dpa.common.addressbook.base.v01.codes.Capacity;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for SenderPerson complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SenderPerson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="personId" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="capacityCode" type="{http://common.dpa.services/addressbook/base/v01/codes}Capacity"/>
 *         &lt;element name="personFirstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="personLastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SenderPerson", propOrder = {
        "personId",
        "capacityCode",
        "personFirstName",
        "personLastName"
})
public class SenderPerson {

    @XmlElement(required = true)
    protected String personId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected Capacity capacityCode;
    @XmlElement(required = true)
    protected String personFirstName;
    @XmlElement(required = true)
    protected String personLastName;

    /**
     * Gets the value of the personId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Sets the value of the personId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPersonId(String value) {
        this.personId = value;
    }

    /**
     * Gets the value of the capacityCode property.
     *
     * @return possible object is
     * {@link Capacity }
     */
    public Capacity getCapacityCode() {
        return capacityCode;
    }

    /**
     * Sets the value of the capacityCode property.
     *
     * @param value allowed object is
     *              {@link Capacity }
     */
    public void setCapacityCode(Capacity value) {
        this.capacityCode = value;
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

}
