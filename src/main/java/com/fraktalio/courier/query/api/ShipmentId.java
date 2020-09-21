package com.fraktalio.courier.query.api;

public record ShipmentId(String identifier) {

    @Override
    public String toString() {
        return identifier;
    }
}
