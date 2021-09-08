
package services.dpa.common.global.v01.codes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Gender.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Gender">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="M"/>
 *     &lt;enumeration value="F"/>
 *     &lt;enumeration value="U"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "Gender", namespace = "http://common.dpa.services/global/v01/codes")
@XmlEnum
public enum Gender {

    M,
    F,
    U;

    public static Gender fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
