package com.fraktalio.courier.command.api;

public record ShipmentDeliveredEvent(ShipmentId aggregateIdentifier,
                                     CourierId courierId,
                                     AuditEntry auditEntry) {

}
