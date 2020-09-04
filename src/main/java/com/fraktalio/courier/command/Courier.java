package com.fraktalio.courier.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static com.fraktalio.courier.command.api.commands.CreateCourierCommand;
import static com.fraktalio.courier.command.api.events.CourierCreatedEvent;
import static com.fraktalio.courier.command.api.valueObjects.*;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate(snapshotTriggerDefinition = "courierSnapshotTriggerDefinition", cache = "cache")
class Courier {

    /**
     * Aggregates that are managed by Axon must have a unique identifier. The
     * annotation 'AggregateIdentifier' identifies the id field as such.
     */
    @AggregateIdentifier
    private CourierId id;
    private PersonName name;
    private Integer maxNumberOfActiveOrders = 5;
    private Integer numberOfActiveOrders = 0;

    /**
     * This default constructor is used by the Repository to construct a prototype
     * [Courier]. Events are then used to set properties such as the
     * Courier Id in order to make the Aggregate reflect it's true logical state.
     */
    private Courier() {
    }

    /**
     * This constructor is marked as a 'CommandHandler' for the
     * [CreateCourierCommand]. This command can be used to construct new
     * instances of the Aggregate. If successful a new [CourierCreatedEvent]
     * is 'applied' to the aggregate using the Axon 'apply' method. The apply method
     * appears to also propagate the Event to any other registered 'Event
     * Listeners', who may take further action.
     *
     * @param command    - the command
     * @param auditEntry - the authority who initiated this command
     */
    @CommandHandler
    Courier(CreateCourierCommand command, @MetaDataValue(value = "auditEntry") AuditEntry auditEntry) {
        apply(
                new CourierCreatedEvent(
                        command.targetAggregateIdentifier(),
                        command.name(),
                        command.maxNumberOfActiveOrders(),
                        auditEntry
                )
        );
    }

    @EventSourcingHandler
    void on(CourierCreatedEvent event) {
        id = event.aggregateIdentifier();
        name = event.name();
        maxNumberOfActiveOrders = event.maxNumberOfActiveOrders();
        numberOfActiveOrders = 0;
    }
}
