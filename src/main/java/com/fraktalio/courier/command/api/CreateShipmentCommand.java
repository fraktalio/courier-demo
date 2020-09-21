package com.fraktalio.courier.command.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CreateShipmentCommand(@TargetAggregateIdentifier
                                    ShipmentId targetAggregateIdentifier,
                                    Address address) {

}
