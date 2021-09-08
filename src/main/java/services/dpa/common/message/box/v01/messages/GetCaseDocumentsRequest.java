
package services.dpa.common.message.box.v01.messages;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
 *         &lt;element name="caseId" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *       &lt;/sequence>
 *       &lt;attribute name="languageCode" type="{http://www.w3.org/2001/XMLSchema}language" />
 *       &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="limit" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "boxId",
        "caseId"
})
@XmlRootElement(name = "getCaseDocumentsRequest")
public class GetCaseDocumentsRequest {

    @XmlElement(required = true)
    protected String boxId;
    @XmlElement(required = true)
    protected String caseId;
    @XmlAttribute(name = "languageCode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "language")
    protected String languageCode;
    @XmlAttribute(name = "offset")
    protected BigInteger offset;
    @XmlAttribute(name = "limit")
    protected BigInteger limit;

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

}
