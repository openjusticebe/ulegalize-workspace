
package services.dpa.common.addressbook.base.v01.codes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BillingOrganisationType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BillingOrganisationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PERSON"/>
 *     &lt;enumeration value="ORGANISATION"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "BillingOrganisationType", namespace = "http://common.dpa.services/addressbook/base/v01/codes")
@XmlEnum
public enum BillingOrganisationType {

    PERSON,
    ORGANISATION;

    public static BillingOrganisationType fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
