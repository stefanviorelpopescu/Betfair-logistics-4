package com.betfair.logistics.dao.dto;


import com.betfair.logistics.dao.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {

    private Long id;
    private Long deliveryDate;
    private Long lastUpdated;
    private OrderStatus orderStatus;
    private Long destinationId;
}
