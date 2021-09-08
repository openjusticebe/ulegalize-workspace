
package services.dpa.common.addressbook.base.v01.codes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CommunicationMediumType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CommunicationMediumType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PHONE"/>
 *     &lt;enumeration value="EMAIL"/>
 *     &lt;enumeration value="FAX"/>
 *     &lt;enumeration value="GSM"/>
 *     &lt;enumeration value="WEBSITE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "CommunicationMediumType", namespace = "http://common.dpa.services/addressbook/base/v01/codes")
@XmlEnum
public enum CommunicationMediumType {

    PHONE,
    EMAIL,
    FAX,
    GSM,
    WEBSITE;

    public static CommunicationMediumType fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
