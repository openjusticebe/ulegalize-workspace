
package services.dpa.common.addressbook.external.consult.court.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for nrFix complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="nrFix">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/addressbook/external/consult/court/v01/types}ReferenceRulePart">
 *       &lt;sequence>
 *         &lt;element name="size" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nrFix", propOrder = {
        "size"
})
public class NrFix
        extends ReferenceRulePart {

    protected int size;

    /**
     * Gets the value of the size property.
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     */
    public void setSize(int value) {
        this.size = value;
    }

}
