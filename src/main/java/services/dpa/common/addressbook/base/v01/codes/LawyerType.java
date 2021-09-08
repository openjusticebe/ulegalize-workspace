
package services.dpa.common.addressbook.base.v01.codes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LawyerType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LawyerType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TRAINEE_CAND"/>
 *     &lt;enumeration value="TRAINEE"/>
 *     &lt;enumeration value="ROLL"/>
 *     &lt;enumeration value="EU"/>
 *     &lt;enumeration value="FOREIGN"/>
 *     &lt;enumeration value="HONOR"/>
 *     &lt;enumeration value="OLD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "LawyerType", namespace = "http://common.dpa.services/addressbook/base/v01/codes")
@XmlEnum
public enum LawyerType {

    TRAINEE_CAND,
    TRAINEE,
    ROLL,
    EU,
    FOREIGN,
    HONOR,
    OLD;

    public static LawyerType fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
