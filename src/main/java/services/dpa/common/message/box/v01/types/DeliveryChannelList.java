
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for DeliveryChannelList complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DeliveryChannelList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="edeposit" type="{http://common.dpa.services/message/box/v01/types}Edeposit" minOccurs="0"/>
 *           &lt;element name="letter" type="{http://common.dpa.services/message/box/v01/types}Letter" minOccurs="0"/>
 *           &lt;element name="fax" type="{http://common.dpa.services/message/box/v01/types}Fax" minOccurs="0"/>
 *           &lt;element name="dpabox" type="{http://common.dpa.services/message/box/v01/types}DPAbox" minOccurs="0"/>
 *           &lt;element name="email" type="{http://common.dpa.services/message/box/v01/types}Email" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeliveryChannelList", propOrder = {
        "edepositOrLetterOrFax"
})
public class DeliveryChannelList {

    @XmlElements({
            @XmlElement(name = "edeposit", type = Edeposit.class),
            @XmlElement(name = "letter", type = Letter.class),
            @XmlElement(name = "fax", type = Fax.class),
            @XmlElement(name = "dpabox", type = DPAbox.class),
            @XmlElement(name = "email", type = Email.class)
    })
    protected List<DeliveryChannel> edepositOrLetterOrFax;

    /**
     * Gets the value of the edepositOrLetterOrFax property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the edepositOrLetterOrFax property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEdepositOrLetterOrFax().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Edeposit }
     * {@link Letter }
     * {@link Fax }
     * {@link DPAbox }
     * {@link Email }
     */
    public List<DeliveryChannel> getEdepositOrLetterOrFax() {
        if (edepositOrLetterOrFax == null) {
            edepositOrLetterOrFax = new ArrayList<DeliveryChannel>();
        }
        return this.edepositOrLetterOrFax;
    }

}
