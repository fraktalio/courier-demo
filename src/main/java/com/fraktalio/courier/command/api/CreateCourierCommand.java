package com.fraktalio.courier.command.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CreateCourierCommand(@TargetAggregateIdentifier
                                   CourierId targetAggregateIdentifier,
                                   String firstName, String lastName, Integer maxNumberOfActiveOrders) {

    public CreateCourierCommand(String firstName, String lastName, Integer maxNumberOfActiveOrders) {
        this(new CourierId(), firstName, lastName, maxNumberOfActiveOrders);
    }
}
