
package services.dpa.common.addressbook.external.consult.court.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * List of courts
 *
 *
 * <p>Java class for CourtList complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CourtList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="court" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}Court" maxOccurs="100" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CourtList", propOrder = {
        "court"
})
public class CourtList {

    protected List<Court> court;

    /**
     * Gets the value of the court property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the court property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCourt().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Court }
     */
    public List<Court> getCourt() {
        if (court == null) {
            court = new ArrayList<Court>();
        }
        return this.court;
    }

}
