
package services.dpa.common.deposit.consult.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Lawyer complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Lawyer">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/addressbook/external/consult/lawyer/v01/types}Lawyer">
 *       &lt;sequence>
 *         &lt;element name="channelInfos" type="{http://common.dpa.services/deposit/consult/v01/types}ChannelInfoListType"/>
 *         &lt;element name="allowedToDeposit" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Lawyer", propOrder = {
        "channelInfos",
        "allowedToDeposit"
})
public class Lawyer
        extends services.dpa.common.addressbook.external.consult.lawyer.v01.types.Lawyer {

    @XmlElement(required = true)
    protected ChannelInfoListType channelInfos;
    protected boolean allowedToDeposit;

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

    /**
     * Gets the value of the allowedToDeposit property.
     */
    public boolean isAllowedToDeposit() {
        return allowedToDeposit;
    }

    /**
     * Sets the value of the allowedToDeposit property.
     */
    public void setAllowedToDeposit(boolean value) {
        this.allowedToDeposit = value;
    }

}
