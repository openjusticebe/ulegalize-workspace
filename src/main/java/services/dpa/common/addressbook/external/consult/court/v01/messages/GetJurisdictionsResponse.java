
package services.dpa.common.addressbook.external.consult.court.v01.messages;

import services.dpa.common.addressbook.external.consult.court.v01.types.JurisdictionList;

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
 *         &lt;element name="jurisdictions" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}JurisdictionList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "jurisdictions"
})
@XmlRootElement(name = "getJurisdictionsResponse")
public class GetJurisdictionsResponse {

    @XmlElement(required = true)
    protected JurisdictionList jurisdictions;

    /**
     * Gets the value of the jurisdictions property.
     *
     * @return possible object is
     * {@link JurisdictionList }
     */
    public JurisdictionList getJurisdictions() {
        return jurisdictions;
    }

    /**
     * Sets the value of the jurisdictions property.
     *
     * @param value allowed object is
     *              {@link JurisdictionList }
     */
    public void setJurisdictions(JurisdictionList value) {
        this.jurisdictions = value;
    }

}
