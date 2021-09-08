
package services.dpa.common.message.box.v01.messages;

import services.dpa.common.message.box.v01.types.CaseDocumentList;

import javax.xml.bind.annotation.*;


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
 *         &lt;element name="caseDocuments" type="{http://common.dpa.services/message/box/v01/types}CaseDocumentList"/>
 *         &lt;element name="limit" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="offset" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="size" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "caseDocuments",
        "limit",
        "offset",
        "size"
})
@XmlRootElement(name = "getCaseDocumentsResponse")
public class GetCaseDocumentsResponse {

    @XmlElement(required = true)
    protected CaseDocumentList caseDocuments;
    protected Integer limit;
    protected Integer offset;
    protected Integer size;

    /**
     * Gets the value of the caseDocuments property.
     *
     * @return possible object is
     * {@link CaseDocumentList }
     */
    public CaseDocumentList getCaseDocuments() {
        return caseDocuments;
    }

    /**
     * Sets the value of the caseDocuments property.
     *
     * @param value allowed object is
     *              {@link CaseDocumentList }
     */
    public void setCaseDocuments(CaseDocumentList value) {
        this.caseDocuments = value;
    }

    /**
     * Gets the value of the limit property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Sets the value of the limit property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setLimit(Integer value) {
        this.limit = value;
    }

    /**
     * Gets the value of the offset property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * Sets the value of the offset property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setOffset(Integer value) {
        this.offset = value;
    }

    /**
     * Gets the value of the size property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setSize(Integer value) {
        this.size = value;
    }

}
