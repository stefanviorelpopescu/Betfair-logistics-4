package com.betfair.logistics.service;

import com.betfair.logistics.dao.cache.DestinationCache;
import com.betfair.logistics.dao.dto.DestinationDto;
import com.betfair.logistics.dao.model.Destination;
import com.betfair.logistics.exception.CannotCreateResourceException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DestinationServiceTest {

    @Autowired
    DestinationService destinationService;

    @MockBean
    DestinationCache destinationCache;

    @Order(3)
    @Test
    void createValidDestination() {
    }

    @Order(2)
    @Test
    void createInvalidDestination_WithExistingName() {
        //given
        String destinationName = "destinationName";
        Destination existingDestination = Destination.builder()
                .id(5L)
                .name(destinationName)
                .build();
        when(destinationCache.findByName(destinationName)).thenReturn(Optional.of(existingDestination));
        DestinationDto destinationDto = DestinationDto.builder()
                .name(destinationName)
                .build();

        //when
        CannotCreateResourceException cannotCreateResourceException = assertThrows(CannotCreateResourceException.class,
                () -> destinationService.createDestination(destinationDto));

        //then
        assertEquals(String.format("Destination with id=%d already has name=%s", existingDestination.getId(), existingDestination.getName()),
                cannotCreateResourceException.getMessage());
    }

    @Order(1)
    @Test
    void createInvalidDestination_WithId() {
        //given
        DestinationDto destinationDto = DestinationDto.builder()
                .id(5L)
                .build();

        //when
        CannotCreateResourceException cannotCreateResourceException = assertThrows(CannotCreateResourceException.class,
                () -> destinationService.createDestination(destinationDto));

        //then
        assertEquals("ID should not be provided!", cannotCreateResourceException.getMessage());
    }
}