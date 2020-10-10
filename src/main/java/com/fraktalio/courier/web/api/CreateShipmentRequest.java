package com.fraktalio.courier.web.api;

public class CreateShipmentRequest {

    private String city;
    private String street;


    public CreateShipmentRequest() {
    }

    public CreateShipmentRequest(String city, String street) {
        this.city = city;
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
