
package services.dpa.common.message.box.v01.types;

import services.dpa.common.message.base.v01.codes.MessageState;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Message complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Message">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="direction" type="{http://common.dpa.services/message/box/v01/types}Direction"/>
 *         &lt;element name="messageId" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="state" type="{http://common.dpa.services/message/base/v01/codes}MessageState"/>
 *         &lt;element name="totalPrice" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="basePrice" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="sender" type="{http://common.dpa.services/message/box/v01/types}Sender"/>
 *         &lt;element name="caseReference" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="senderReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="courtReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="proDeoReference" type="{http://common.dpa.services/global/v01/types}StringMax64" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="sent" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="createdOn" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="updatedOn" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Message", propOrder = {
        "direction",
        "messageId",
        "state",
        "totalPrice",
        "basePrice",
        "sender",
        "caseReference",
        "sent",
        "createdOn",
        "updatedOn"
})
@XmlSeeAlso({
        IncomingMessage.class,
        OutgoingMessage.class
})
public abstract class Message {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected Direction direction;
    @XmlElement(required = true)
    protected String messageId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected MessageState state;
    protected double totalPrice;
    protected double basePrice;
    @XmlElement(required = true)
    protected Sender sender;
    protected CaseReference caseReference;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar sent;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdOn;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updatedOn;

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
     * Gets the value of the state property.
     *
     * @return possible object is
     * {@link MessageState }
     */
    public MessageState getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     *
     * @param value allowed object is
     *              {@link MessageState }
     */
    public void setState(MessageState value) {
        this.state = value;
    }

    /**
     * Gets the value of the totalPrice property.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the value of the totalPrice property.
     */
    public void setTotalPrice(double value) {
        this.totalPrice = value;
    }

    /**
     * Gets the value of the basePrice property.
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Sets the value of the basePrice property.
     */
    public void setBasePrice(double value) {
        this.basePrice = value;
    }

    /**
     * Gets the value of the sender property.
     *
     * @return possible object is
     * {@link Sender }
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     *
     * @param value allowed object is
     *              {@link Sender }
     */
    public void setSender(Sender value) {
        this.sender = value;
    }

    /**
     * Gets the value of the caseReference property.
     *
     * @return possible object is
     * {@link CaseReference }
     */
    public CaseReference getCaseReference() {
        return caseReference;
    }

    /**
     * Sets the value of the caseReference property.
     *
     * @param value allowed object is
     *              {@link CaseReference }
     */
    public void setCaseReference(CaseReference value) {
        this.caseReference = value;
    }

    /**
     * Gets the value of the sent property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getSent() {
        return sent;
    }

    /**
     * Sets the value of the sent property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setSent(XMLGregorianCalendar value) {
        this.sent = value;
    }

    /**
     * Gets the value of the createdOn property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the value of the createdOn property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setCreatedOn(XMLGregorianCalendar value) {
        this.createdOn = value;
    }

    /**
     * Gets the value of the updatedOn property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getUpdatedOn() {
        return updatedOn;
    }

    /**
     * Sets the value of the updatedOn property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setUpdatedOn(XMLGregorianCalendar value) {
        this.updatedOn = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="senderReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="courtReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="proDeoReference" type="{http://common.dpa.services/global/v01/types}StringMax64" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "senderReference",
            "courtReference",
            "proDeoReference"
    })
    public static class CaseReference {

        protected String senderReference;
        protected String courtReference;
        protected String proDeoReference;

        /**
         * Gets the value of the senderReference property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getSenderReference() {
            return senderReference;
        }

        /**
         * Sets the value of the senderReference property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setSenderReference(String value) {
            this.senderReference = value;
        }

        /**
         * Gets the value of the courtReference property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getCourtReference() {
            return courtReference;
        }

        /**
         * Sets the value of the courtReference property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setCourtReference(String value) {
            this.courtReference = value;
        }

        /**
         * Gets the value of the proDeoReference property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getProDeoReference() {
            return proDeoReference;
        }

        /**
         * Sets the value of the proDeoReference property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setProDeoReference(String value) {
            this.proDeoReference = value;
        }

    }

}
