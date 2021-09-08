
package services.dpa.common.addressbook.external.consult.lawyer.v01.types;

import services.dpa.common.addressbook.base.v01.types.LawyerInfo;
import services.dpa.common.addressbook.base.v01.types.Person;
import services.dpa.common.addressbook.external.v01.types.LawyerOfficeList;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for Lawyer complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Lawyer">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/addressbook/base/v01/types}Person">
 *       &lt;sequence>
 *         &lt;element name="lawyerInfo" type="{http://common.dpa.services/addressbook/base/v01/types}LawyerInfo"/>
 *         &lt;element name="lawyerOffices" type="{http://common.dpa.services/addressbook/external/v01/types}LawyerOfficeList"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Lawyer", propOrder = {
        "lawyerInfo",
        "lawyerOffices"
})
@XmlSeeAlso({
        services.dpa.common.deposit.consult.v01.types.Lawyer.class
})
public class Lawyer
        extends Person {

    @XmlElement(required = true)
    protected LawyerInfo lawyerInfo;
    @XmlElement(required = true)
    protected LawyerOfficeList lawyerOffices;

    /**
     * Gets the value of the lawyerInfo property.
     *
     * @return possible object is
     * {@link LawyerInfo }
     */
    public LawyerInfo getLawyerInfo() {
        return lawyerInfo;
    }

    /**
     * Sets the value of the lawyerInfo property.
     *
     * @param value allowed object is
     *              {@link LawyerInfo }
     */
    public void setLawyerInfo(LawyerInfo value) {
        this.lawyerInfo = value;
    }

    /**
     * Gets the value of the lawyerOffices property.
     *
     * @return possible object is
     * {@link LawyerOfficeList }
     */
    public LawyerOfficeList getLawyerOffices() {
        return lawyerOffices;
    }

    /**
     * Sets the value of the lawyerOffices property.
     *
     * @param value allowed object is
     *              {@link LawyerOfficeList }
     */
    public void setLawyerOffices(LawyerOfficeList value) {
        this.lawyerOffices = value;
    }

}
