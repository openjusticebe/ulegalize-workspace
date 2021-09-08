
package services.dpa.common.addressbook.base.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * A list of communication media for a person at an organisation
 *
 * <p>Java class for CommunicationMediumList complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CommunicationMediumList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="communicationMedium" type="{http://common.dpa.services/addressbook/base/v01/types}CommunicationMedium" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommunicationMediumList", propOrder = {
        "communicationMedium"
})
public class CommunicationMediumList {

    protected List<CommunicationMedium> communicationMedium;

    /**
     * Gets the value of the communicationMedium property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the communicationMedium property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCommunicationMedium().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CommunicationMedium }
     */
    public List<CommunicationMedium> getCommunicationMedium() {
        if (communicationMedium == null) {
            communicationMedium = new ArrayList<CommunicationMedium>();
        }
        return this.communicationMedium;
    }

}
