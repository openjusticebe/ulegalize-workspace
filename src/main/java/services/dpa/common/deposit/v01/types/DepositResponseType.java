
package services.dpa.common.deposit.v01.types;

import services.dpa.common.addressbook.base.v01.types.BillingOrganisation;
import services.dpa.common.deposit.base.v01.types.BaseResponseType;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for DepositResponseType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DepositResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/deposit/base/v01/types}BaseResponseType">
 *       &lt;sequence>
 *         &lt;element name="depositID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="billingInfo" type="{http://common.dpa.services/addressbook/base/v01/types}BillingOrganisation"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DepositResponseType", propOrder = {
        "depositID",
        "timestamp",
        "billingInfo"
})
public class DepositResponseType
        extends BaseResponseType {

    @XmlElement(required = true)
    protected String depositID;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timestamp;
    @XmlElement(required = true)
    protected BillingOrganisation billingInfo;

    /**
     * Gets the value of the depositID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDepositID() {
        return depositID;
    }

    /**
     * Sets the value of the depositID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDepositID(String value) {
        this.depositID = value;
    }

    /**
     * Gets the value of the timestamp property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setTimestamp(XMLGregorianCalendar value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the billingInfo property.
     *
     * @return possible object is
     * {@link BillingOrganisation }
     */
    public BillingOrganisation getBillingInfo() {
        return billingInfo;
    }

    /**
     * Sets the value of the billingInfo property.
     *
     * @param value allowed object is
     *              {@link BillingOrganisation }
     */
    public void setBillingInfo(BillingOrganisation value) {
        this.billingInfo = value;
    }

}
