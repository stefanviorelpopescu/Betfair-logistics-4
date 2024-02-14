package com.betfair.logistics.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class ActuatorConfig implements InfoContributor {

    private final CompanyInfo companyInfo;

    @Override
    public void contribute(Info.Builder builder) {

        AtomicLong companyProfit = companyInfo.getCompanyProfit();
        LocalDate currentDate = companyInfo.getCurrentDate();

        Map<String, Object> values = new HashMap<>();
        values.put("currentDate", currentDate);
        values.put("companyProfit", companyProfit);

//        builder.withDetail("currentDate", currentDate);
//        builder.withDetail("companyProfit", companyProfit);

        builder.withDetail("companyInfo", values);
    }
}
