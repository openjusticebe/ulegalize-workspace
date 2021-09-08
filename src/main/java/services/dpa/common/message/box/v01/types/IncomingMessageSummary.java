
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for IncomingMessageSummary complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="IncomingMessageSummary">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/message/box/v01/types}MessageSummary">
 *       &lt;sequence>
 *         &lt;element name="received" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IncomingMessageSummary", propOrder = {
        "received"
})
public class IncomingMessageSummary
        extends MessageSummary {

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar received;

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
