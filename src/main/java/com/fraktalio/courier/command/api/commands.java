package com.fraktalio.courier.command.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static com.fraktalio.courier.command.api.valueObjects.*;

public class commands {

    public static record CollectShipmentCommand(@TargetAggregateIdentifier ShipmentId targetAggregateIdentifier,
                                                CourierId courierId) {

    }

    public static record CreateCourierCommand(@TargetAggregateIdentifier CourierId targetAggregateIdentifier,
                                              PersonName name, Integer maxNumberOfActiveOrders) {

        public CreateCourierCommand(PersonName name, Integer maxNumberOfActiveOrders) {
            this(new CourierId(), name, maxNumberOfActiveOrders);
        }
    }

    public static record CreateShipmentCommand(@TargetAggregateIdentifier ShipmentId targetAggregateIdentifier,
                                               Address address) {

        public CreateShipmentCommand(Address address) {
            this(new ShipmentId(), address);
        }
    }

    public static record MarkShipmentAsDeliveredCommand(
            @TargetAggregateIdentifier ShipmentId targetAggregateIdentifier) {

    }

    public static record MarkShipmentAsExpiredCommand(@TargetAggregateIdentifier ShipmentId targetAggregateIdentifier) {

    }
}
