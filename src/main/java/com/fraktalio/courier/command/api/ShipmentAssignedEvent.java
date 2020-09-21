package com.fraktalio.courier.command.api;

public record ShipmentAssignedEvent(ShipmentId aggregateIdentifier,
                                    CourierId courierId,
                                    AuditEntry auditEntry) {

}
