
package services.dpa.common.addressbook.external.consult.court.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for year complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="year">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/addressbook/external/consult/court/v01/types}ReferenceRulePart">
 *       &lt;sequence>
 *         &lt;element name="lower" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="upper" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "year", propOrder = {
        "lower",
        "upper"
})
public class Year
        extends ReferenceRulePart {

    protected int lower;
    protected int upper;

    /**
     * Gets the value of the lower property.
     */
    public int getLower() {
        return lower;
    }

    /**
     * Sets the value of the lower property.
     */
    public void setLower(int value) {
        this.lower = value;
    }

    /**
     * Gets the value of the upper property.
     */
    public int getUpper() {
        return upper;
    }

    /**
     * Sets the value of the upper property.
     */
    public void setUpper(int value) {
        this.upper = value;
    }

}
