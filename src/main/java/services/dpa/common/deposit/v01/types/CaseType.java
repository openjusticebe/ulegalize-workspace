
package services.dpa.common.deposit.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CaseType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CaseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="senderReference" type="{http://common.dpa.services/global/v01/types}StringMax64"/>
 *         &lt;element name="proDeoReference" type="{http://common.dpa.services/global/v01/types}StringMax64" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CaseType", propOrder = {
        "senderReference",
        "proDeoReference"
})
public class CaseType {

    @XmlElement(required = true)
    protected String senderReference;
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
