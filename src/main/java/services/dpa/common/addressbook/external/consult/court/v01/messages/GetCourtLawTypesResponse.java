
package services.dpa.common.addressbook.external.consult.court.v01.messages;

import services.dpa.common.addressbook.external.consult.court.v01.types.CourtLawTypeList;

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
 *         &lt;element name="courtLawTypes" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}CourtLawTypeList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "courtLawTypes"
})
@XmlRootElement(name = "getCourtLawTypesResponse")
public class GetCourtLawTypesResponse {

    @XmlElement(required = true)
    protected CourtLawTypeList courtLawTypes;

    /**
     * Gets the value of the courtLawTypes property.
     *
     * @return possible object is
     * {@link CourtLawTypeList }
     */
    public CourtLawTypeList getCourtLawTypes() {
        return courtLawTypes;
    }

    /**
     * Sets the value of the courtLawTypes property.
     *
     * @param value allowed object is
     *              {@link CourtLawTypeList }
     */
    public void setCourtLawTypes(CourtLawTypeList value) {
        this.courtLawTypes = value;
    }

}
