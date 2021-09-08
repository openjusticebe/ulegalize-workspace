
package services.dpa.common.addressbook.base.v01.types;

import services.dpa.common.global.v01.codes.BarAssociation;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for LawyerOffice complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="LawyerOffice">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/addressbook/base/v01/types}Organisation">
 *       &lt;sequence>
 *         &lt;element name="barAssociation" type="{http://common.dpa.services/global/v01/codes}BarAssociation"/>
 *         &lt;element name="bar" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="parentID" type="{http://common.dpa.services/global/v01/types}OrganisationID" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LawyerOffice", propOrder = {
        "barAssociation",
        "bar",
        "parentID"
})
public class LawyerOffice
        extends Organisation {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected BarAssociation barAssociation;
    @XmlElement(required = true)
    protected String bar;
    protected String parentID;

    /**
     * Gets the value of the barAssociation property.
     *
     * @return possible object is
     * {@link BarAssociation }
     */
    public BarAssociation getBarAssociation() {
        return barAssociation;
    }

    /**
     * Sets the value of the barAssociation property.
     *
     * @param value allowed object is
     *              {@link BarAssociation }
     */
    public void setBarAssociation(BarAssociation value) {
        this.barAssociation = value;
    }

    /**
     * Gets the value of the bar property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBar() {
        return bar;
    }

    /**
     * Sets the value of the bar property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBar(String value) {
        this.bar = value;
    }

    /**
     * Gets the value of the parentID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getParentID() {
        return parentID;
    }

    /**
     * Sets the value of the parentID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setParentID(String value) {
        this.parentID = value;
    }

}
