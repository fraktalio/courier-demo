package com.fraktalio.courier.command.api;

import com.fraktalio.api.AuditEntry;

public record ShipmentCreatedEvent(ShipmentId aggregateIdentifier, Address address,
                                   AuditEntry auditEntry) {

}
