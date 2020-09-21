package com.fraktalio.courier.web.api;

public class CreateCourierRequest {

    private String firstName;
    private String lastName;
    private Integer maxNumberOfActiveOrders;

    public CreateCourierRequest() {
    }

    public CreateCourierRequest(String firstName, String lastName, Integer maxNumberOfActiveOrders) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.maxNumberOfActiveOrders = maxNumberOfActiveOrders;
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
