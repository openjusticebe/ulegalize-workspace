
package services.dpa.common.addressbook.external.consult.lawyer.v01.types;

import services.dpa.common.addressbook.base.v01.types.BillingOrganisation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * List of billing organisations of a lawyer
 *
 *
 * <p>Java class for BillingInfoList complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BillingInfoList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="billingInfo" type="{http://common.dpa.services/addressbook/base/v01/types}BillingOrganisation" maxOccurs="100" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BillingInfoList", propOrder = {
        "billingInfo"
})
public class BillingInfoList {

    protected List<BillingOrganisation> billingInfo;

    /**
     * Gets the value of the billingInfo property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the billingInfo property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBillingInfo().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BillingOrganisation }
     */
    public List<BillingOrganisation> getBillingInfo() {
        if (billingInfo == null) {
            billingInfo = new ArrayList<BillingOrganisation>();
        }
        return this.billingInfo;
    }

}
