
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReceivedFrom complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ReceivedFrom">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="senderList" type="{http://common.dpa.services/message/box/v01/types}SenderList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReceivedFrom", propOrder = {
        "senderList"
})
public class ReceivedFrom {

    @XmlElement(required = true)
    protected SenderList senderList;

    /**
     * Gets the value of the senderList property.
     *
     * @return possible object is
     * {@link SenderList }
     */
    public SenderList getSenderList() {
        return senderList;
    }

    /**
     * Sets the value of the senderList property.
     *
     * @param value allowed object is
     *              {@link SenderList }
     */
    public void setSenderList(SenderList value) {
        this.senderList = value;
    }

}
