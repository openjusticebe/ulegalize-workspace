
package services.dpa.common.addressbook.external.consult.court.v01.messages;

import services.dpa.common.addressbook.external.consult.court.v01.types.CourtTypeList;

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
 *         &lt;element name="courtTypes" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}CourtTypeList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "courtTypes"
})
@XmlRootElement(name = "getCourtTypesResponse")
public class GetCourtTypesResponse {

    @XmlElement(required = true)
    protected CourtTypeList courtTypes;

    /**
     * Gets the value of the courtTypes property.
     *
     * @return possible object is
     * {@link CourtTypeList }
     */
    public CourtTypeList getCourtTypes() {
        return courtTypes;
    }

    /**
     * Sets the value of the courtTypes property.
     *
     * @param value allowed object is
     *              {@link CourtTypeList }
     */
    public void setCourtTypes(CourtTypeList value) {
        this.courtTypes = value;
    }

}
