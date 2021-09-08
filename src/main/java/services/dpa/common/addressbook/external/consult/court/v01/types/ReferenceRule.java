
package services.dpa.common.addressbook.external.consult.court.v01.types;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ReferenceRule complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ReferenceRule">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="3" minOccurs="0">
 *           &lt;element name="year" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}year"/>
 *           &lt;element name="alpha" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}alpha"/>
 *           &lt;element name="nrFix" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}nrFix"/>
 *           &lt;element name="nrSeq" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}nrSeq"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferenceRule", propOrder = {
        "yearOrAlphaOrNrFix"
})
public class ReferenceRule {

    @XmlElements({
            @XmlElement(name = "year", type = Year.class),
            @XmlElement(name = "alpha", type = Alpha.class),
            @XmlElement(name = "nrFix", type = NrFix.class),
            @XmlElement(name = "nrSeq", type = NrSeq.class)
    })
    protected List<ReferenceRulePart> yearOrAlphaOrNrFix;

    /**
     * Gets the value of the yearOrAlphaOrNrFix property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the yearOrAlphaOrNrFix property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getYearOrAlphaOrNrFix().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Year }
     * {@link Alpha }
     * {@link NrFix }
     * {@link NrSeq }
     */
    public List<ReferenceRulePart> getYearOrAlphaOrNrFix() {
        if (yearOrAlphaOrNrFix == null) {
            yearOrAlphaOrNrFix = new ArrayList<ReferenceRulePart>();
        }
        return this.yearOrAlphaOrNrFix;
    }

}
