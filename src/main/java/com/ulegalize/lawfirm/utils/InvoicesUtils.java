package com.ulegalize.lawfirm.utils;

import com.ulegalize.enumeration.EnumFactureType;
import org.apache.commons.lang3.StringUtils;

public class InvoicesUtils {

    private InvoicesUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getInvoiceReference(Integer yearFacture, Integer numFacture, EnumFactureType enumFactureType) {

        return enumFactureType.getCode() + "-" + yearFacture + "-" + StringUtils.leftPad(numFacture.toString(), 3, "0");
    }
}
