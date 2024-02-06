package com.betfair.logistics.dao.repository;

import com.betfair.logistics.dao.model.Order;
import com.betfair.logistics.dao.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByDestinationId(Long destinationId);

    List<Order> findAllByDeliveryDateAndDestination_NameContainingIgnoreCase(Long deliveryDate, String destinationQueryString);

    default void archiveOrder(Order order) {
        order.setDestination(null);
        order.setOrderStatus(OrderStatus.ARCHIVED);
        this.save(order);
    }

}
