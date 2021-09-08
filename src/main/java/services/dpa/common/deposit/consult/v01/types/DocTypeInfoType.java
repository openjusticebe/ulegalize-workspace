
package services.dpa.common.deposit.consult.v01.types;

import services.dpa.common.deposit.base.v01.types.DocTypeType;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for DocTypeInfoType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DocTypeInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="docType" type="{http://common.dpa.services/deposit/base/v01/types}DocTypeType"/>
 *         &lt;element name="minOccurs" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="maxOccurs" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="printOrder" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocTypeInfoType", propOrder = {
        "docType",
        "minOccurs",
        "maxOccurs",
        "printOrder"
})
public class DocTypeInfoType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected DocTypeType docType;
    protected int minOccurs;
    protected int maxOccurs;
    protected Integer printOrder;

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
     * Gets the value of the minOccurs property.
     */
    public int getMinOccurs() {
        return minOccurs;
    }

    /**
     * Sets the value of the minOccurs property.
     */
    public void setMinOccurs(int value) {
        this.minOccurs = value;
    }

    /**
     * Gets the value of the maxOccurs property.
     */
    public int getMaxOccurs() {
        return maxOccurs;
    }

    /**
     * Sets the value of the maxOccurs property.
     */
    public void setMaxOccurs(int value) {
        this.maxOccurs = value;
    }

    /**
     * Gets the value of the printOrder property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getPrintOrder() {
        return printOrder;
    }

    /**
     * Sets the value of the printOrder property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setPrintOrder(Integer value) {
        this.printOrder = value;
    }

}
