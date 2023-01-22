package com.ulegalize.lawfirm.rest.v2;

import com.ulegalize.lawfirm.model.LawfirmToken;
import org.springframework.core.io.ByteArrayResource;

public interface ReportApi {
    ByteArrayResource getInvoice(LawfirmToken lawfirmToken, Long invoiceId) throws Exception;
}
