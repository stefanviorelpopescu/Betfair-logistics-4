package com.betfair.logistics.service;

import com.betfair.logistics.dao.cache.DestinationCache;
import com.betfair.logistics.dao.converter.DestinationConverter;
import com.betfair.logistics.dao.dto.DestinationDto;
import com.betfair.logistics.dao.model.Destination;
import com.betfair.logistics.dao.repository.DestinationRepository;
import com.betfair.logistics.dao.repository.OrderRepository;
import com.betfair.logistics.exception.CannotCreateResourceException;
import com.betfair.logistics.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final OrderRepository orderRepository;
    private final DestinationCache destinationCache;

    public List<DestinationDto> getAllDestinations() {

        List<Destination> destinationList = destinationCache.findAll();
        return DestinationConverter.modelListToDtoList(destinationList);
    }

//    @Cacheable("destinations")
    public DestinationDto getDestinationById(Long id) throws ResourceNotFoundException {

        Destination destination = destinationCache.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found"));

        return DestinationConverter.modelToDto(destination);
    }

    @CacheEvict("destinations")
    public void deleteDestination(Long id) throws ResourceNotFoundException {

        Destination destination = destinationCache.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found"));

        orderRepository.findAllByDestinationId(id).forEach(orderRepository::archiveOrder);
        destinationRepository.delete(destination);
        destinationCache.delete(destination);
    }

    public Long createDestination(DestinationDto destinationDto) throws CannotCreateResourceException {

        if (destinationDto.getId() != null) {
            throw new CannotCreateResourceException("ID should not be provided!");
        }

        Optional<Destination> optionalDestination = destinationCache.findByName(destinationDto.getName());
        if (optionalDestination.isPresent()) {
            Destination destination = optionalDestination.get();
            throw new CannotCreateResourceException(String.format("Destination with id=%d already has name=%s",
                    destination.getId(), destination.getName()));
        }

        Destination destination = DestinationConverter.dtoToModel(destinationDto);
        destinationRepository.save(destination);
        destinationCache.put(destination);

        return destination.getId();
    }

    @CachePut(value = "destinations", key = "#destinationDto.id")
    public DestinationDto updateDestination(DestinationDto destinationDto) throws CannotCreateResourceException {

        if (destinationDto.getId() == null) {
            throw new CannotCreateResourceException("ID should be provided!");
        }
        Optional<Destination> optionalDestination = destinationCache.findByName(destinationDto.getName());
        if (optionalDestination.isPresent()) {
            Destination destination = optionalDestination.get();
            if (!Objects.equals(destination.getId(), destinationDto.getId())) {
                throw new CannotCreateResourceException(String.format("Name=%s is already assigned to destination with id=%d",
                        destination.getName(), destination.getId()));
            }
        }

        Destination destination = DestinationConverter.dtoToModel(destinationDto);
        Destination saved = destinationRepository.save(destination);
        destinationCache.put(saved);
        return DestinationConverter.modelToDto(saved);
    }
}
