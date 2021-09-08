
package services.dpa.common.deposit.base.v01.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ChannelTypeType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ChannelTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EDEPOSIT"/>
 *     &lt;enumeration value="FAX"/>
 *     &lt;enumeration value="LETTER"/>
 *     &lt;enumeration value="DPABOX"/>
 *     &lt;enumeration value="EMAIL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "ChannelTypeType")
@XmlEnum
public enum ChannelTypeType {

    EDEPOSIT,
    FAX,
    LETTER,
    DPABOX,
    EMAIL;

    public static ChannelTypeType fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
