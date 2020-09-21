package com.fraktalio.courier.command.api;

import java.util.UUID;

public record CourierId(String identifier) {

    public CourierId() {
        this(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return identifier;
    }
}
