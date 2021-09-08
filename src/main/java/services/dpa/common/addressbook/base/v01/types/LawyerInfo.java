
package services.dpa.common.addressbook.base.v01.types;

import services.dpa.common.addressbook.base.v01.codes.LawyerStatus;
import services.dpa.common.addressbook.base.v01.codes.LawyerType;
import services.dpa.common.global.v01.codes.BarAssociation;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for LawyerInfo complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="LawyerInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="barAssociation" type="{http://common.dpa.services/global/v01/codes}BarAssociation"/>
 *         &lt;element name="lawyerStatus" type="{http://common.dpa.services/addressbook/base/v01/codes}LawyerStatus" minOccurs="0"/>
 *         &lt;element name="lawyerType" type="{http://common.dpa.services/addressbook/base/v01/codes}LawyerType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LawyerInfo", propOrder = {
        "barAssociation",
        "lawyerStatus",
        "lawyerType"
})
public class LawyerInfo {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected BarAssociation barAssociation;
    @XmlSchemaType(name = "string")
    protected LawyerStatus lawyerStatus;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected LawyerType lawyerType;

    /**
     * Gets the value of the barAssociation property.
     *
     * @return possible object is
     * {@link BarAssociation }
     */
    public BarAssociation getBarAssociation() {
        return barAssociation;
    }

    /**
     * Sets the value of the barAssociation property.
     *
     * @param value allowed object is
     *              {@link BarAssociation }
     */
    public void setBarAssociation(BarAssociation value) {
        this.barAssociation = value;
    }

    /**
     * Gets the value of the lawyerStatus property.
     *
     * @return possible object is
     * {@link LawyerStatus }
     */
    public LawyerStatus getLawyerStatus() {
        return lawyerStatus;
    }

    /**
     * Sets the value of the lawyerStatus property.
     *
     * @param value allowed object is
     *              {@link LawyerStatus }
     */
    public void setLawyerStatus(LawyerStatus value) {
        this.lawyerStatus = value;
    }

    /**
     * Gets the value of the lawyerType property.
     *
     * @return possible object is
     * {@link LawyerType }
     */
    public LawyerType getLawyerType() {
        return lawyerType;
    }

    /**
     * Sets the value of the lawyerType property.
     *
     * @param value allowed object is
     *              {@link LawyerType }
     */
    public void setLawyerType(LawyerType value) {
        this.lawyerType = value;
    }

}
