package com.fraktalio.courier.query;

import com.fraktalio.api.AuditEntry;
import com.fraktalio.courier.command.api.ShipmentAssignedEvent;
import com.fraktalio.courier.command.api.ShipmentCreatedEvent;
import com.fraktalio.courier.command.api.ShipmentDeliveredEvent;
import com.fraktalio.courier.command.api.ShipmentNotAssignedEvent;
import com.fraktalio.courier.command.api.ShipmentState;
import com.fraktalio.courier.query.api.FindAllShipmentsQuery;
import com.fraktalio.courier.query.api.FindShipmentQuery;
import com.fraktalio.courier.query.api.ShipmentModel;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tracking event processor - Eventual consistency
 */
@Component
@ProcessingGroup("ShipmentTrackingProcessor")
class ShipmentHandler {

    private final ShipmentRepository shipmentRepository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    private ShipmentModel convert(ShipmentEntity entity) {
        return new ShipmentModel(entity.getId(),
                                 entity.getCourierId(),
                                 entity.getState(),
                                 entity.getAddress());
    }


    ShipmentHandler(ShipmentRepository shipmentRepository, QueryUpdateEmitter queryUpdateEmitter) {
        this.shipmentRepository = shipmentRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    void on(ShipmentCreatedEvent event) {
        var record = shipmentRepository.save(new ShipmentEntity(event.aggregateIdentifier().identifier(),
                                                                null,
                                                                ShipmentState.CREATED,
                                                                MessageFormat.format("{0},{1}",
                                                                                     event.address().city(),
                                                                                     event.address().street())));

        queryUpdateEmitter.emit(
                FindAllShipmentsQuery.class,
                filter -> true,
                convert(record));
    }

    @EventHandler
    void on(ShipmentAssignedEvent event) {
        var record = shipmentRepository.findById(event.aggregateIdentifier().identifier())
                                       .orElseThrow(() -> new UnsupportedOperationException(
                                               "Shipment with id '" + event.aggregateIdentifier() + "' not found"));

        record.setCourierId(event.courierId().identifier());
        record.setState(ShipmentState.ASSIGNED);
        shipmentRepository.save(record);

        queryUpdateEmitter.emit(
                FindAllShipmentsQuery.class,
                filter -> true,
                convert(record));

        queryUpdateEmitter.emit(
                FindShipmentQuery.class,
                query -> query.shipmentId().identifier().equals(event.aggregateIdentifier().identifier()),
                convert(record));
    }

    @EventHandler
    void on(ShipmentNotAssignedEvent event) {
        var record = shipmentRepository.findById(event.aggregateIdentifier().identifier())
                                       .orElseThrow(() -> new UnsupportedOperationException(
                                               "Shipment with id '" + event.aggregateIdentifier() + "' not found"));

        record.setCourierId(null);
        record.setState(ShipmentState.CREATED);
        shipmentRepository.save(record);

        queryUpdateEmitter.emit(
                FindAllShipmentsQuery.class,
                filter -> true,
                convert(record));

        queryUpdateEmitter.emit(
                FindShipmentQuery.class,
                query -> query.shipmentId().identifier().equals(event.aggregateIdentifier().identifier()),
                convert(record));
    }

    @EventHandler
    void on(ShipmentDeliveredEvent event) {
        var record = shipmentRepository.findById(event.aggregateIdentifier().identifier())
                                       .orElseThrow(() -> new UnsupportedOperationException(
                                               "Shipment with id '" + event.aggregateIdentifier() + "' not found"));

        record.setCourierId(event.courierId().identifier());
        record.setState(ShipmentState.DELIVERED);
        shipmentRepository.save(record);

        queryUpdateEmitter.emit(
                FindAllShipmentsQuery.class,
                filter -> true,
                convert(record));

        queryUpdateEmitter.emit(
                FindShipmentQuery.class,
                query -> query.shipmentId().identifier().equals(event.aggregateIdentifier().identifier()),
                convert(record));
    }

    @QueryHandler
    List<ShipmentModel> on(FindAllShipmentsQuery query, @MetaDataValue(value = "auditEntry") AuditEntry auditEntry) {
        return shipmentRepository.findAll().stream()
                                 .map(this::convert)
                                 .collect(Collectors.toList());
    }

    @QueryHandler
    ShipmentModel on(FindShipmentQuery query, @MetaDataValue(value = "auditEntry") AuditEntry auditEntry) {
        return shipmentRepository.findById(query.shipmentId().identifier())
                                 .map(this::convert)
                                 .orElseThrow(() -> new UnsupportedOperationException(
                                         "Courier with id '" + query.shipmentId().identifier() + "' not found"));
    }
}
