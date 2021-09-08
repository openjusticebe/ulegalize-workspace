
package services.dpa.common.addressbook.external.consult.court.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for alpha complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="alpha">
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/addressbook/external/consult/court/v01/types}ReferenceRulePart">
 *       &lt;sequence>
 *         &lt;element name="alphaValueList" type="{http://common.dpa.services/addressbook/external/consult/court/v01/types}alphaValueList"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "alpha", propOrder = {
        "alphaValueList"
})
public class Alpha
        extends ReferenceRulePart {

    @XmlElement(required = true)
    protected AlphaValueList alphaValueList;

    /**
     * Gets the value of the alphaValueList property.
     *
     * @return possible object is
     * {@link AlphaValueList }
     */
    public AlphaValueList getAlphaValueList() {
        return alphaValueList;
    }

    /**
     * Sets the value of the alphaValueList property.
     *
     * @param value allowed object is
     *              {@link AlphaValueList }
     */
    public void setAlphaValueList(AlphaValueList value) {
        this.alphaValueList = value;
    }

}
