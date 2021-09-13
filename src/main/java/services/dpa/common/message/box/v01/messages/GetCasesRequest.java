
package services.dpa.common.message.box.v01.messages;

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
 *         &lt;element name="lawyerReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
 *         &lt;element name="openedBetween" minOccurs="0">
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
        "lawyerReference",
        "updatedBetween",
        "openedBetween"
})
@XmlRootElement(name = "getCasesRequest")
public class GetCasesRequest {

    @XmlElement(required = true)
    protected String boxId;
    protected String lawyerReference;
    protected UpdatedBetween updatedBetween;
    protected OpenedBetween openedBetween;
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
     * Gets the value of the lawyerReference property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLawyerReference() {
        return lawyerReference;
    }

    /**
     * Sets the value of the lawyerReference property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLawyerReference(String value) {
        this.lawyerReference = value;
    }

    /**
     * Gets the value of the updatedBetween property.
     *
     * @return possible object is
     * {@link UpdatedBetween }
     */
    public UpdatedBetween getUpdatedBetween() {
        return updatedBetween;
    }

    /**
     * Sets the value of the updatedBetween property.
     *
     * @param value allowed object is
     *              {@link UpdatedBetween }
     */
    public void setUpdatedBetween(UpdatedBetween value) {
        this.updatedBetween = value;
    }

    /**
     * Gets the value of the openedBetween property.
     *
     * @return possible object is
     * {@link OpenedBetween }
     */
    public OpenedBetween getOpenedBetween() {
        return openedBetween;
    }

    /**
     * Sets the value of the openedBetween property.
     *
     * @param value allowed object is
     *              {@link OpenedBetween }
     */
    public void setOpenedBetween(OpenedBetween value) {
        this.openedBetween = value;
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
    public static class OpenedBetween {

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
