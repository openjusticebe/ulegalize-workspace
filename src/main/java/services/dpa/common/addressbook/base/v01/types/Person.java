
package services.dpa.common.addressbook.base.v01.types;

import services.dpa.common.global.v01.codes.Gender;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Personal information about the user
 *
 * <p>Java class for Person complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Person">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://common.dpa.services/global/v01/types}PersonID"/>
 *         &lt;element name="firstName" type="{http://common.dpa.services/global/v01/types}StringMax100"/>
 *         &lt;element name="lastName" type="{http://common.dpa.services/global/v01/types}StringMax100"/>
 *         &lt;element name="gender" type="{http://common.dpa.services/global/v01/codes}Gender" minOccurs="0"/>
 *         &lt;element name="birthDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="nationality" type="{http://common.dpa.services/global/v01/types}Country" minOccurs="0"/>
 *         &lt;element name="nationalNumber" type="{http://common.dpa.services/global/v01/types}NationalNumber" minOccurs="0"/>
 *         &lt;element name="personCommunication" type="{http://common.dpa.services/addressbook/base/v01/types}CommunicationMediumList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Person", propOrder = {
        "id",
        "firstName",
        "lastName",
        "gender",
        "birthDate",
        "nationality",
        "nationalNumber",
        "personCommunication"
})
public abstract class Person {

    @XmlElement(name = "ID", required = true)
    protected String id;
    @XmlElement(required = true)
    protected String firstName;
    @XmlElement(required = true)
    protected String lastName;
    @XmlSchemaType(name = "string")
    protected Gender gender;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar birthDate;
    @XmlElement(defaultValue = "BE")
    protected String nationality;
    protected String nationalNumber;
    protected CommunicationMediumList personCommunication;

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the firstName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the gender property.
     *
     * @return possible object is
     * {@link Gender }
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     *
     * @param value allowed object is
     *              {@link Gender }
     */
    public void setGender(Gender value) {
        this.gender = value;
    }

    /**
     * Gets the value of the birthDate property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setBirthDate(XMLGregorianCalendar value) {
        this.birthDate = value;
    }

    /**
     * Gets the value of the nationality property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Sets the value of the nationality property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNationality(String value) {
        this.nationality = value;
    }

    /**
     * Gets the value of the nationalNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNationalNumber() {
        return nationalNumber;
    }

    /**
     * Sets the value of the nationalNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNationalNumber(String value) {
        this.nationalNumber = value;
    }

    /**
     * Gets the value of the personCommunication property.
     *
     * @return possible object is
     * {@link CommunicationMediumList }
     */
    public CommunicationMediumList getPersonCommunication() {
        return personCommunication;
    }

    /**
     * Sets the value of the personCommunication property.
     *
     * @param value allowed object is
     *              {@link CommunicationMediumList }
     */
    public void setPersonCommunication(CommunicationMediumList value) {
        this.personCommunication = value;
    }

}
