
package services.dpa.common.deposit.v01.messages;

import services.dpa.common.deposit.v01.types.DepositResponseType;

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
 *         &lt;element name="response" type="{http://common.dpa.services/deposit/v01/types}DepositResponseType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "response"
})
@XmlRootElement(name = "DepositResponse")
public class DepositResponse {

    @XmlElement(required = true)
    protected DepositResponseType response;

    /**
     * Gets the value of the response property.
     *
     * @return possible object is
     * {@link DepositResponseType }
     */
    public DepositResponseType getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     *
     * @param value allowed object is
     *              {@link DepositResponseType }
     */
    public void setResponse(DepositResponseType value) {
        this.response = value;
    }

}
