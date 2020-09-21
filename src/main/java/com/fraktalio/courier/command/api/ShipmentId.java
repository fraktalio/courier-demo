package com.fraktalio.courier.command.api;

import java.util.UUID;

public record ShipmentId(String identifier) {

    public ShipmentId() {
        this(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return identifier;
    }
}
