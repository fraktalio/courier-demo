package com.fraktalio.courier.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.spring.stereotype.Aggregate;

import static com.fraktalio.courier.command.api.commands.*;
import static com.fraktalio.courier.command.api.events.*;
import static com.fraktalio.courier.command.api.valueObjects.*;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate(snapshotTriggerDefinition = "shipmentSnapshotTriggerDefinition", cache = "cache")
class Shipment {

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
    public Shipment(CreateShipmentCommand command,
                    @MetaDataValue(value = "auditEntry") AuditEntry auditEntry) {
        apply(new ShipmentCreatedEvent(command.targetAggregateIdentifier(), command.address(), auditEntry));
    }

    @EventSourcingHandler
    public void on(ShipmentCreatedEvent event) {
        id = event.aggregateIdentifier();
        courierId = null;
        state = ShipmentState.CREATED;
    }

    @CommandHandler
    void on(AssignShipmentCommand command,
                @MetaDataValue(value = "auditEntry") AuditEntry auditEntry) {
        if (ShipmentState.CREATED == state) {
            apply(new ShipmentAssigningInitiatedEvent(command.targetAggregateIdentifier(),
                                                      command.courierId(),
                                                      auditEntry));
        } else {
            throw new UnsupportedOperationException("The current state of Shipment is not %s"
                                                            .formatted(ShipmentState.CREATED));
        }
    }

    @EventSourcingHandler
    public void on(ShipmentAssigningInitiatedEvent event) {
        state = ShipmentState.ASSIGNING;
        courierId = event.courierId();
    }

    @CommandHandler
    void on(MarkShipmentAsAssignedCommand command,
            @MetaDataValue(value = "auditEntry") AuditEntry auditEntry) {
        if (ShipmentState.ASSIGNING == state && command.courierId() == courierId) {
            apply(new ShipmentAssignedEvent(command.targetAggregateIdentifier(),
                                            command.courierId(),
                                            auditEntry));
        } else {
            throw new UnsupportedOperationException(
                    "The current state of Shipment is not %s and/or it is not the same Courier that initiated the process"
                            .formatted(ShipmentState.ASSIGNING));
        }
    }

    @EventSourcingHandler
    public void on(ShipmentAssignedEvent event) {
        state = ShipmentState.ASSIGNED;
        courierId = event.courierId();
    }

    @CommandHandler
    void on(MarkShipmentAsNotAssignedCommand command,
            @MetaDataValue(value = "auditEntry") AuditEntry auditEntry) {
        if (ShipmentState.ASSIGNING == state && command.courierId() == courierId) {
            apply(new ShipmentNotAssignedEvent(command.targetAggregateIdentifier(),
                                               command.courierId(),
                                               auditEntry));
        } else {
            throw new UnsupportedOperationException(
                    "The current state of Shipment is not %s and/or it is not the same Courier that initiated the process"
                            .formatted(ShipmentState.ASSIGNING));
        }
    }

    @EventSourcingHandler
    public void on(ShipmentNotAssignedEvent event) {
        state = ShipmentState.CREATED;
        courierId = null;
    }
}
