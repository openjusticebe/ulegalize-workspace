
package services.dpa.common.deposit.price.v01.messages;

import services.dpa.common.deposit.price.v01.types.PriceResponseType;

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
 *         &lt;element name="response" type="{http://common.dpa.services/deposit/price/v01/types}PriceResponseType"/>
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
@XmlRootElement(name = "PriceResponse")
public class PriceResponse {

    @XmlElement(required = true)
    protected PriceResponseType response;

    /**
     * Gets the value of the response property.
     *
     * @return possible object is
     * {@link PriceResponseType }
     */
    public PriceResponseType getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     *
     * @param value allowed object is
     *              {@link PriceResponseType }
     */
    public void setResponse(PriceResponseType value) {
        this.response = value;
    }

}
