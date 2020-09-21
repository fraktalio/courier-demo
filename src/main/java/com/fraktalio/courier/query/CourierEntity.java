package com.fraktalio.courier.query;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
class CourierEntity {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Integer maxNumberOfActiveOrders;
    private Integer numberOfActiveOrders;

    protected CourierEntity() {
    }

    CourierEntity(String id, String firstName, String lastName, Integer maxNumberOfActiveOrders,
                  Integer numberOfActiveOrders) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.maxNumberOfActiveOrders = maxNumberOfActiveOrders;
        this.numberOfActiveOrders = numberOfActiveOrders;
    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    String getFirstName() {
        return firstName;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    String getLastName() {
        return lastName;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    Integer getMaxNumberOfActiveOrders() {
        return maxNumberOfActiveOrders;
    }

    void setMaxNumberOfActiveOrders(Integer maxNumberOfActiveOrders) {
        this.maxNumberOfActiveOrders = maxNumberOfActiveOrders;
    }

    Integer getNumberOfActiveOrders() {
        return numberOfActiveOrders;
    }

    void setNumberOfActiveOrders(Integer numberOfActiveOrders) {
        this.numberOfActiveOrders = numberOfActiveOrders;
    }
}
