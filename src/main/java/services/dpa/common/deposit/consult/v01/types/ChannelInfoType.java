
package services.dpa.common.deposit.consult.v01.types;

import services.dpa.common.deposit.base.v01.types.ChannelTypeType;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for ChannelInfoType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ChannelInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="channel" type="{http://common.dpa.services/deposit/base/v01/types}ChannelTypeType"/>
 *         &lt;element name="docTypeInfoList" type="{http://common.dpa.services/deposit/consult/v01/types}DocTypeInfoListType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChannelInfoType", propOrder = {
        "channel",
        "docTypeInfoList"
})
public class ChannelInfoType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ChannelTypeType channel;
    @XmlElement(required = true)
    protected DocTypeInfoListType docTypeInfoList;

    /**
     * Gets the value of the channel property.
     *
     * @return possible object is
     * {@link ChannelTypeType }
     */
    public ChannelTypeType getChannel() {
        return channel;
    }

    /**
     * Sets the value of the channel property.
     *
     * @param value allowed object is
     *              {@link ChannelTypeType }
     */
    public void setChannel(ChannelTypeType value) {
        this.channel = value;
    }

    /**
     * Gets the value of the docTypeInfoList property.
     *
     * @return possible object is
     * {@link DocTypeInfoListType }
     */
    public DocTypeInfoListType getDocTypeInfoList() {
        return docTypeInfoList;
    }

    /**
     * Sets the value of the docTypeInfoList property.
     *
     * @param value allowed object is
     *              {@link DocTypeInfoListType }
     */
    public void setDocTypeInfoList(DocTypeInfoListType value) {
        this.docTypeInfoList = value;
    }

}
