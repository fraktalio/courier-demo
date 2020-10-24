package com.fraktalio.courier.command.api;

import com.fraktalio.api.AuditEntry;

public record ShipmentDeliveredEvent(ShipmentId aggregateIdentifier,
                                     CourierId courierId,
                                     AuditEntry auditEntry) {

}
