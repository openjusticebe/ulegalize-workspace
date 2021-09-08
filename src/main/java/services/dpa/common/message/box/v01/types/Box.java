
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for Box complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Box">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="boxId" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="owner" type="{http://common.dpa.services/message/box/v01/types}BoxOwner"/>
 *       &lt;/sequence>
 *       &lt;attribute name="active" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="personal" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Box", propOrder = {
        "boxId",
        "owner"
})
public class Box {

    @XmlElement(required = true)
    protected String boxId;
    @XmlElement(required = true)
    protected BoxOwner owner;
    @XmlAttribute(name = "active")
    protected Boolean active;
    @XmlAttribute(name = "personal")
    protected Boolean personal;

    /**
     * Gets the value of the boxId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBoxId() {
        return boxId;
    }

    /**
     * Sets the value of the boxId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBoxId(String value) {
        this.boxId = value;
    }

    /**
     * Gets the value of the owner property.
     *
     * @return possible object is
     * {@link BoxOwner }
     */
    public BoxOwner getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     *
     * @param value allowed object is
     *              {@link BoxOwner }
     */
    public void setOwner(BoxOwner value) {
        this.owner = value;
    }

    /**
     * Gets the value of the active property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setActive(Boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the personal property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isPersonal() {
        return personal;
    }

    /**
     * Sets the value of the personal property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setPersonal(Boolean value) {
        this.personal = value;
    }

}
