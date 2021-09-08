
package services.dpa.common.addressbook.external.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LawyerOffice complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="LawyerOffice">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/addressbook/base/v01/types}LawyerOffice">
 *       &lt;sequence>
 *         &lt;element name="primary" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LawyerOffice", propOrder = {
        "primary"
})
public class LawyerOffice
        extends services.dpa.common.addressbook.base.v01.types.LawyerOffice {

    protected boolean primary;

    /**
     * Gets the value of the primary property.
     */
    public boolean isPrimary() {
        return primary;
    }

    /**
     * Sets the value of the primary property.
     */
    public void setPrimary(boolean value) {
        this.primary = value;
    }

}
