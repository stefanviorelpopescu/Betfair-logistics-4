package com.betfair.logistics.dao.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DestinationDto {

    @Schema(type = "number", example = "1")
    private Long id;

    @Schema(type = "string", example = "Cluj")
    @NotBlank(message = "Name must be provided!")
    private String name;

    @Schema(type = "number", example = "100")
    @NotNull
    @Min(0)
    private int distance;

    @AssertTrue(message = "Distance must be longer than name length")
    @JsonIgnore
    public boolean isDistanceValid() {
        return distance > name.length();
    }
}
