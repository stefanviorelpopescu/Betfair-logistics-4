package com.betfair.logistics.dao.repository;

import com.betfair.logistics.dao.model.Destination;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class DestinationRepositoryTest {

    @Autowired
    DestinationRepository destinationRepository;

    @Test
    void findByExistingName() {
        //given
        Destination destination = destinationRepository.findAll().stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No test data found"));
        String destinationName = destination.getName();

        //when
        Optional<Destination> optionalDestination = destinationRepository.findByName(destinationName);

        //then
        assertTrue(optionalDestination.isPresent());
    }
    @Test
    void findByNonExistingName() {
        //given
        String destinationName = UUID.randomUUID().toString();

        //when
        Optional<Destination> optionalDestination = destinationRepository.findByName(destinationName);

        //then
        assertTrue(optionalDestination.isEmpty());
    }
}