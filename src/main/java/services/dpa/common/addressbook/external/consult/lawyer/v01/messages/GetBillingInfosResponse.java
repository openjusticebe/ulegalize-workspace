
package services.dpa.common.addressbook.external.consult.lawyer.v01.messages;

import services.dpa.common.addressbook.external.consult.lawyer.v01.types.BillingInfoList;

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
 *         &lt;element name="billingInfos" type="{http://common.dpa.services/addressbook/external/consult/lawyer/v01/types}BillingInfoList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "billingInfos"
})
@XmlRootElement(name = "getBillingInfosResponse")
public class GetBillingInfosResponse {

    @XmlElement(required = true)
    protected BillingInfoList billingInfos;

    /**
     * Gets the value of the billingInfos property.
     *
     * @return possible object is
     * {@link BillingInfoList }
     */
    public BillingInfoList getBillingInfos() {
        return billingInfos;
    }

    /**
     * Sets the value of the billingInfos property.
     *
     * @param value allowed object is
     *              {@link BillingInfoList }
     */
    public void setBillingInfos(BillingInfoList value) {
        this.billingInfos = value;
    }

}
