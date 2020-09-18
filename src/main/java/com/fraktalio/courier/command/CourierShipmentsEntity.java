package com.fraktalio.courier.command;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Query side aggregate model - Immediately consistent
 */
@Entity
class CourierShipmentsEntity {

    @Id
    private String id;
    private Integer maxNumberOfActiveOrders;
    private Integer numberOfActiveOrders;

    public CourierShipmentsEntity() {
    }

    public CourierShipmentsEntity(String id, Integer maxNumberOfActiveOrders, Integer numberOfActiveOrders) {
        this.id = id;
        this.maxNumberOfActiveOrders = maxNumberOfActiveOrders;
        this.numberOfActiveOrders = numberOfActiveOrders;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMaxNumberOfActiveOrders() {
        return maxNumberOfActiveOrders;
    }

    public void setMaxNumberOfActiveOrders(Integer maxNumberOfActiveOrders) {
        this.maxNumberOfActiveOrders = maxNumberOfActiveOrders;
    }

    public Integer getNumberOfActiveOrders() {
        return numberOfActiveOrders;
    }

    public void setNumberOfActiveOrders(Integer numberOfActiveOrders) {
        this.numberOfActiveOrders = numberOfActiveOrders;
    }
}
