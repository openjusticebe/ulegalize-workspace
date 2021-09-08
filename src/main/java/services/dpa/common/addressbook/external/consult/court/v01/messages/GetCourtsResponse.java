
package services.dpa.common.addressbook.external.consult.court.v01.messages;

import services.dpa.common.addressbook.external.consult.court.v01.types.CourtList;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="courts" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}CourtList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "courts"
})
@XmlRootElement(name = "getCourtsResponse")
public class GetCourtsResponse {

    @XmlElement(required = true)
    protected CourtList courts;

    /**
     * Gets the value of the courts property.
     *
     * @return possible object is
     * {@link CourtList }
     */
    public CourtList getCourts() {
        return courts;
    }

    /**
     * Sets the value of the courts property.
     *
     * @param value allowed object is
     *              {@link CourtList }
     */
    public void setCourts(CourtList value) {
        this.courts = value;
    }

}
