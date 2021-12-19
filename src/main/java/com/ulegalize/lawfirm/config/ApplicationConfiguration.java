package com.ulegalize.lawfirm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class ApplicationConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${app.scheduler.calendar}")
    String schedulerCalendar;

    @Bean
    public String getSchedulerValue() {
        return schedulerCalendar;
    }


}
