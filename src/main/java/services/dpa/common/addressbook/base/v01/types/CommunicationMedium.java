
package services.dpa.common.addressbook.base.v01.types;

import services.dpa.common.addressbook.base.v01.codes.CommunicationMediumType;

import javax.xml.bind.annotation.*;


/**
 * A communication medium for a person at an organisation
 *
 * <p>Java class for CommunicationMedium complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CommunicationMedium">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="communicationMediumType" type="{http://common.dpa.services/addressbook/base/v01/codes}CommunicationMediumType"/>
 *         &lt;element name="communicationMediumValue" type="{http://common.dpa.services/global/v01/types}StringMax250"/>
 *         &lt;element name="privateFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommunicationMedium", propOrder = {
        "communicationMediumType",
        "communicationMediumValue",
        "privateFlag"
})
public class CommunicationMedium {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected CommunicationMediumType communicationMediumType;
    @XmlElement(required = true)
    protected String communicationMediumValue;
    @XmlElement(defaultValue = "false")
    protected boolean privateFlag;

    /**
     * Gets the value of the communicationMediumType property.
     *
     * @return possible object is
     * {@link CommunicationMediumType }
     */
    public CommunicationMediumType getCommunicationMediumType() {
        return communicationMediumType;
    }

    /**
     * Sets the value of the communicationMediumType property.
     *
     * @param value allowed object is
     *              {@link CommunicationMediumType }
     */
    public void setCommunicationMediumType(CommunicationMediumType value) {
        this.communicationMediumType = value;
    }

    /**
     * Gets the value of the communicationMediumValue property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCommunicationMediumValue() {
        return communicationMediumValue;
    }

    /**
     * Sets the value of the communicationMediumValue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCommunicationMediumValue(String value) {
        this.communicationMediumValue = value;
    }

    /**
     * Gets the value of the privateFlag property.
     */
    public boolean isPrivateFlag() {
        return privateFlag;
    }

    /**
     * Sets the value of the privateFlag property.
     */
    public void setPrivateFlag(boolean value) {
        this.privateFlag = value;
    }

}
