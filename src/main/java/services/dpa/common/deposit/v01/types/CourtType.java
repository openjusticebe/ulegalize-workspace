
package services.dpa.common.deposit.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CourtType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CourtType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/deposit/v01/types}RecipientType">
 *       &lt;sequence>
 *         &lt;element name="courtID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CourtType", propOrder = {
        "courtID"
})
public class CourtType
        extends RecipientType {

    @XmlElement(required = true)
    protected String courtID;

    /**
     * Gets the value of the courtID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCourtID() {
        return courtID;
    }

    /**
     * Sets the value of the courtID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCourtID(String value) {
        this.courtID = value;
    }

}
