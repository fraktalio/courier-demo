package com.fraktalio.courier.command.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record AssignShipmentCommand(@TargetAggregateIdentifier
                                    ShipmentId targetAggregateIdentifier,
                                    CourierId courierId) {

}
