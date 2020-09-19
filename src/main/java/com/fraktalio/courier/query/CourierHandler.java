package com.fraktalio.courier.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.fraktalio.courier.command.api.events.CourierCreatedEvent;
import static com.fraktalio.courier.command.api.valueObjects.AuditEntry;
import static com.fraktalio.courier.query.api.queries.FindAllCouriers;
import static com.fraktalio.courier.query.api.queries.FindCourier;
import static com.fraktalio.courier.query.api.valueObjects.CourierModel;

/**
 * Tracking event processor - Eventual consistency
 */
@Component
@ProcessingGroup("CourierTrackingProcessor")
class CourierHandler {

    private final CourierRepository courierRepository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    private CourierModel convert(CourierEntity entity) {
        return new CourierModel(entity.getId(),
                                entity.getFirstName(),
                                entity.getLastName(),
                                entity.getMaxNumberOfActiveOrders());
    }


    CourierHandler(CourierRepository courierRepository, QueryUpdateEmitter queryUpdateEmitter) {
        this.courierRepository = courierRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    void on(CourierCreatedEvent event) {
        var record = courierRepository.save(new CourierEntity(event.aggregateIdentifier().identifier(),
                                                 event.name().firstName(),
                                                 event.name().lastName(),
                                                 event.maxNumberOfActiveOrders(),
                                                 0));

        queryUpdateEmitter.emit(
                FindAllCouriers.class,
                filter -> true,
                convert(record) );
    }

    @QueryHandler
    List<CourierModel> on(FindAllCouriers query, @MetaDataValue(value = "auditEntry") AuditEntry auditEntry) {
        return courierRepository.findAll().stream()
                                .map(this::convert)
                                .collect(Collectors.toList());
    }

    @QueryHandler
    CourierModel on(FindCourier query, @MetaDataValue(value = "auditEntry") AuditEntry auditEntry) {
        return courierRepository.findById(query.courierId().identifier())
                                .map(this::convert)
                                .orElseThrow(() -> new UnsupportedOperationException(
                                        "Courier with id '" + query.courierId().identifier() + "' not found"));
    }
}
