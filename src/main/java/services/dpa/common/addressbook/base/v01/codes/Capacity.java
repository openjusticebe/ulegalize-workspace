
package services.dpa.common.addressbook.base.v01.codes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Capacity.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Capacity">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="LAWYER"/>
 *     &lt;enumeration value="BAR_STAFF"/>
 *     &lt;enumeration value="LAWYER_STAFF"/>
 *     &lt;enumeration value="ASSISTANT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "Capacity", namespace = "http://common.dpa.services/addressbook/base/v01/codes")
@XmlEnum
public enum Capacity {

    LAWYER,
    BAR_STAFF,
    LAWYER_STAFF,
    ASSISTANT;

    public static Capacity fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
