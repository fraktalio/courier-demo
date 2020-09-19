package com.fraktalio.courier.query;

import com.fraktalio.courier.command.api.events;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

/**
 * Tracking event processor - Eventual consistency
 */
@Component
@ProcessingGroup("CourierTrackingProcessor")
class CourierHandler {

    private final CourierRepository courierRepository;


    CourierHandler(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @EventHandler
    void on(events.CourierCreatedEvent event) {
        courierRepository.save(new CourierEntity(event.aggregateIdentifier().identifier(),
                                                 event.maxNumberOfActiveOrders(),
                                                 0));
    }
}
