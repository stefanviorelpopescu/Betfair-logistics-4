package com.betfair.logistics.service;

import com.betfair.logistics.config.CompanyInfo;
import com.betfair.logistics.dao.model.Destination;
import com.betfair.logistics.dao.model.Order;
import com.betfair.logistics.dao.model.OrderStatus;
import com.betfair.logistics.dao.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShippingService {

    private final CompanyInfo companyInfo;
    private final OrderRepository orderRepository;
    private final ShippingManager shippingManager;
//    private final ExecutorService executor;

    public String advanceDate() {

        LocalDate currentDate = companyInfo.advanceDate();
        log.info("New Day starting : " + currentDate);

        Map<Destination, List<Long>> ordersByDestinationId = orderRepository.findAllByDeliveryDate(companyInfo.getCurrentDateAsLong())
                .stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.NEW)
                .collect(groupingBy(Order::getDestination, mapping(Order::getId, Collectors.toList())));

        List<Long> orderIds = ordersByDestinationId.values().stream()
                .flatMap(Collection::stream)
                .toList();
        orderRepository.updateStatusForOrders(orderIds, null, OrderStatus.DELIVERING);

        String destinationNames = ordersByDestinationId.keySet().stream()
                .map(Destination::getName)
                .collect(Collectors.joining(", "));
        if (!destinationNames.isBlank()) {
            log.info("Today we will be delivering to " + destinationNames);
        }

        for (Map.Entry<Destination, List<Long>> entry : ordersByDestinationId.entrySet()) {
//            DeliveryTask deliveryTask = new DeliveryTask(entry.getKey(), entry.getValue());
//            executor.submit(deliveryTask);

            shippingManager.deliverToDestination(entry.getKey(), entry.getValue());
        }

        return "Today we will be delivering to " + destinationNames;
    }

}
