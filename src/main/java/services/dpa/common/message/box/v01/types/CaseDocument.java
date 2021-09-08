
package services.dpa.common.message.box.v01.types;

import services.dpa.common.message.base.v01.codes.MessageState;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for CaseDocument complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CaseDocument">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="docID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="mimeType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="docType" type="{http://common.dpa.services/message/box/v01/types}DocType"/>
 *         &lt;element name="direction" type="{http://common.dpa.services/message/box/v01/types}Direction"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="docNr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transactionTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="messageId" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="messageState" type="{http://common.dpa.services/message/base/v01/codes}MessageState"/>
 *         &lt;element name="pending" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CaseDocument", propOrder = {
        "docID",
        "mimeType",
        "docType",
        "direction",
        "fileName",
        "docNr",
        "transactionTime",
        "messageId",
        "messageState",
        "pending"
})
public class CaseDocument {

    @XmlElement(required = true)
    protected String docID;
    @XmlElement(required = true)
    protected String mimeType;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected DocType docType;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected Direction direction;
    @XmlElement(required = true)
    protected String fileName;
    protected String docNr;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar transactionTime;
    @XmlElement(required = true)
    protected String messageId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected MessageState messageState;
    protected boolean pending;

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
     * Gets the value of the direction property.
     *
     * @return possible object is
     * {@link Direction }
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     *
     * @param value allowed object is
     *              {@link Direction }
     */
    public void setDirection(Direction value) {
        this.direction = value;
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
     * Gets the value of the transactionTime property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getTransactionTime() {
        return transactionTime;
    }

    /**
     * Sets the value of the transactionTime property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setTransactionTime(XMLGregorianCalendar value) {
        this.transactionTime = value;
    }

    /**
     * Gets the value of the messageId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMessageId(String value) {
        this.messageId = value;
    }

    /**
     * Gets the value of the messageState property.
     *
     * @return possible object is
     * {@link MessageState }
     */
    public MessageState getMessageState() {
        return messageState;
    }

    /**
     * Sets the value of the messageState property.
     *
     * @param value allowed object is
     *              {@link MessageState }
     */
    public void setMessageState(MessageState value) {
        this.messageState = value;
    }

    /**
     * Gets the value of the pending property.
     */
    public boolean isPending() {
        return pending;
    }

    /**
     * Sets the value of the pending property.
     */
    public void setPending(boolean value) {
        this.pending = value;
    }

}
