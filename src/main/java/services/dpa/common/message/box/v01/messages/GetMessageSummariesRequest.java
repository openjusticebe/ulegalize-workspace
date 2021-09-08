
package services.dpa.common.message.box.v01.messages;

import services.dpa.common.message.box.v01.types.Direction;
import services.dpa.common.message.box.v01.types.MessageStatusList;
import services.dpa.common.message.box.v01.types.ReceivedFrom;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;


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
 *         &lt;element name="boxId" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="updatedBetween" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                   &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="direction" type="{http://common.dpa.services/message/box/v01/types}Direction" minOccurs="0"/>
 *         &lt;element name="messageStatuses" type="{http://common.dpa.services/message/box/v01/types}MessageStatusList" minOccurs="0"/>
 *         &lt;element name="receivedFrom" type="{http://common.dpa.services/message/box/v01/types}ReceivedFrom" minOccurs="0"/>
 *         &lt;element name="caseReference" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="courtReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="myReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="transactionId" type="{http://common.dpa.services/global/v01/types}UUID" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="languageCode" type="{http://www.w3.org/2001/XMLSchema}language" />
 *       &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="limit" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="sort" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "boxId",
        "updatedBetween",
        "direction",
        "messageStatuses",
        "receivedFrom",
        "caseReference",
        "transactionId"
})
@XmlRootElement(name = "getMessageSummariesRequest")
public class GetMessageSummariesRequest {

    @XmlElement(required = true)
    protected String boxId;
    protected GetMessageSummariesRequest.UpdatedBetween updatedBetween;
    @XmlSchemaType(name = "string")
    protected Direction direction;
    protected MessageStatusList messageStatuses;
    protected ReceivedFrom receivedFrom;
    protected GetMessageSummariesRequest.CaseReference caseReference;
    protected String transactionId;
    @XmlAttribute(name = "languageCode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "language")
    protected String languageCode;
    @XmlAttribute(name = "offset")
    protected BigInteger offset;
    @XmlAttribute(name = "limit")
    protected BigInteger limit;
    @XmlAttribute(name = "sort")
    protected String sort;

    /**
     * Gets the value of the boxId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBoxId() {
        return boxId;
    }

    /**
     * Sets the value of the boxId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBoxId(String value) {
        this.boxId = value;
    }

    /**
     * Gets the value of the updatedBetween property.
     *
     * @return possible object is
     * {@link GetMessageSummariesRequest.UpdatedBetween }
     */
    public GetMessageSummariesRequest.UpdatedBetween getUpdatedBetween() {
        return updatedBetween;
    }

    /**
     * Sets the value of the updatedBetween property.
     *
     * @param value allowed object is
     *              {@link GetMessageSummariesRequest.UpdatedBetween }
     */
    public void setUpdatedBetween(GetMessageSummariesRequest.UpdatedBetween value) {
        this.updatedBetween = value;
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
     * Gets the value of the messageStatuses property.
     *
     * @return possible object is
     * {@link MessageStatusList }
     */
    public MessageStatusList getMessageStatuses() {
        return messageStatuses;
    }

    /**
     * Sets the value of the messageStatuses property.
     *
     * @param value allowed object is
     *              {@link MessageStatusList }
     */
    public void setMessageStatuses(MessageStatusList value) {
        this.messageStatuses = value;
    }

    /**
     * Gets the value of the receivedFrom property.
     *
     * @return possible object is
     * {@link ReceivedFrom }
     */
    public ReceivedFrom getReceivedFrom() {
        return receivedFrom;
    }

    /**
     * Sets the value of the receivedFrom property.
     *
     * @param value allowed object is
     *              {@link ReceivedFrom }
     */
    public void setReceivedFrom(ReceivedFrom value) {
        this.receivedFrom = value;
    }

    /**
     * Gets the value of the caseReference property.
     *
     * @return possible object is
     * {@link GetMessageSummariesRequest.CaseReference }
     */
    public GetMessageSummariesRequest.CaseReference getCaseReference() {
        return caseReference;
    }

    /**
     * Sets the value of the caseReference property.
     *
     * @param value allowed object is
     *              {@link GetMessageSummariesRequest.CaseReference }
     */
    public void setCaseReference(GetMessageSummariesRequest.CaseReference value) {
        this.caseReference = value;
    }

    /**
     * Gets the value of the transactionId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the languageCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * Sets the value of the languageCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLanguageCode(String value) {
        this.languageCode = value;
    }

    /**
     * Gets the value of the offset property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getOffset() {
        return offset;
    }

    /**
     * Sets the value of the offset property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setOffset(BigInteger value) {
        this.offset = value;
    }

    /**
     * Gets the value of the limit property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getLimit() {
        return limit;
    }

    /**
     * Sets the value of the limit property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setLimit(BigInteger value) {
        this.limit = value;
    }

    /**
     * Gets the value of the sort property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSort() {
        return sort;
    }

    /**
     * Sets the value of the sort property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSort(String value) {
        this.sort = value;
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
     *         &lt;element name="courtReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="myReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "courtReference",
            "myReference"
    })
    public static class CaseReference {

        protected String courtReference;
        protected String myReference;

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
         * Gets the value of the myReference property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getMyReference() {
            return myReference;
        }

        /**
         * Sets the value of the myReference property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setMyReference(String value) {
            this.myReference = value;
        }

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
     *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "startDate",
            "endDate"
    })
    public static class UpdatedBetween {

        @XmlElement(required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar startDate;
        @XmlElement(required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar endDate;

        /**
         * Gets the value of the startDate property.
         *
         * @return possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getStartDate() {
            return startDate;
        }

        /**
         * Sets the value of the startDate property.
         *
         * @param value allowed object is
         *              {@link XMLGregorianCalendar }
         */
        public void setStartDate(XMLGregorianCalendar value) {
            this.startDate = value;
        }

        /**
         * Gets the value of the endDate property.
         *
         * @return possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getEndDate() {
            return endDate;
        }

        /**
         * Sets the value of the endDate property.
         *
         * @param value allowed object is
         *              {@link XMLGregorianCalendar }
         */
        public void setEndDate(XMLGregorianCalendar value) {
            this.endDate = value;
        }

    }

}
