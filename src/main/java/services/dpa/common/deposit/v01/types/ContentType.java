
package services.dpa.common.deposit.v01.types;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for ContentType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ContentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://common.dpa.services/global/v01/types}StringMax512" minOccurs="0"/>
 *         &lt;element name="docType" type="{http://common.dpa.services/deposit/v01/types}DocTypeType"/>
 *         &lt;element name="docNr" type="{http://common.dpa.services/global/v01/types}StringMax12" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ref" type="{http://common.dpa.services/global/v01/types}UUID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContentType", propOrder = {
        "description",
        "docType",
        "docNr"
})
public class ContentType {

    protected String description;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected DocTypeType docType;
    protected String docNr;
    @XmlAttribute(name = "ref")
    protected String ref;

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
     * Gets the value of the docType property.
     *
     * @return possible object is
     * {@link DocTypeType }
     */
    public DocTypeType getDocType() {
        return docType;
    }

    /**
     * Sets the value of the docType property.
     *
     * @param value allowed object is
     *              {@link DocTypeType }
     */
    public void setDocType(DocTypeType value) {
        this.docType = value;
    }

    /**
     * Gets the value of the docNr property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDocNr() {
        return docNr;
    }

    /**
     * Sets the value of the docNr property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDocNr(String value) {
        this.docNr = value;
    }

    /**
     * Gets the value of the ref property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRef(String value) {
        this.ref = value;
    }

}
