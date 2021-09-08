
package services.dpa.common.addressbook.external.consult.court.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * List of courtTypes
 *
 *
 * <p>Java class for CourtTypeList complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CourtTypeList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="courtType" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}CourtType" maxOccurs="100" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CourtTypeList", propOrder = {
        "courtType"
})
public class CourtTypeList {

    protected List<CourtType> courtType;

    /**
     * Gets the value of the courtType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the courtType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCourtType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourtType }
     */
    public List<CourtType> getCourtType() {
        if (courtType == null) {
            courtType = new ArrayList<CourtType>();
        }
        return this.courtType;
    }

}
