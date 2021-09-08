
package services.dpa.common.addressbook.base.v01.codes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LawyerStatus.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LawyerStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NOT_TAKEN"/>
 *     &lt;enumeration value="ACTIVE"/>
 *     &lt;enumeration value="NONACTIVE"/>
 *     &lt;enumeration value="INTERRUPT_TR"/>
 *     &lt;enumeration value="SUSPEND_TR"/>
 *     &lt;enumeration value="SUSPEND_ROLL"/>
 *     &lt;enumeration value="PALACEBAN"/>
 *     &lt;enumeration value="STRIKETHROUGH"/>
 *     &lt;enumeration value="OMISSION"/>
 *     &lt;enumeration value="DECEASED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "LawyerStatus", namespace = "http://common.dpa.services/addressbook/base/v01/codes")
@XmlEnum
public enum LawyerStatus {

    NOT_TAKEN,
    ACTIVE,
    NONACTIVE,
    INTERRUPT_TR,
    SUSPEND_TR,
    SUSPEND_ROLL,
    PALACEBAN,
    STRIKETHROUGH,
    OMISSION,
    DECEASED;

    public static LawyerStatus fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
