package com.fraktalio.courier.command.api;

import com.fraktalio.api.AuditEntry;

public record ShipmentNotAssignedEvent(ShipmentId aggregateIdentifier,
                                       CourierId courierId,
                                       AuditEntry auditEntry) {

}
