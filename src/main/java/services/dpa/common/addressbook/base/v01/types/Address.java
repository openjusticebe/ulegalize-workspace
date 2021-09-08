
package services.dpa.common.addressbook.base.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Information about the address
 *
 * <p>Java class for Address complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Address">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="streetName" type="{http://common.dpa.services/global/v01/types}StringMax250"/>
 *         &lt;element name="houseNumber" type="{http://common.dpa.services/global/v01/types}StringMax20" minOccurs="0"/>
 *         &lt;element name="postbus" type="{http://common.dpa.services/global/v01/types}StringMax20" minOccurs="0"/>
 *         &lt;element name="zipCode" type="{http://common.dpa.services/global/v01/types}ZipCode"/>
 *         &lt;element name="city" type="{http://common.dpa.services/global/v01/types}StringMax250"/>
 *         &lt;element name="country" type="{http://common.dpa.services/global/v01/types}Country"/>
 *         &lt;element name="streetCode" type="{http://common.dpa.services/global/v01/types}StringMax10" minOccurs="0"/>
 *         &lt;element name="nisCode" type="{http://common.dpa.services/global/v01/types}StringMax10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Address", propOrder = {
        "streetName",
        "houseNumber",
        "postbus",
        "zipCode",
        "city",
        "country",
        "streetCode",
        "nisCode"
})
public class Address {

    @XmlElement(required = true)
    protected String streetName;
    protected String houseNumber;
    protected String postbus;
    @XmlElement(required = true)
    protected String zipCode;
    @XmlElement(required = true)
    protected String city;
    @XmlElement(required = true, defaultValue = "BE")
    protected String country;
    protected String streetCode;
    protected String nisCode;

    /**
     * Gets the value of the streetName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * Sets the value of the streetName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStreetName(String value) {
        this.streetName = value;
    }

    /**
     * Gets the value of the houseNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getHouseNumber() {
        return houseNumber;
    }

    /**
     * Sets the value of the houseNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setHouseNumber(String value) {
        this.houseNumber = value;
    }

    /**
     * Gets the value of the postbus property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPostbus() {
        return postbus;
    }

    /**
     * Sets the value of the postbus property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPostbus(String value) {
        this.postbus = value;
    }

    /**
     * Gets the value of the zipCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the value of the zipCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setZipCode(String value) {
        this.zipCode = value;
    }

    /**
     * Gets the value of the city property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the country property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the streetCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getStreetCode() {
        return streetCode;
    }

    /**
     * Sets the value of the streetCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStreetCode(String value) {
        this.streetCode = value;
    }

    /**
     * Gets the value of the nisCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNisCode() {
        return nisCode;
    }

    /**
     * Sets the value of the nisCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNisCode(String value) {
        this.nisCode = value;
    }

}
