package com.fraktalio.courier.command.api;

import static com.fraktalio.courier.command.api.valueObjects.*;

public class events {

    public static record CourierCreatedEvent(CourierId aggregateIdentifier, PersonName name,
                                             Integer maxNumberOfActiveOrders, AuditEntry auditEntry) {

    }

    public static record ShipmentDeliveredEvent(ShipmentId aggregateIdentifier,
                                                CourierId courierId,
                                                AuditEntry auditEntry) {

    }

    public static record ShipmentExpiredEvent(ShipmentId aggregateIdentifier, AuditEntry auditEntry) {

    }

    public static record ShipmentAssignedEvent(ShipmentId aggregateIdentifier,
                                               CourierId courierId,
                                               AuditEntry auditEntry) {

    }

    public static record ShipmentCreatedEvent(ShipmentId aggregateIdentifier, Address address,
                                              AuditEntry auditEntry) {

    }

    public static record ShipmentNotAssignedEvent(ShipmentId aggregateIdentifier,
                                                  CourierId courierId,
                                                  AuditEntry auditEntry) {

    }
}
