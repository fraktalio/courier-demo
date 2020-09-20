package com.fraktalio.courier.command;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.fraktalio.courier.command.api.events.*;

/**
 * Subscribing event processor - Immediate consistency
 */
@Component("CommandSideCourierHandler")
@ProcessingGroup("CourierSubscribingProcessor")
class CourierProjectionHandler {

    private final CourierProjectionRepository courierProjectionRepository;

    CourierProjectionHandler(CourierProjectionRepository courierProjectionRepository) {
        this.courierProjectionRepository = courierProjectionRepository;
    }

    @EventHandler
    void on(CourierCreatedEvent event) {
        courierProjectionRepository.save(new CourierProjection(event.aggregateIdentifier().identifier(),
                                                               event.maxNumberOfActiveOrders(),
                                                               0));
    }

    @EventHandler
    void on(ShipmentAssignedEvent event) {
        Optional<CourierProjection> entity = courierProjectionRepository.findById(event.courierId().identifier());
        if (entity.isPresent()) {
            var courierEntity = entity.get();
            courierEntity.setNumberOfActiveOrders(courierEntity.getNumberOfActiveOrders() + 1);
            courierProjectionRepository.save(courierEntity);
        } else {
            throw new RuntimeException("No Courier with this identifier");
        }
    }

    @EventHandler
    void on(ShipmentDeliveredEvent event) {
        Optional<CourierProjection> entity = courierProjectionRepository.findById(event.courierId().identifier());
        if (entity.isPresent()) {
            var courierEntity = entity.get();
            courierEntity.setNumberOfActiveOrders(courierEntity.getNumberOfActiveOrders() - 1);
            courierProjectionRepository.save(courierEntity);
        } else {
            throw new RuntimeException("No Courier with this identifier");
        }
    }
}
