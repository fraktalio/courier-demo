package com.fraktalio.courier.command.api;

public enum ExceptionStatusCode {

    SHIPMENT_NOT_CREATED("The shipment is not in CREATED state."),
    SHIPMENT_NOT_ASSIGNED("Shipment is not in ASSIGNED state.");

    private final String description;

    ExceptionStatusCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
