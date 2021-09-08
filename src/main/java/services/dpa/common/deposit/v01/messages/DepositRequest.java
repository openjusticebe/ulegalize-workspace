
package services.dpa.common.deposit.v01.messages;

import services.dpa.common.deposit.v01.types.DepositRequestType;

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
 *         &lt;element name="deposit" type="{http://common.dpa.services/deposit/v01/types}DepositRequestType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "deposit"
})
@XmlRootElement(name = "DepositRequest")
public class DepositRequest {

    @XmlElement(required = true)
    protected DepositRequestType deposit;

    /**
     * Gets the value of the deposit property.
     *
     * @return possible object is
     * {@link DepositRequestType }
     */
    public DepositRequestType getDeposit() {
        return deposit;
    }

    /**
     * Sets the value of the deposit property.
     *
     * @param value allowed object is
     *              {@link DepositRequestType }
     */
    public void setDeposit(DepositRequestType value) {
        this.deposit = value;
    }

}
