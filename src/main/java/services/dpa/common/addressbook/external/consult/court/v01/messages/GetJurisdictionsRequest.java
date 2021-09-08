
package services.dpa.common.addressbook.external.consult.court.v01.messages;

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
 *         &lt;element name="courtType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="languagecode" use="required" type="{http://www.w3.org/2001/XMLSchema}language" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "courtType"
})
@XmlRootElement(name = "getJurisdictionsRequest")
public class GetJurisdictionsRequest {

    protected Integer courtType;
    @XmlAttribute(name = "languagecode", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "language")
    protected String languagecode;

    /**
     * Gets the value of the courtType property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getCourtType() {
        return courtType;
    }

    /**
     * Sets the value of the courtType property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setCourtType(Integer value) {
        this.courtType = value;
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
