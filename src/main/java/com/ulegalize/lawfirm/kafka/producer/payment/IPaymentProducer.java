package com.ulegalize.lawfirm.kafka.producer.payment;

import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;

public interface IPaymentProducer {
    void sendPayment(LawfirmToken lawfirmToken, LawfirmCalendarEventDTO calendarEvent);

    void sendReportTopic(Long totalWorkspace, long totalUser, Long newTotalUserWeek);
}