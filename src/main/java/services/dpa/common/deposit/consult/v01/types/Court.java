
package services.dpa.common.deposit.consult.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Court complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Court">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/addressbook/external/consult/court/v01/types}Court">
 *       &lt;sequence>
 *         &lt;element name="channelInfos" type="{http://common.dpa.services/deposit/consult/v01/types}ChannelInfoListType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Court", propOrder = {
        "channelInfos"
})
public class Court
        extends services.dpa.common.addressbook.external.consult.court.v01.types.Court {

    @XmlElement(required = true)
    protected ChannelInfoListType channelInfos;

    /**
     * Gets the value of the channelInfos property.
     *
     * @return possible object is
     * {@link ChannelInfoListType }
     */
    public ChannelInfoListType getChannelInfos() {
        return channelInfos;
    }

    /**
     * Sets the value of the channelInfos property.
     *
     * @param value allowed object is
     *              {@link ChannelInfoListType }
     */
    public void setChannelInfos(ChannelInfoListType value) {
        this.channelInfos = value;
    }

}
