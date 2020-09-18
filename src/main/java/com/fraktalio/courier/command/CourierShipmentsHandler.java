package com.fraktalio.courier.command;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.fraktalio.courier.command.api.events.*;

@Component
@ProcessingGroup("SubscribingCourierProcessor")
class CourierShipmentsHandler {

    private final CourierShipmentsRepository courierShipmentsRepository;

    CourierShipmentsHandler(CourierShipmentsRepository courierShipmentsRepository) {
        this.courierShipmentsRepository = courierShipmentsRepository;
    }

    @EventHandler
    void on(CourierCreatedEvent event) {
        courierShipmentsRepository.save(new CourierShipmentsEntity(event.aggregateIdentifier().identifier(),
                                                                   event.maxNumberOfActiveOrders(),
                                                                   0));
    }

    @EventHandler
    void on(ShipmentAssignedEvent event) {
        Optional<CourierShipmentsEntity> entity = courierShipmentsRepository.findById(event.courierId().identifier());
        if (entity.isPresent()) {
            var courierEntity = entity.get();
            courierEntity.setNumberOfActiveOrders(courierEntity.getNumberOfActiveOrders() + 1);
            courierShipmentsRepository.save(courierEntity);
        } else {
            throw new RuntimeException("No Courier with this identifier");
        }
    }

    @EventHandler
    void on(ShipmentDeliveredEvent event) {
        Optional<CourierShipmentsEntity> entity = courierShipmentsRepository.findById(event.courierId().identifier());
        if (entity.isPresent()) {
            var courierEntity = entity.get();
            courierEntity.setNumberOfActiveOrders(courierEntity.getNumberOfActiveOrders() - 1);
            courierShipmentsRepository.save(courierEntity);
        } else {
            throw new RuntimeException("No Courier with this identifier");
        }
    }
}
