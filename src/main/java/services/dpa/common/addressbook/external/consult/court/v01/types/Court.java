
package services.dpa.common.addressbook.external.consult.court.v01.types;

import services.dpa.common.addressbook.base.v01.types.Organisation;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for Court complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Court">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/addressbook/base/v01/types}Organisation">
 *       &lt;sequence>
 *         &lt;element name="courtLawType" type="{http://common.dpa.services/global/v01/types}StringMax20" minOccurs="0"/>
 *         &lt;element name="courtType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="courtJurisdiction" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="referencePattern" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referenceMask" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="externalKey" type="{http://common.dpa.services/global/v01/types}StringMax100"/>
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="referenceRule" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}ReferenceRule"/>
 *         &lt;element name="supportsCaseLookup" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Court", propOrder = {
        "courtLawType",
        "courtType",
        "courtJurisdiction",
        "referencePattern",
        "referenceMask",
        "externalKey",
        "active",
        "referenceRule",
        "supportsCaseLookup"
})
@XmlSeeAlso({
        services.dpa.common.deposit.consult.v01.types.Court.class
})
public class Court
        extends Organisation {

    protected String courtLawType;
    protected Integer courtType;
    protected Integer courtJurisdiction;
    @XmlElement(required = true)
    protected String referencePattern;
    protected String referenceMask;
    @XmlElement(required = true)
    protected String externalKey;
    protected Boolean active;
    @XmlElement(required = true)
    protected ReferenceRule referenceRule;
    protected Boolean supportsCaseLookup;

    /**
     * Gets the value of the courtLawType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCourtLawType() {
        return courtLawType;
    }

    /**
     * Sets the value of the courtLawType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCourtLawType(String value) {
        this.courtLawType = value;
    }

    /**
     * Gets the value of the courtType property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getCourtType() {
        return courtType;
    }

    /**
     * Sets the value of the courtType property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setCourtType(Integer value) {
        this.courtType = value;
    }

    /**
     * Gets the value of the courtJurisdiction property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getCourtJurisdiction() {
        return courtJurisdiction;
    }

    /**
     * Sets the value of the courtJurisdiction property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setCourtJurisdiction(Integer value) {
        this.courtJurisdiction = value;
    }

    /**
     * Gets the value of the referencePattern property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getReferencePattern() {
        return referencePattern;
    }

    /**
     * Sets the value of the referencePattern property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setReferencePattern(String value) {
        this.referencePattern = value;
    }

    /**
     * Gets the value of the referenceMask property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getReferenceMask() {
        return referenceMask;
    }

    /**
     * Sets the value of the referenceMask property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setReferenceMask(String value) {
        this.referenceMask = value;
    }

    /**
     * Gets the value of the externalKey property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getExternalKey() {
        return externalKey;
    }

    /**
     * Sets the value of the externalKey property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExternalKey(String value) {
        this.externalKey = value;
    }

    /**
     * Gets the value of the active property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setActive(Boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the referenceRule property.
     *
     * @return possible object is
     * {@link ReferenceRule }
     */
    public ReferenceRule getReferenceRule() {
        return referenceRule;
    }

    /**
     * Sets the value of the referenceRule property.
     *
     * @param value allowed object is
     *              {@link ReferenceRule }
     */
    public void setReferenceRule(ReferenceRule value) {
        this.referenceRule = value;
    }

    /**
     * Gets the value of the supportsCaseLookup property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isSupportsCaseLookup() {
        return supportsCaseLookup;
    }

    /**
     * Sets the value of the supportsCaseLookup property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setSupportsCaseLookup(Boolean value) {
        this.supportsCaseLookup = value;
    }

}
