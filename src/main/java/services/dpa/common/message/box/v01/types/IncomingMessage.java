
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for IncomingMessage complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="IncomingMessage">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/message/box/v01/types}Message">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="court" type="{http://common.dpa.services/message/box/v01/types}Court" minOccurs="0"/>
 *           &lt;element name="lawyer" type="{http://common.dpa.services/message/box/v01/types}Lawyer" minOccurs="0"/>
 *           &lt;element name="thirdParty" type="{http://common.dpa.services/message/box/v01/types}ThirdParty" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element name="received" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IncomingMessage", propOrder = {
        "court",
        "lawyer",
        "thirdParty",
        "received"
})
public class IncomingMessage
        extends Message {

    protected Court court;
    protected Lawyer lawyer;
    protected ThirdParty thirdParty;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar received;

    /**
     * Gets the value of the court property.
     *
     * @return possible object is
     * {@link Court }
     */
    public Court getCourt() {
        return court;
    }

    /**
     * Sets the value of the court property.
     *
     * @param value allowed object is
     *              {@link Court }
     */
    public void setCourt(Court value) {
        this.court = value;
    }

    /**
     * Gets the value of the lawyer property.
     *
     * @return possible object is
     * {@link Lawyer }
     */
    public Lawyer getLawyer() {
        return lawyer;
    }

    /**
     * Sets the value of the lawyer property.
     *
     * @param value allowed object is
     *              {@link Lawyer }
     */
    public void setLawyer(Lawyer value) {
        this.lawyer = value;
    }

    /**
     * Gets the value of the thirdParty property.
     *
     * @return possible object is
     * {@link ThirdParty }
     */
    public ThirdParty getThirdParty() {
        return thirdParty;
    }

    /**
     * Sets the value of the thirdParty property.
     *
     * @param value allowed object is
     *              {@link ThirdParty }
     */
    public void setThirdParty(ThirdParty value) {
        this.thirdParty = value;
    }

    /**
     * Gets the value of the received property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getReceived() {
        return received;
    }

    /**
     * Sets the value of the received property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setReceived(XMLGregorianCalendar value) {
        this.received = value;
    }

}
