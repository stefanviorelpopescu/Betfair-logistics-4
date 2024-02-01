package com.betfair.logistics.dao.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
@Builder
public class Order {

//    @Id
//    @ColumnDefault(value = "RANDOM_UUID()")
//    private UUID id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long deliveryDate;

    @Column(nullable = false)
    private Long lastUpdated;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name="destination_id")
    Destination destination;

}
