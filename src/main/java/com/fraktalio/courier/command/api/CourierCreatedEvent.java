package com.fraktalio.courier.command.api;

import com.fraktalio.api.AuditEntry;

public record CourierCreatedEvent(CourierId aggregateIdentifier, String firstName, String lastName,
                                  Integer maxNumberOfActiveOrders, AuditEntry auditEntry) {

}
