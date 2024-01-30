package com.betfair.logistics.controller;

import com.betfair.logistics.dao.dto.DestinationDto;
import com.betfair.logistics.exception.CannotCreateResourceException;
import com.betfair.logistics.exception.ResourceNotFoundException;
import com.betfair.logistics.service.DestinationService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping
    public List<DestinationDto> getAllDestinations() {
        return destinationService.getAllDestinations();
    }

    @GetMapping("/{id}")
    public DestinationDto getDestination(@PathVariable Long id) throws ResourceNotFoundException {
        return destinationService.getDestinationById(id);
    }

    @PostMapping
    public Long createDestination(@RequestBody @Valid DestinationDto destinationDto) throws CannotCreateResourceException {
        return destinationService.createDestination(destinationDto);
    }

    @DeleteMapping("{id}")
    public void deleteDestination(@PathVariable Long id) throws ResourceNotFoundException {
        destinationService.deleteDestination(id);
    }
}
