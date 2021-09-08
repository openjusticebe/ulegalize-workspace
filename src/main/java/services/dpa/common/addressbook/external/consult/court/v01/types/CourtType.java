
package services.dpa.common.addressbook.external.consult.court.v01.types;

import services.dpa.common.global.v01.types.CodeType;

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
 *     &lt;extension base="{http://common.dpa.services/global/v01/types}CodeType">
 *       &lt;sequence>
 *         &lt;element name="courtLawType" type="{http://common.dpa.services/global/v01/types}StringMax20"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CourtType", propOrder = {
        "courtLawType"
})
public class CourtType
        extends CodeType {

    @XmlElement(required = true)
    protected String courtLawType;

    /**
     * Gets the value of the courtLawType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCourtLawType() {
        return courtLawType;
    }

    /**
     * Sets the value of the courtLawType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCourtLawType(String value) {
        this.courtLawType = value;
    }

}
