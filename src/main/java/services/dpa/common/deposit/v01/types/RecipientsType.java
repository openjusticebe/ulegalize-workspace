
package services.dpa.common.deposit.v01.types;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for RecipientsType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RecipientsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="court" type="{http://common.dpa.services/deposit/v01/types}CourtType" minOccurs="0"/>
 *           &lt;element name="lawyer" type="{http://common.dpa.services/deposit/v01/types}LawyerType" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="thirdParty" type="{http://common.dpa.services/deposit/v01/types}ThirdPartyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecipientsType", propOrder = {
        "courtOrLawyerOrThirdParty"
})
public class RecipientsType {

    @XmlElements({
            @XmlElement(name = "court", type = CourtType.class),
            @XmlElement(name = "lawyer", type = LawyerType.class),
            @XmlElement(name = "thirdParty", type = ThirdPartyType.class)
    })
    protected List<RecipientType> courtOrLawyerOrThirdParty;

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
     * {@link CourtType }
     * {@link LawyerType }
     * {@link ThirdPartyType }
     */
    public List<RecipientType> getCourtOrLawyerOrThirdParty() {
        if (courtOrLawyerOrThirdParty == null) {
            courtOrLawyerOrThirdParty = new ArrayList<RecipientType>();
        }
        return this.courtOrLawyerOrThirdParty;
    }

}
