
package services.dpa.common.addressbook.external.consult.court.v01.messages;

import services.dpa.common.addressbook.external.consult.court.v01.types.Court;

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
 *         &lt;element name="court" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}Court"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "court"
})
@XmlRootElement(name = "getCourtResponse")
public class GetCourtResponse {

    @XmlElement(required = true)
    protected Court court;

    /**
     * Gets the value of the court property.
     *
     * @return possible object is
     * {@link Court }
     */
    public Court getCourt() {
        return court;
    }

    /**
     * Sets the value of the court property.
     *
     * @param value allowed object is
     *              {@link Court }
     */
    public void setCourt(Court value) {
        this.court = value;
    }

}
