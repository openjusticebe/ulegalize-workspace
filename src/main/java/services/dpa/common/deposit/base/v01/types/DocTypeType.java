
package services.dpa.common.deposit.base.v01.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DocTypeType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DocTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CONCLUSION"/>
 *     &lt;enumeration value="PIECE"/>
 *     &lt;enumeration value="PETITION"/>
 *     &lt;enumeration value="ASSOCIATED_LETTER"/>
 *     &lt;enumeration value="OTHER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "DocTypeType")
@XmlEnum
public enum DocTypeType {

    CONCLUSION,
    PIECE,
    PETITION,
    ASSOCIATED_LETTER,
    OTHER;

    public static DocTypeType fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
