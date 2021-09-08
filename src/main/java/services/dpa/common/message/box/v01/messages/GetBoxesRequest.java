
package services.dpa.common.message.box.v01.messages;

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
 *         &lt;element name="includeInactive" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *       &lt;attribute name="languagecode" type="{http://www.w3.org/2001/XMLSchema}language" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "includeInactive"
})
@XmlRootElement(name = "getBoxesRequest")
public class GetBoxesRequest {

    @XmlElement(defaultValue = "false")
    protected boolean includeInactive;
    @XmlAttribute(name = "languagecode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "language")
    protected String languagecode;

    /**
     * Gets the value of the includeInactive property.
     */
    public boolean isIncludeInactive() {
        return includeInactive;
    }

    /**
     * Sets the value of the includeInactive property.
     */
    public void setIncludeInactive(boolean value) {
        this.includeInactive = value;
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
