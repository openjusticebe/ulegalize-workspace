
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BillingInfo complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BillingInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="billingId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="billingName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billingEnterpriseNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BillingInfo", propOrder = {
        "billingId",
        "billingName",
        "billingEnterpriseNumber"
})
public class BillingInfo {

    protected int billingId;
    @XmlElement(required = true)
    protected String billingName;
    @XmlElement(required = true)
    protected String billingEnterpriseNumber;

    /**
     * Gets the value of the billingId property.
     */
    public int getBillingId() {
        return billingId;
    }

    /**
     * Sets the value of the billingId property.
     */
    public void setBillingId(int value) {
        this.billingId = value;
    }

    /**
     * Gets the value of the billingName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBillingName() {
        return billingName;
    }

    /**
     * Sets the value of the billingName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBillingName(String value) {
        this.billingName = value;
    }

    /**
     * Gets the value of the billingEnterpriseNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBillingEnterpriseNumber() {
        return billingEnterpriseNumber;
    }

    /**
     * Sets the value of the billingEnterpriseNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBillingEnterpriseNumber(String value) {
        this.billingEnterpriseNumber = value;
    }

}
