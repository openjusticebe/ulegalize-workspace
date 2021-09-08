
package services.dpa.common.addressbook.base.v01.types;

import services.dpa.common.addressbook.base.v01.codes.BillingOrganisationType;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for BillingOrganisation complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BillingOrganisation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/addressbook/base/v01/types}Organisation">
 *       &lt;sequence>
 *         &lt;element name="organisationType" type="{http://common.dpa.services/addressbook/base/v01/codes}BillingOrganisationType"/>
 *         &lt;element name="entrepriseNumber" type="{http://common.dpa.services/global/v01/types}EntrepriseNumber"/>
 *         &lt;element name="vatNumber" type="{http://common.dpa.services/global/v01/types}VatNumber" minOccurs="0"/>
 *         &lt;element name="billingID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BillingOrganisation", propOrder = {
        "organisationType",
        "entrepriseNumber",
        "vatNumber",
        "billingID"
})
public class BillingOrganisation
        extends Organisation {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected BillingOrganisationType organisationType;
    @XmlElement(required = true)
    protected String entrepriseNumber;
    protected String vatNumber;
    protected Integer billingID;

    /**
     * Gets the value of the organisationType property.
     *
     * @return possible object is
     * {@link BillingOrganisationType }
     */
    public BillingOrganisationType getOrganisationType() {
        return organisationType;
    }

    /**
     * Sets the value of the organisationType property.
     *
     * @param value allowed object is
     *              {@link BillingOrganisationType }
     */
    public void setOrganisationType(BillingOrganisationType value) {
        this.organisationType = value;
    }

    /**
     * Gets the value of the entrepriseNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEntrepriseNumber() {
        return entrepriseNumber;
    }

    /**
     * Sets the value of the entrepriseNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEntrepriseNumber(String value) {
        this.entrepriseNumber = value;
    }

    /**
     * Gets the value of the vatNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVatNumber() {
        return vatNumber;
    }

    /**
     * Sets the value of the vatNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVatNumber(String value) {
        this.vatNumber = value;
    }

    /**
     * Gets the value of the billingID property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getBillingID() {
        return billingID;
    }

    /**
     * Sets the value of the billingID property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setBillingID(Integer value) {
        this.billingID = value;
    }

}
