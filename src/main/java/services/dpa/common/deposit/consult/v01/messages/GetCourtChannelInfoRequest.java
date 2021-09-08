
package services.dpa.common.deposit.consult.v01.messages;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element name="courtID" type="{http://common.dpa.services/global/v01/types}OrganisationID"/>
 *       &lt;/sequence>
 *       &lt;attribute name="languagecode" use="required" type="{http://www.w3.org/2001/XMLSchema}language" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "courtID"
})
@XmlRootElement(name = "getCourtChannelInfoRequest")
public class GetCourtChannelInfoRequest {

    @XmlElement(required = true)
    protected String courtID;
    @XmlAttribute(name = "languagecode", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "language")
    protected String languagecode;

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
     * Gets the value of the languagecode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLanguagecode() {
        return languagecode;
    }

    /**
     * Sets the value of the languagecode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLanguagecode(String value) {
        this.languagecode = value;
    }

}
