
package services.dpa.common.deposit.v01.types;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for DeliveryChannelType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DeliveryChannelType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contentRefs" type="{http://common.dpa.services/deposit/v01/types}ContentRefsType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="channelID" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeliveryChannelType", propOrder = {
        "contentRefs"
})
@XmlSeeAlso({
        EmailType.class,
        FaxType.class,
        LetterType.class,
        DPAboxType.class,
        EdepositType.class
})
public abstract class DeliveryChannelType {

    @XmlElement(required = true)
    protected ContentRefsType contentRefs;
    @XmlAttribute(name = "channelID", required = true)
    protected int channelID;

    /**
     * Gets the value of the contentRefs property.
     *
     * @return possible object is
     * {@link ContentRefsType }
     */
    public ContentRefsType getContentRefs() {
        return contentRefs;
    }

    /**
     * Sets the value of the contentRefs property.
     *
     * @param value allowed object is
     *              {@link ContentRefsType }
     */
    public void setContentRefs(ContentRefsType value) {
        this.contentRefs = value;
    }

    /**
     * Gets the value of the channelID property.
     */
    public int getChannelID() {
        return channelID;
    }

    /**
     * Sets the value of the channelID property.
     */
    public void setChannelID(int value) {
        this.channelID = value;
    }

}
