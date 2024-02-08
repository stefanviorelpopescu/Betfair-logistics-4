package com.betfair.logistics.service;

import com.betfair.logistics.config.CompanyInfo;
import com.betfair.logistics.dao.model.Destination;
import com.betfair.logistics.dao.model.Order;
import com.betfair.logistics.dao.model.OrderStatus;
import com.betfair.logistics.dao.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShippingManager {

    private final OrderRepository orderRepository;
    private final CompanyInfo companyInfo;

    @SneakyThrows
    @Async("executor")
    public void deliverToDestination(Destination destination, List<Long> orderIds) {

        log.info(String.format("Started delivering %d orders to %s on thread %s for %d km.",
                orderIds.size(), destination.getName(), Thread.currentThread().getName(), destination.getDistance()));

        Thread.sleep(destination.getDistance() * 1000);

        int noOfDeliveredOrders = orderRepository.updateStatusForOrders(orderIds, OrderStatus.DELIVERING, OrderStatus.DELIVERED);
        log.info(String.format("%d deliveries completed for %s ", noOfDeliveredOrders, destination.getName()));

        long newProfit = companyInfo.increaseCompanyProfit((long) noOfDeliveredOrders * destination.getDistance());
        log.info("Company profit : " + newProfit);
    }
}
