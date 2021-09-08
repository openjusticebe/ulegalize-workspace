
package services.dpa.common.addressbook.external.consult.lawyer.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * List of lawyers
 *
 *
 * <p>Java class for LawyerList complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="LawyerList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lawyer" type="{http://common.dpa.services/addressbook/external/consult/lawyer/v01/types}Lawyer" maxOccurs="100" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LawyerList", propOrder = {
        "lawyer"
})
public class LawyerList {

    protected List<Lawyer> lawyer;

    /**
     * Gets the value of the lawyer property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lawyer property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLawyer().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Lawyer }
     */
    public List<Lawyer> getLawyer() {
        if (lawyer == null) {
            lawyer = new ArrayList<Lawyer>();
        }
        return this.lawyer;
    }

}
