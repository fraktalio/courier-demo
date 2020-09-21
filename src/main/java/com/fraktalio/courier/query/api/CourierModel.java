package com.fraktalio.courier.query.api;

public class CourierModel{
    private String id;
    private String firstName;
    private String lastName;
    private Integer maxNumberOfActiveOrders;

    public CourierModel(String id, String firstName, String lastName, Integer maxNumberOfActiveOrders) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.maxNumberOfActiveOrders = maxNumberOfActiveOrders;
    }

    public CourierModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getMaxNumberOfActiveOrders() {
        return maxNumberOfActiveOrders;
    }

    public void setMaxNumberOfActiveOrders(Integer maxNumberOfActiveOrders) {
        this.maxNumberOfActiveOrders = maxNumberOfActiveOrders;
    }
}
