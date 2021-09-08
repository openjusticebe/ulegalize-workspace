
package services.dpa.common.deposit.v01.messages;

import services.dpa.common.global.v01.messages.GeneralFault;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/global/v01/messages}GeneralFault">
 *       &lt;sequence>
 *         &lt;element name="person" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "person"
})
@XmlRootElement(name = "Deposit1Fault")
public class Deposit1Fault
        extends GeneralFault {

    @XmlElement(required = true)
    protected String person;

    /**
     * Gets the value of the person property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPerson() {
        return person;
    }

    /**
     * Sets the value of the person property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPerson(String value) {
        this.person = value;
    }

}
