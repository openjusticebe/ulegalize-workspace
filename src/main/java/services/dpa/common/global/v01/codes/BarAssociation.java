
package services.dpa.common.global.v01.codes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BarAssociation.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BarAssociation">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="OVB"/>
 *     &lt;enumeration value="OBFG"/>
 *     &lt;enumeration value="CASS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "BarAssociation", namespace = "http://common.dpa.services/global/v01/codes")
@XmlEnum
public enum BarAssociation {

    OVB,
    OBFG,
    CASS;

    public static BarAssociation fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
