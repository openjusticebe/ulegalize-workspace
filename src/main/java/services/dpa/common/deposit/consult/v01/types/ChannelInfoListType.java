
package services.dpa.common.deposit.consult.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ChannelInfoListType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ChannelInfoListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="channelInfo" type="{http://common.dpa.services/deposit/consult/v01/types}ChannelInfoType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChannelInfoListType", propOrder = {
        "channelInfo"
})
public class ChannelInfoListType {

    @XmlElement(required = true)
    protected List<ChannelInfoType> channelInfo;

    /**
     * Gets the value of the channelInfo property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the channelInfo property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChannelInfo().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ChannelInfoType }
     */
    public List<ChannelInfoType> getChannelInfo() {
        if (channelInfo == null) {
            channelInfo = new ArrayList<ChannelInfoType>();
        }
        return this.channelInfo;
    }

}
