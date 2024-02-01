package com.betfair.logistics.dao.converter;

import com.betfair.logistics.dao.dto.OrderDto;
import com.betfair.logistics.dao.model.Order;

import java.util.List;

public class OrderConverter {

    public static OrderDto modelToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .destinationId(order.getDestination().getId())
                .orderStatus(order.getOrderStatus())
                .deliveryDate(order.getDeliveryDate())
                .lastUpdated(order.getLastUpdated())
                .build();
    }

    public static List<OrderDto> modelListToDtoList(List<Order> orders) {
        return orders.stream()
                .map(OrderConverter::modelToDto)
                .toList();
    }
}
