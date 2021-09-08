
package services.dpa.common.message.box.v01.types;

import services.dpa.common.message.base.v01.codes.TransDocState;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for Doc complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Doc">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="docID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mimeType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numberPages" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="docType" type="{http://common.dpa.services/message/box/v01/types}DocType"/>
 *         &lt;element name="docNr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="state" type="{http://common.dpa.services/message/base/v01/codes}TransDocState"/>
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Doc", propOrder = {
        "docID",
        "fileName",
        "mimeType",
        "numberPages",
        "description",
        "docType",
        "docNr",
        "state",
        "comment"
})
public class Doc {

    @XmlElement(required = true)
    protected String docID;
    @XmlElement(required = true)
    protected String fileName;
    @XmlElement(required = true)
    protected String mimeType;
    protected long numberPages;
    protected String description;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected DocType docType;
    protected String docNr;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TransDocState state;
    protected String comment;

    /**
     * Gets the value of the docID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDocID() {
        return docID;
    }

    /**
     * Sets the value of the docID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDocID(String value) {
        this.docID = value;
    }

    /**
     * Gets the value of the fileName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the mimeType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the value of the mimeType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMimeType(String value) {
        this.mimeType = value;
    }

    /**
     * Gets the value of the numberPages property.
     */
    public long getNumberPages() {
        return numberPages;
    }

    /**
     * Sets the value of the numberPages property.
     */
    public void setNumberPages(long value) {
        this.numberPages = value;
    }

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
     * {@link DocType }
     */
    public DocType getDocType() {
        return docType;
    }

    /**
     * Sets the value of the docType property.
     *
     * @param value allowed object is
     *              {@link DocType }
     */
    public void setDocType(DocType value) {
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
     * Gets the value of the state property.
     *
     * @return possible object is
     * {@link TransDocState }
     */
    public TransDocState getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     *
     * @param value allowed object is
     *              {@link TransDocState }
     */
    public void setState(TransDocState value) {
        this.state = value;
    }

    /**
     * Gets the value of the comment property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setComment(String value) {
        this.comment = value;
    }

}
