package com.fraktalio.courier.web.api;

public class AssignShipmentRequest {

    private String shipmentId;
    private String courierId;


    public AssignShipmentRequest() {
    }

    public AssignShipmentRequest(String shipmentId, String courierId) {
        this.shipmentId = shipmentId;
        this.courierId = courierId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }
}
