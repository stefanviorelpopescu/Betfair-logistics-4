package com.betfair.logistics.dao.repository;

import com.betfair.logistics.dao.model.Order;
import com.betfair.logistics.dao.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByDestinationId(Long destinationId);

    List<Order> findAllByDeliveryDate(Long deliveryDate);

    List<Order> findAllByDeliveryDateAndDestination_NameContainingIgnoreCase(Long deliveryDate, String destinationQueryString);

    default void archiveOrder(Order order) {
        order.setDestination(null);
        changeOrderState(order, OrderStatus.ARCHIVED);
        this.save(order);
    }

    default boolean changeOrderState(Order order, OrderStatus newStatus) {
        OrderStatus initialStatus = order.getOrderStatus();
        if (OrderStatus.allowedTransitions.get(initialStatus).contains(newStatus)) {
            order.setOrderStatus(newStatus);
            return true;
        }
        return false;
    }

    default int updateStatusForOrders(List<Long> orderIds, OrderStatus initialStatus, OrderStatus newStatus) {
        int affectedRows = 0;

        List<Order> ordersToUpdate = this.findAllById(orderIds).stream()
                .filter(order -> isNull(initialStatus) || order.getOrderStatus() == initialStatus)
                .toList();

        for (Order order : ordersToUpdate) {
            if (changeOrderState(order, newStatus)) {
                affectedRows++;
            }
        }
        this.saveAll(ordersToUpdate);
        return affectedRows;
    }
}
