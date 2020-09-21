package com.fraktalio.courier.command.api;

public record CourierCreatedEvent(CourierId aggregateIdentifier, String firstName, String lastName,
                                  Integer maxNumberOfActiveOrders, AuditEntry auditEntry) {

}
