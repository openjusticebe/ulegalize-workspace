
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Case complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Case">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="caseId" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="caseReference" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="openedOn" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="lastUpdatedOn" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="nrOfDocuments" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nrOfPendingTransactions" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Case", propOrder = {
        "caseId",
        "caseReference",
        "openedOn",
        "lastUpdatedOn",
        "nrOfDocuments",
        "nrOfPendingTransactions"
})
public class Case {

    @XmlElement(required = true)
    protected String caseId;
    @XmlElement(required = true)
    protected String caseReference;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar openedOn;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastUpdatedOn;
    protected int nrOfDocuments;
    protected int nrOfPendingTransactions;

    /**
     * Gets the value of the caseId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * Sets the value of the caseId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCaseId(String value) {
        this.caseId = value;
    }

    /**
     * Gets the value of the caseReference property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCaseReference() {
        return caseReference;
    }

    /**
     * Sets the value of the caseReference property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCaseReference(String value) {
        this.caseReference = value;
    }

    /**
     * Gets the value of the openedOn property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getOpenedOn() {
        return openedOn;
    }

    /**
     * Sets the value of the openedOn property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setOpenedOn(XMLGregorianCalendar value) {
        this.openedOn = value;
    }

    /**
     * Gets the value of the lastUpdatedOn property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    /**
     * Sets the value of the lastUpdatedOn property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setLastUpdatedOn(XMLGregorianCalendar value) {
        this.lastUpdatedOn = value;
    }

    /**
     * Gets the value of the nrOfDocuments property.
     */
    public int getNrOfDocuments() {
        return nrOfDocuments;
    }

    /**
     * Sets the value of the nrOfDocuments property.
     */
    public void setNrOfDocuments(int value) {
        this.nrOfDocuments = value;
    }

    /**
     * Gets the value of the nrOfPendingTransactions property.
     */
    public int getNrOfPendingTransactions() {
        return nrOfPendingTransactions;
    }

    /**
     * Sets the value of the nrOfPendingTransactions property.
     */
    public void setNrOfPendingTransactions(int value) {
        this.nrOfPendingTransactions = value;
    }

}
