package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.lawfirm.kafka.producer.payment.IPaymentProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class ReportService {
    private final LawfirmV2Service lawfirmV2Service;
    private final UserV2Service userV2Service;
    private final IPaymentProducer paymentProducer;

    public ReportService(LawfirmV2Service lawfirmV2Service, UserV2Service userV2Service, IPaymentProducer paymentProducer) {
        this.lawfirmV2Service = lawfirmV2Service;
        this.userV2Service = userV2Service;
        this.paymentProducer = paymentProducer;
    }


    @Scheduled(cron = "#{getSchedulerReport}", zone = "Europe/Brussels")
    @Transactional(rollbackFor = ResponseStatusException.class)
    public void report() throws RuntimeException {
        log.debug("Start report Scheduler");
        Long totalWorkspace = lawfirmV2Service.getTotalWorkspace();
        long totalUser = userV2Service.findTotalUser();
        Long newTotalUserWeek = userV2Service.getNewTotalUserWeek();

        paymentProducer.sendReportTopic(totalWorkspace, totalUser, newTotalUserWeek);

        log.debug("End report Scheduler");
    }
}
