package com.betfair.logistics.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
@Getter
public class CompanyInfo {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;

    private LocalDate currentDate = LocalDate.of(2021, 12, 14);
    private Long companyProfit;

    public Long getCurrentDateAsLong() {
        return currentDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    }

    public Long getLocalDateStringAsLong(String dateString) {
        LocalDate localDate = LocalDate.parse(dateString, dateTimeFormatter);
        return localDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    }



}
