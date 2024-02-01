package com.betfair.logistics.controller;

import com.betfair.logistics.dao.dto.AddOrderDto;
import com.betfair.logistics.dao.dto.OrderDto;
import com.betfair.logistics.exception.CannotCreateResourceException;
import com.betfair.logistics.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add")
    public List<OrderDto> addOrders(@RequestBody @Valid List<AddOrderDto> addOrderDtos) throws CannotCreateResourceException {
        return orderService.addOrders(addOrderDtos);
    }

    @PutMapping("/cancel")
    public void cancelOrders(@RequestBody List<Long> orderIds) {
        orderService.cancelOrders(orderIds);
    }

}
