package com.fraktalio.courier.command.api;

import com.fraktalio.api.AuditEntry;

public record ShipmentAssignedEvent(ShipmentId aggregateIdentifier,
                                    CourierId courierId,
                                    AuditEntry auditEntry) {

}
