package com.fraktalio.courier.query;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Query side aggregate model - Immediately consistent
 */
@Entity
class CourierEntity {

    @Id
    private String id;
    private Integer maxNumberOfActiveOrders;
    private Integer numberOfActiveOrders;

    protected CourierEntity() {
    }

    CourierEntity(String id, Integer maxNumberOfActiveOrders, Integer numberOfActiveOrders) {
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
