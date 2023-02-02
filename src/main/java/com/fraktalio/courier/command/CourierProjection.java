package com.fraktalio.courier.command;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Query side aggregate model - Immediately consistent
 */
@Entity(name = "CmdCourierEntity")
class CourierProjection {

    @Id
    private String id;
    private Integer maxNumberOfActiveOrders;
    private Integer numberOfActiveOrders;

    protected CourierProjection() {
    }

    CourierProjection(String id, Integer maxNumberOfActiveOrders, Integer numberOfActiveOrders) {
        this.id = id;
        this.maxNumberOfActiveOrders = maxNumberOfActiveOrders;
        this.numberOfActiveOrders = numberOfActiveOrders;
    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
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
