package com.fraktalio.courier.command.api;

import static com.fraktalio.courier.command.api.valueObjects.*;

public class events {

    public static record CourierCreatedEvent(CourierId aggregateIdentifier, PersonName name,
                                             Integer maxNumberOfActiveOrders, AuditEntry auditEntry) {

    }

    public static record ShipmenDeliveredEvent(ShipmentId agregateIdentifier) {

    }

    public static record ShipmenExpiredEvent(ShipmentId agregateIdentifier) {

    }

    public static record ShipmentCollectedEvent(ShipmentId aggregateIdentifier,
                                                CourierId courierId) {

    }

    public static record ShipmentCreatedEvent(ShipmentId aggregateIdentifier, Address address,
                                              AuditEntry auditEntry) {

    }

    public static record ShipmentNotCollectedEvent(ShipmentId aggregateIdentifier,
                                                   CourierId courierId) {

    }
}
