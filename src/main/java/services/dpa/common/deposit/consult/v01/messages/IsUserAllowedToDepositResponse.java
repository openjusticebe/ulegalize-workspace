
package services.dpa.common.deposit.consult.v01.messages;

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
 *         &lt;element name="isUserAllowedToDeposit" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "isUserAllowedToDeposit"
})
@XmlRootElement(name = "isUserAllowedToDepositResponse")
public class IsUserAllowedToDepositResponse {

    @XmlElement(defaultValue = "false")
    protected boolean isUserAllowedToDeposit;

    /**
     * Gets the value of the isUserAllowedToDeposit property.
     */
    public boolean isIsUserAllowedToDeposit() {
        return isUserAllowedToDeposit;
    }

    /**
     * Sets the value of the isUserAllowedToDeposit property.
     */
    public void setIsUserAllowedToDeposit(boolean value) {
        this.isUserAllowedToDeposit = value;
    }

}
