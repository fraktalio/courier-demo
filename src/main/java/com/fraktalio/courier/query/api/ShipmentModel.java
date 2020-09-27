package com.fraktalio.courier.query.api;

import com.fraktalio.courier.command.api.ShipmentState;

public class ShipmentModel {
    private String aggregateIdentifier;
    private String courierId;
    private ShipmentState state;
    private String address;

    public ShipmentModel(String aggregateIdentifier, String courierId,
                         ShipmentState state, String address) {
        this.aggregateIdentifier = aggregateIdentifier;
        this.courierId = courierId;
        this.state = state;
        this.address = address;
    }

    public ShipmentModel() {
    }

    public String getAggregateIdentifier() {
        return aggregateIdentifier;
    }

    public void setAggregateIdentifier(String aggregateIdentifier) {
        this.aggregateIdentifier = aggregateIdentifier;
    }

    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    public ShipmentState getState() {
        return state;
    }

    public void setState(ShipmentState state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
