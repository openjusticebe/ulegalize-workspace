package com.ulegalize.lawfirm.service.v2;

import java.math.BigDecimal;

public interface LawfirmVatV2Service {
    Long changeDefaultVat(BigDecimal vat);

    Long deleteVat(BigDecimal vat);

    Long createVat(BigDecimal newVat);

    boolean existVirtualCabVatByVat(BigDecimal newVat);

    Long countVirtualCabVatByVcKey();
}
