package com.betfair.logistics.controller;

import com.betfair.logistics.dao.dto.DestinationDto;
import com.betfair.logistics.exception.CannotCreateResourceException;
import com.betfair.logistics.exception.ResourceNotFoundException;
import com.betfair.logistics.service.DestinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;

    @GetMapping
    public List<DestinationDto> getAllDestinations() {
        return destinationService.getAllDestinations();
    }

    @Operation(summary = "Get a destination by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the destination",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DestinationDto.class)) }),
            @ApiResponse(responseCode = "400", description = "ID is not a number",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Destination not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public DestinationDto getDestination(@PathVariable Long id) throws ResourceNotFoundException {
        return destinationService.getDestinationById(id);
    }

    @PostMapping
    public Long createDestination(@RequestBody @Valid DestinationDto destinationDto) throws CannotCreateResourceException {
        return destinationService.createDestination(destinationDto);
    }

    @PutMapping
    public void updateDestination(@RequestBody @Valid DestinationDto destinationDto) throws CannotCreateResourceException {
        destinationService.updateDestination(destinationDto);
    }

    @DeleteMapping("{id}")
    public void deleteDestination(@PathVariable Long id) throws ResourceNotFoundException {
        destinationService.deleteDestination(id);
    }
}
