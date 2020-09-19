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
class CourierHandler {

    private final CourierRepository courierRepository;

    CourierHandler(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @EventHandler
    void on(CourierCreatedEvent event) {
        courierRepository.save(new CourierEntity(event.aggregateIdentifier().identifier(),
                                                 event.maxNumberOfActiveOrders(),
                                                 0));
    }

    @EventHandler
    void on(ShipmentAssignedEvent event) {
        Optional<CourierEntity> entity = courierRepository.findById(event.courierId().identifier());
        if (entity.isPresent()) {
            var courierEntity = entity.get();
            courierEntity.setNumberOfActiveOrders(courierEntity.getNumberOfActiveOrders() + 1);
            courierRepository.save(courierEntity);
        } else {
            throw new RuntimeException("No Courier with this identifier");
        }
    }

    @EventHandler
    void on(ShipmentDeliveredEvent event) {
        Optional<CourierEntity> entity = courierRepository.findById(event.courierId().identifier());
        if (entity.isPresent()) {
            var courierEntity = entity.get();
            courierEntity.setNumberOfActiveOrders(courierEntity.getNumberOfActiveOrders() - 1);
            courierRepository.save(courierEntity);
        } else {
            throw new RuntimeException("No Courier with this identifier");
        }
    }
}
