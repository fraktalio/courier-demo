package com.fraktalio.courier.command.api;

public record ShipmentCreatedEvent(ShipmentId aggregateIdentifier, Address address,
                                   AuditEntry auditEntry) {

}
