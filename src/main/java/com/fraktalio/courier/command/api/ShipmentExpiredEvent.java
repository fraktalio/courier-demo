package com.fraktalio.courier.command.api;

public record ShipmentExpiredEvent(ShipmentId aggregateIdentifier, AuditEntry auditEntry) {

}
