package com.ulegalize.lawfirm.kafka.producer.template;

import com.ulegalize.dto.template.InvoiceTemplateDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;

public interface IInvoiceProducer {
    void createDocument(LawfirmToken lawfirmToken, InvoiceTemplateDTO invoiceTemplateDTO);
}
