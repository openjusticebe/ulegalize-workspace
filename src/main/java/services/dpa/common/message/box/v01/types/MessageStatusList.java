
package services.dpa.common.message.box.v01.types;

import services.dpa.common.message.base.v01.codes.MessageState;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for MessageStatusList complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MessageStatusList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="messageStatus" type="{http://common.dpa.services/message/base/v01/codes}MessageState" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageStatusList", propOrder = {
        "messageStatus"
})
public class MessageStatusList {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected List<MessageState> messageStatus;

    /**
     * Gets the value of the messageStatus property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageStatus property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageStatus().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageState }
     */
    public List<MessageState> getMessageStatus() {
        if (messageStatus == null) {
            messageStatus = new ArrayList<MessageState>();
        }
        return this.messageStatus;
    }

}
