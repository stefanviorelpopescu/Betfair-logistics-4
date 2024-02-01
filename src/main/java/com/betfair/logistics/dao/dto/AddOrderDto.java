package com.betfair.logistics.dao.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddOrderDto {

    @NotNull
    private Long deliveryDate;

    @NotNull
    private Long destinationId;

}
