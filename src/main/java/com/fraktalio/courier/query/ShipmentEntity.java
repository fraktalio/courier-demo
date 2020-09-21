package com.fraktalio.courier.query;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fraktalio.courier.command.api.ShipmentState;

@Entity
class ShipmentEntity {

    @Id
    private String id;
    private String courierId;
    private ShipmentState state;
    private String address;


    protected ShipmentEntity() {
    }

    ShipmentEntity(String id, String courierId, ShipmentState state, String address) {
        this.id = id;
        this.courierId = courierId;
        this.state = state;
        this.address = address;
    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    String getCourierId() {
        return courierId;
    }

    void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    ShipmentState getState() {
        return state;
    }

    void setState(ShipmentState state) {
        this.state = state;
    }

    String getAddress() {
        return address;
    }

    void setAddress(String address) {
        this.address = address;
    }
}
