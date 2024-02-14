package com.betfair.logistics.dao.cache;

import com.betfair.logistics.dao.model.Destination;
import com.betfair.logistics.dao.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DestinationCache {

    private final DestinationRepository destinationRepository;
    private Map<Long, Destination> data;

    private void populate() {
        data = destinationRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Destination::getId, Function.identity()));
    }

    private Map<Long, Destination> getCacheDate() {
        if (data == null) {
            populate();
        }
        return data;
    }

    public List<Destination> findAll() {
        return new ArrayList<>(getCacheDate().values());
    }

    public Optional<Destination> findById(Long destinationId) {
        return Optional.ofNullable(getCacheDate().get(destinationId));
    }

    public Optional<Destination> findByName(String destinationName) {
        return getCacheDate().values()
                .stream()
                .filter(destination -> destination.getName().equals(destinationName))
                .findAny();
    }

    public void put(Destination destination) {
        getCacheDate().put(destination.getId(), destination);
    }

    public void delete(Destination destination) {
        getCacheDate().remove(destination.getId());
    }

}
