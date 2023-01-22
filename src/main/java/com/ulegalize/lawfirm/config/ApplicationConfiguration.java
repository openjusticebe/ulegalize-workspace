package com.ulegalize.lawfirm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
@EnableScheduling
public class ApplicationConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    @Value("${app.scheduler.calendar}")
    String schedulerCalendar;

    @Value("${app.scheduler.reminder}")
    String schedulerReminderEmail;

    @Value("${app.scheduler.report}")
    String schedulerReminderReport;

    @Bean
    public String getSchedulerValue() {
        return schedulerCalendar;
    }

    @Bean
    public String getSchedulerReminderEmail() {
        return schedulerReminderEmail;
    }

    @Bean
    public String getSchedulerReport() {
        return schedulerReminderReport;
    }


}
