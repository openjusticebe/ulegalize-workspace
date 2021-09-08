
package services.dpa.common.message.box.v01.messages;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element name="messageId" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *       &lt;/sequence>
 *       &lt;attribute name="languageCode" type="{http://www.w3.org/2001/XMLSchema}language" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "messageId"
})
@XmlRootElement(name = "getMessageByIdRequest")
public class GetMessageByIdRequest {

    @XmlElement(required = true)
    protected String messageId;
    @XmlAttribute(name = "languageCode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "language")
    protected String languageCode;

    /**
     * Gets the value of the messageId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMessageId(String value) {
        this.messageId = value;
    }

    /**
     * Gets the value of the languageCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * Sets the value of the languageCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLanguageCode(String value) {
        this.languageCode = value;
    }

}
