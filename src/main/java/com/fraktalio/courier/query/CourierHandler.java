package com.fraktalio.courier.query;

import com.fraktalio.courier.command.api.AuditEntry;
import com.fraktalio.courier.command.api.CourierCreatedEvent;
import com.fraktalio.courier.query.api.CourierModel;
import com.fraktalio.courier.query.api.FindAllCouriersQuery;
import com.fraktalio.courier.query.api.FindCourierQuery;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
                                                              event.firstName(),
                                                              event.lastName(),
                                                              event.maxNumberOfActiveOrders(),
                                                              0));

        queryUpdateEmitter.emit(
                FindAllCouriersQuery.class,
                filter -> true,
                convert(record));
    }

    @QueryHandler
    List<CourierModel> on(FindAllCouriersQuery query, @MetaDataValue(value = "auditEntry") AuditEntry auditEntry) {
        return courierRepository.findAll().stream()
                                .map(this::convert)
                                .collect(Collectors.toList());
    }

    @QueryHandler
    CourierModel on(FindCourierQuery query, @MetaDataValue(value = "auditEntry") AuditEntry auditEntry) {
        return courierRepository.findById(query.courierId().identifier())
                                .map(this::convert)
                                .orElseThrow(() -> new UnsupportedOperationException(
                                        "Courier with id '" + query.courierId().identifier() + "' not found"));
    }
}
