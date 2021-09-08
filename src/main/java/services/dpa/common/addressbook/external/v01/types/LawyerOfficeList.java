
package services.dpa.common.addressbook.external.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * A list of lawyerOffices
 *
 * <p>Java class for LawyerOfficeList complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="LawyerOfficeList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lawyerOffice" type="{http://common.dpa.services/addressbook/external/v01/types}LawyerOffice" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LawyerOfficeList", propOrder = {
        "lawyerOffice"
})
public class LawyerOfficeList {

    protected List<LawyerOffice> lawyerOffice;

    /**
     * Gets the value of the lawyerOffice property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lawyerOffice property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLawyerOffice().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LawyerOffice }
     */
    public List<LawyerOffice> getLawyerOffice() {
        if (lawyerOffice == null) {
            lawyerOffice = new ArrayList<LawyerOffice>();
        }
        return this.lawyerOffice;
    }

}
