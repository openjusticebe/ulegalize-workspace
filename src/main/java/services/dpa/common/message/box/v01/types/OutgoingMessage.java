
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutgoingMessage complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="OutgoingMessage">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/message/box/v01/types}Message">
 *       &lt;sequence>
 *         &lt;element name="recipientList" type="{http://common.dpa.services/message/box/v01/types}RecipientList"/>
 *         &lt;element name="billingInfo" type="{http://common.dpa.services/message/box/v01/types}BillingInfo"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutgoingMessage", propOrder = {
        "recipientList",
        "billingInfo"
})
public class OutgoingMessage
        extends Message {

    @XmlElement(required = true)
    protected RecipientList recipientList;
    @XmlElement(required = true)
    protected BillingInfo billingInfo;

    /**
     * Gets the value of the recipientList property.
     *
     * @return possible object is
     * {@link RecipientList }
     */
    public RecipientList getRecipientList() {
        return recipientList;
    }

    /**
     * Sets the value of the recipientList property.
     *
     * @param value allowed object is
     *              {@link RecipientList }
     */
    public void setRecipientList(RecipientList value) {
        this.recipientList = value;
    }

    /**
     * Gets the value of the billingInfo property.
     *
     * @return possible object is
     * {@link BillingInfo }
     */
    public BillingInfo getBillingInfo() {
        return billingInfo;
    }

    /**
     * Sets the value of the billingInfo property.
     *
     * @param value allowed object is
     *              {@link BillingInfo }
     */
    public void setBillingInfo(BillingInfo value) {
        this.billingInfo = value;
    }

}
