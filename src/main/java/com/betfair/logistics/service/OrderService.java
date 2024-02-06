package com.betfair.logistics.service;

import com.betfair.logistics.config.CompanyInfo;
import com.betfair.logistics.dao.dto.AddOrderDto;
import com.betfair.logistics.dao.dto.OrderDto;
import com.betfair.logistics.dao.model.Destination;
import com.betfair.logistics.dao.model.Order;
import com.betfair.logistics.dao.model.OrderStatus;
import com.betfair.logistics.dao.repository.DestinationRepository;
import com.betfair.logistics.dao.repository.OrderRepository;
import com.betfair.logistics.exception.CannotCreateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.betfair.logistics.dao.converter.OrderConverter.modelListToDtoList;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CompanyInfo companyInfo;

    private final DestinationRepository destinationRepository;
    private final OrderRepository orderRepository;

    public List<OrderDto> getOrders(String dateAsString, String destinationQueryParam) {

        Long dateAsLong = companyInfo.getCurrentDateAsLong();
        if (!dateAsString.isBlank()) {
            dateAsLong = companyInfo.getLocalDateStringAsLong(dateAsString);
        }

        List<Order> foundOrders =
                orderRepository.findAllByDeliveryDateAndDestination_NameContainingIgnoreCase(dateAsLong, destinationQueryParam);

        return modelListToDtoList(foundOrders);
    }

    public List<OrderDto> addOrders(List<AddOrderDto> addOrderDtos) throws CannotCreateResourceException {

        Map<Long, Destination> destinationMap = destinationRepository.findAll().stream()
                .collect(Collectors.toMap(Destination::getId, Function.identity()));

        validateOrdersPayload(addOrderDtos, destinationMap.keySet());

        List<Order> ordersToSave = new ArrayList<>();
        addOrderDtos.forEach(addOrderDto -> ordersToSave.add(
                    createNewOrder(addOrderDto.getDeliveryDate(), destinationMap.get(addOrderDto.getDestinationId())))
                );

        return modelListToDtoList(orderRepository.saveAll(ordersToSave));
    }

    public void cancelOrders(List<Long> orderIds) {

        List<Order> foundOrders = orderRepository.findAllById(orderIds);
        for (Order foundOrder : foundOrders) {
            if (foundOrder.getOrderStatus() != OrderStatus.DELIVERED) {
                foundOrder.setOrderStatus(OrderStatus.CANCELED);
            }
        }
        orderRepository.saveAll(foundOrders);
    }

    private void validateOrdersPayload(List<AddOrderDto> addOrderDtos, Set<Long> destinationIds) throws CannotCreateResourceException {

        StringBuilder errors = new StringBuilder();
        for (int i = 0; i < addOrderDtos.size(); i++) {
            AddOrderDto addOrderDto = addOrderDtos.get(i);
            if (!destinationIds.contains(addOrderDto.getDestinationId())) {
                errors.append(String.format("Destination id=%d is invalid for order %d\n", addOrderDto.getDestinationId(), i));
            }
            if (addOrderDto.getDeliveryDate() <= companyInfo.getCurrentDateAsLong()) {
                errors.append(String.format("Delivery date date=%d for order %d should be greater than current date\n", addOrderDto.getDeliveryDate(), i));
            }
        }
        if (!errors.toString().isBlank()) {
            throw new CannotCreateResourceException(errors.toString());
        }
    }

    private Order createNewOrder(Long deliveryDate, Destination destination) {
        return Order.builder()
                .orderStatus(OrderStatus.NEW)
                .lastUpdated(System.currentTimeMillis())
                .deliveryDate(deliveryDate)
                .destination(destination)
                .build();
    }
}
