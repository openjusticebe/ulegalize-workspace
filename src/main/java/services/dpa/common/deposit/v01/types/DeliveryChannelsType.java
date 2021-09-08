
package services.dpa.common.deposit.v01.types;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for DeliveryChannelsType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DeliveryChannelsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="edeposit" type="{http://common.dpa.services/deposit/v01/types}EdepositType" minOccurs="0"/>
 *           &lt;element name="letter" type="{http://common.dpa.services/deposit/v01/types}LetterType" minOccurs="0"/>
 *           &lt;element name="fax" type="{http://common.dpa.services/deposit/v01/types}FaxType" minOccurs="0"/>
 *           &lt;element name="dpabox" type="{http://common.dpa.services/deposit/v01/types}DPAboxType" minOccurs="0"/>
 *           &lt;element name="email" type="{http://common.dpa.services/deposit/v01/types}EmailType" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeliveryChannelsType", propOrder = {
        "edepositOrLetterOrFax"
})
public class DeliveryChannelsType {

    @XmlElements({
            @XmlElement(name = "edeposit", type = EdepositType.class),
            @XmlElement(name = "letter", type = LetterType.class),
            @XmlElement(name = "fax", type = FaxType.class),
            @XmlElement(name = "dpabox", type = DPAboxType.class),
            @XmlElement(name = "email", type = EmailType.class)
    })
    protected List<DeliveryChannelType> edepositOrLetterOrFax;

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
     * {@link EdepositType }
     * {@link LetterType }
     * {@link FaxType }
     * {@link DPAboxType }
     * {@link EmailType }
     */
    public List<DeliveryChannelType> getEdepositOrLetterOrFax() {
        if (edepositOrLetterOrFax == null) {
            edepositOrLetterOrFax = new ArrayList<DeliveryChannelType>();
        }
        return this.edepositOrLetterOrFax;
    }

}
