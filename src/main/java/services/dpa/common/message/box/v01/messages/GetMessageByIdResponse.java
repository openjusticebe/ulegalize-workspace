
package services.dpa.common.message.box.v01.messages;

import services.dpa.common.message.box.v01.types.Direction;
import services.dpa.common.message.box.v01.types.IncomingMessage;
import services.dpa.common.message.box.v01.types.OutgoingMessage;

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
 *         &lt;element name="direction" type="{http://common.dpa.services/message/box/v01/types}Direction"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="incomingMessage" type="{http://common.dpa.services/message/box/v01/types}IncomingMessage" minOccurs="0"/>
 *           &lt;element name="outgoingMessage" type="{http://common.dpa.services/message/box/v01/types}OutgoingMessage" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "direction",
        "incomingMessage",
        "outgoingMessage"
})
@XmlRootElement(name = "getMessageByIdResponse")
public class GetMessageByIdResponse {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected Direction direction;
    protected IncomingMessage incomingMessage;
    protected OutgoingMessage outgoingMessage;

    /**
     * Gets the value of the direction property.
     *
     * @return possible object is
     * {@link Direction }
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     *
     * @param value allowed object is
     *              {@link Direction }
     */
    public void setDirection(Direction value) {
        this.direction = value;
    }

    /**
     * Gets the value of the incomingMessage property.
     *
     * @return possible object is
     * {@link IncomingMessage }
     */
    public IncomingMessage getIncomingMessage() {
        return incomingMessage;
    }

    /**
     * Sets the value of the incomingMessage property.
     *
     * @param value allowed object is
     *              {@link IncomingMessage }
     */
    public void setIncomingMessage(IncomingMessage value) {
        this.incomingMessage = value;
    }

    /**
     * Gets the value of the outgoingMessage property.
     *
     * @return possible object is
     * {@link OutgoingMessage }
     */
    public OutgoingMessage getOutgoingMessage() {
        return outgoingMessage;
    }

    /**
     * Sets the value of the outgoingMessage property.
     *
     * @param value allowed object is
     *              {@link OutgoingMessage }
     */
    public void setOutgoingMessage(OutgoingMessage value) {
        this.outgoingMessage = value;
    }

}
