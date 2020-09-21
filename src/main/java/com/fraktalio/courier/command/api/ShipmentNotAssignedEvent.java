package com.fraktalio.courier.command.api;

public record ShipmentNotAssignedEvent(ShipmentId aggregateIdentifier,
                                       CourierId courierId,
                                       AuditEntry auditEntry) {

}
