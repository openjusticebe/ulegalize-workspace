
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Sender complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Sender">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="person" type="{http://common.dpa.services/message/box/v01/types}SenderPerson" minOccurs="0"/>
 *         &lt;element name="organisation" type="{http://common.dpa.services/message/box/v01/types}SenderOrganisation"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sender", propOrder = {
        "person",
        "organisation"
})
public class Sender {

    protected SenderPerson person;
    @XmlElement(required = true)
    protected SenderOrganisation organisation;

    /**
     * Gets the value of the person property.
     *
     * @return possible object is
     * {@link SenderPerson }
     */
    public SenderPerson getPerson() {
        return person;
    }

    /**
     * Sets the value of the person property.
     *
     * @param value allowed object is
     *              {@link SenderPerson }
     */
    public void setPerson(SenderPerson value) {
        this.person = value;
    }

    /**
     * Gets the value of the organisation property.
     *
     * @return possible object is
     * {@link SenderOrganisation }
     */
    public SenderOrganisation getOrganisation() {
        return organisation;
    }

    /**
     * Sets the value of the organisation property.
     *
     * @param value allowed object is
     *              {@link SenderOrganisation }
     */
    public void setOrganisation(SenderOrganisation value) {
        this.organisation = value;
    }

}
