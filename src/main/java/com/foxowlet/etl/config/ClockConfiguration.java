package com.foxowlet.etl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

@Configuration
public class ClockConfiguration {
    @Bean
    public Clock clock(@Value("${date}") String date) {
        ZoneId zone = ZoneId.systemDefault();
        return Clock.fixed(LocalDate.parse(date).atStartOfDay(zone).toInstant(), zone);
    }
}
