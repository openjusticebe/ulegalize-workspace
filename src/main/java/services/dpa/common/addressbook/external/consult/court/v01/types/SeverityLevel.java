
package services.dpa.common.addressbook.external.consult.court.v01.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SeverityLevel.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SeverityLevel">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ERROR"/>
 *     &lt;enumeration value="WARN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "SeverityLevel")
@XmlEnum
public enum SeverityLevel {

    ERROR,
    WARN;

    public static SeverityLevel fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
