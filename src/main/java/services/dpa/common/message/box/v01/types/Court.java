
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Court complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Court">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/message/box/v01/types}Recipient">
 *       &lt;sequence>
 *         &lt;element name="courtID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="courtName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Court", propOrder = {
        "courtID",
        "courtName"
})
public class Court
        extends Recipient {

    @XmlElement(required = true)
    protected String courtID;
    @XmlElement(required = true)
    protected String courtName;

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

    /**
     * Gets the value of the courtName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCourtName() {
        return courtName;
    }

    /**
     * Sets the value of the courtName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCourtName(String value) {
        this.courtName = value;
    }

}
