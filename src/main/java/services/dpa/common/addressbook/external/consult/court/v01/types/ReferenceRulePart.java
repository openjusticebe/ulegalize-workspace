
package services.dpa.common.addressbook.external.consult.court.v01.types;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for ReferenceRulePart complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ReferenceRulePart">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="severityLevel" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}SeverityLevel" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferenceRulePart")
@XmlSeeAlso({
        Year.class,
        NrFix.class,
        Alpha.class,
        NrSeq.class
})
public abstract class ReferenceRulePart {

    @XmlAttribute(name = "id", required = true)
    protected int id;
    @XmlAttribute(name = "severityLevel")
    protected SeverityLevel severityLevel;

    /**
     * Gets the value of the id property.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the severityLevel property.
     *
     * @return possible object is
     * {@link SeverityLevel }
     */
    public SeverityLevel getSeverityLevel() {
        return severityLevel;
    }

    /**
     * Sets the value of the severityLevel property.
     *
     * @param value allowed object is
     *              {@link SeverityLevel }
     */
    public void setSeverityLevel(SeverityLevel value) {
        this.severityLevel = value;
    }

}
