
package services.dpa.common.addressbook.external.consult.court.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * List of courtLawTypes
 *
 *
 * <p>Java class for CourtLawTypeList complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CourtLawTypeList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="courtLawType" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}CourtLawType" maxOccurs="100" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CourtLawTypeList", propOrder = {
        "courtLawType"
})
public class CourtLawTypeList {

    protected List<CourtLawType> courtLawType;

    /**
     * Gets the value of the courtLawType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the courtLawType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCourtLawType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourtLawType }
     */
    public List<CourtLawType> getCourtLawType() {
        if (courtLawType == null) {
            courtLawType = new ArrayList<CourtLawType>();
        }
        return this.courtLawType;
    }

}
