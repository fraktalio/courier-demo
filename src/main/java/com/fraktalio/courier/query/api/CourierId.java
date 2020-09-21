package com.fraktalio.courier.query.api;

public record CourierId(String identifier) {

    @Override
    public String toString() {
        return identifier;
    }
}
