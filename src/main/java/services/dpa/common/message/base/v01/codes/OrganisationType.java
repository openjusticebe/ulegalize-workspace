
package services.dpa.common.message.base.v01.codes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrganisationType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrganisationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="LAWYER_OFFICE"/>
 *     &lt;enumeration value="PERSONAL_LAWYER_OFFICE"/>
 *     &lt;enumeration value="BAR"/>
 *     &lt;enumeration value="BAR_ASSOCIATION"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "OrganisationType", namespace = "http://common.dpa.services/message/base/v01/codes")
@XmlEnum
public enum OrganisationType {

    LAWYER_OFFICE,
    PERSONAL_LAWYER_OFFICE,
    BAR,
    BAR_ASSOCIATION;

    public static OrganisationType fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
