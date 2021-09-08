
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for RecipientList complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RecipientList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="court" type="{http://common.dpa.services/message/box/v01/types}Court" minOccurs="0"/>
 *           &lt;element name="lawyer" type="{http://common.dpa.services/message/box/v01/types}Lawyer" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="thirdParty" type="{http://common.dpa.services/message/box/v01/types}ThirdParty" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecipientList", propOrder = {
        "courtOrLawyerOrThirdParty"
})
public class RecipientList {

    @XmlElements({
            @XmlElement(name = "court", type = Court.class),
            @XmlElement(name = "lawyer", type = Lawyer.class),
            @XmlElement(name = "thirdParty", type = ThirdParty.class)
    })
    protected List<Recipient> courtOrLawyerOrThirdParty;

    /**
     * Gets the value of the courtOrLawyerOrThirdParty property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the courtOrLawyerOrThirdParty property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCourtOrLawyerOrThirdParty().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Court }
     * {@link Lawyer }
     * {@link ThirdParty }
     */
    public List<Recipient> getCourtOrLawyerOrThirdParty() {
        if (courtOrLawyerOrThirdParty == null) {
            courtOrLawyerOrThirdParty = new ArrayList<Recipient>();
        }
        return this.courtOrLawyerOrThirdParty;
    }

}
