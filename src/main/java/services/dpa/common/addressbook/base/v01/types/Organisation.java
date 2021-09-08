
package services.dpa.common.addressbook.base.v01.types;

import javax.xml.bind.annotation.*;


/**
 * Grouped information about the organization
 *
 * <p>Java class for Organisation complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Organisation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://common.dpa.services/global/v01/types}OrganisationID"/>
 *         &lt;element name="name" type="{http://common.dpa.services/global/v01/types}StringMax256" minOccurs="0"/>
 *         &lt;element name="address" type="{http://common.dpa.services/addressbook/base/v01/types}Address"/>
 *         &lt;element name="organisationCommunication" type="{http://common.dpa.services/addressbook/base/v01/types}CommunicationMediumList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Organisation", propOrder = {
        "id",
        "name",
        "address",
        "organisationCommunication"
})
@XmlSeeAlso({
        BillingOrganisation.class,
        LawyerOffice.class
})
public abstract class Organisation {

    @XmlElement(name = "ID", required = true)
    protected String id;
    protected String name;
    @XmlElement(required = true)
    protected Address address;
    protected CommunicationMediumList organisationCommunication;

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
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the address property.
     *
     * @return possible object is
     * {@link Address }
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     *
     * @param value allowed object is
     *              {@link Address }
     */
    public void setAddress(Address value) {
        this.address = value;
    }

    /**
     * Gets the value of the organisationCommunication property.
     *
     * @return possible object is
     * {@link CommunicationMediumList }
     */
    public CommunicationMediumList getOrganisationCommunication() {
        return organisationCommunication;
    }

    /**
     * Sets the value of the organisationCommunication property.
     *
     * @param value allowed object is
     *              {@link CommunicationMediumList }
     */
    public void setOrganisationCommunication(CommunicationMediumList value) {
        this.organisationCommunication = value;
    }

}
