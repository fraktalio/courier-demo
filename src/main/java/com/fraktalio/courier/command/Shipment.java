package com.fraktalio.courier.command;

import com.fraktalio.api.AuditEntry;
import com.fraktalio.courier.command.api.*;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Optional;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate(snapshotTriggerDefinition = "shipmentSnapshotTriggerDefinition", cache = "cache")
class Shipment {

    @AggregateIdentifier
    private ShipmentId id;
    private CourierId courierId;
    private ShipmentState state;

    /**
     * This default constructor is used by the Repository to construct a prototype
     * [Shipment]. Events are then used to set properties such as the
     * Shipment Id in order to make the Aggregate reflect it's true logical state.
     */
    private Shipment() {
    }

    /**
     * This constructor is marked as a 'CommandHandler' for the
     * [CreateShipmentCommand]. This command can be used to construct new
     * instances of the Aggregate. If successful a new [ShipmentCreatedEvent]
     * is 'applied' to the aggregate using the Axon 'apply' method. The apply method
     * appears to also propagate the Event to any other registered 'Event
     * Listeners', who may take further action.
     *
     * @param command    - the command
     * @param auditEntry - the authority who initiated this command
     */
    @CommandHandler
    Shipment(CreateShipmentCommand command,
             @MetaDataValue(value = "auditEntry") AuditEntry auditEntry) {
        apply(new ShipmentCreatedEvent(command.targetAggregateIdentifier(), command.address(), auditEntry));
    }

    @EventSourcingHandler
    void on(ShipmentCreatedEvent event) {
        id = event.aggregateIdentifier();
        courierId = null;
        state = ShipmentState.CREATED;
    }

    @CommandHandler
    void on(AssignShipmentCommand command,
            @MetaDataValue(value = "auditEntry") AuditEntry auditEntry,
            CourierProjectionRepository courierProjectionRepository) {

        if (ShipmentState.CREATED == state) {

            Optional<CourierProjection> entity = courierProjectionRepository.findById(command.courierId()
                    .identifier());
            if (entity.isPresent() && entity.get().getNumberOfActiveOrders() < entity.get()
                    .getMaxNumberOfActiveOrders()) {
                apply(new ShipmentAssignedEvent(command.targetAggregateIdentifier(),
                        command.courierId(),
                        auditEntry));
            } else {
                apply(new ShipmentNotAssignedEvent(command.targetAggregateIdentifier(),
                        command.courierId(),
                        auditEntry));
            }
        } else {
            throw new UnsupportedOperationException(ExceptionStatusCode.SHIPMENT_NOT_CREATED.name());
        }
    }

    @EventSourcingHandler
    void on(ShipmentAssignedEvent event) {
        state = ShipmentState.ASSIGNED;
        courierId = event.courierId();
    }

    @EventSourcingHandler
    void on(ShipmentNotAssignedEvent event) {
        state = ShipmentState.CREATED;
        courierId = null;
    }


    @CommandHandler
    void on(MarkShipmentAsDeliveredCommand command,
            @MetaDataValue(value = "auditEntry") AuditEntry auditEntry,
            CourierProjectionRepository courierProjectionRepository) {
        if (ShipmentState.ASSIGNED == state && command.courierId().equals(courierId)) {
            apply(new ShipmentDeliveredEvent(command.targetAggregateIdentifier(),
                    command.courierId(),
                    auditEntry));
        } else {
            throw new UnsupportedOperationException(ExceptionStatusCode.SHIPMENT_NOT_ASSIGNED.name());
        }
    }

    @EventSourcingHandler
    void on(ShipmentDeliveredEvent event) {
        state = ShipmentState.DELIVERED;
    }

    @ExceptionHandler(resultType = UnsupportedOperationException.class)
    public void handle(UnsupportedOperationException exception) {
        var statusCode = ExceptionStatusCode.valueOf(exception.getMessage());
        throw new CommandExecutionException(statusCode.getDescription(), exception, statusCode);
    }
}
