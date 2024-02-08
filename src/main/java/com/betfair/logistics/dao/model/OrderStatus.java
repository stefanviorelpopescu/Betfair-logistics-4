package com.betfair.logistics.dao.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum OrderStatus {
    NEW,
    DELIVERING,
    DELIVERED,
    CANCELED,
    ARCHIVED,
    ;

    public static final Map<OrderStatus, List<OrderStatus>> allowedTransitions = new HashMap<>();

    static {
        allowedTransitions.put(NEW, List.of(DELIVERING, CANCELED, ARCHIVED));
        allowedTransitions.put(DELIVERING, List.of(DELIVERED, CANCELED, ARCHIVED));
        allowedTransitions.put(DELIVERED, List.of(ARCHIVED));
        allowedTransitions.put(CANCELED, List.of(NEW, ARCHIVED));
        allowedTransitions.put(ARCHIVED, Collections.emptyList());
    }
}
