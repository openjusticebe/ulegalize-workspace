package com.ulegalize.lawfirm.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class InvoiceUtils {
    public String communication(Long idFacture, Integer year) {
        String firstChar = idFacture + "" + year + "";
        if (firstChar.length() >= 10) {
            firstChar = firstChar.substring(0, 10);
        }
        String leftPad = StringUtils.leftPad(firstChar, 10, "0");
        BigDecimal divide = new BigDecimal(leftPad).divide(new BigDecimal(97), 4, RoundingMode.HALF_UP);
        // EX divide : 15.4523434
        BigDecimal fractionalPart = divide.remainder(BigDecimal.ONE); // Result:  0.4523434
        // Result * 97
        BigDecimal multiply = fractionalPart.multiply(new BigDecimal(97)).setScale(2, RoundingMode.HALF_UP);// Result = 56.99
        // 0.99
        BigDecimal multiFract = divide.remainder(BigDecimal.ONE); // Result .99

        // if multiFract there is no decimal become 97
        BigDecimal rounded;
        // round decimal to 1 if it's not 0.00
        if (!multiFract.setScale(0, RoundingMode.UP).equals(BigDecimal.ONE)) {
            rounded = new BigDecimal(97);
        } else {
            // round 56.99 to 57
            rounded = multiply.setScale(0, RoundingMode.UP);
        }

        return leftPad + rounded;
    }

}
