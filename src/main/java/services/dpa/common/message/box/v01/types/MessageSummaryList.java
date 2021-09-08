
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for MessageSummaryList complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MessageSummaryList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="incomingMessageSummary" type="{http://common.dpa.services/message/box/v01/types}IncomingMessageSummary"/>
 *           &lt;element name="outgoingMessageSummary" type="{http://common.dpa.services/message/box/v01/types}OutgoingMessageSummary"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageSummaryList", propOrder = {
        "incomingMessageSummaryOrOutgoingMessageSummary"
})
public class MessageSummaryList {

    @XmlElements({
            @XmlElement(name = "incomingMessageSummary", type = IncomingMessageSummary.class),
            @XmlElement(name = "outgoingMessageSummary", type = OutgoingMessageSummary.class)
    })
    protected List<MessageSummary> incomingMessageSummaryOrOutgoingMessageSummary;

    /**
     * Gets the value of the incomingMessageSummaryOrOutgoingMessageSummary property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the incomingMessageSummaryOrOutgoingMessageSummary property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncomingMessageSummaryOrOutgoingMessageSummary().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IncomingMessageSummary }
     * {@link OutgoingMessageSummary }
     */
    public List<MessageSummary> getIncomingMessageSummaryOrOutgoingMessageSummary() {
        if (incomingMessageSummaryOrOutgoingMessageSummary == null) {
            incomingMessageSummaryOrOutgoingMessageSummary = new ArrayList<MessageSummary>();
        }
        return this.incomingMessageSummaryOrOutgoingMessageSummary;
    }

}
