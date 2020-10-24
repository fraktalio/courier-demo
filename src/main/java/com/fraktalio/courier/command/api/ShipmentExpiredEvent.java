package com.fraktalio.courier.command.api;

import com.fraktalio.api.AuditEntry;

public record ShipmentExpiredEvent(ShipmentId aggregateIdentifier, AuditEntry auditEntry) {

}
