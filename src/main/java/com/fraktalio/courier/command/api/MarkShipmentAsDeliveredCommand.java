package com.fraktalio.courier.command.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record MarkShipmentAsDeliveredCommand(
        @TargetAggregateIdentifier ShipmentId targetAggregateIdentifier,
        CourierId courierId) {

}
