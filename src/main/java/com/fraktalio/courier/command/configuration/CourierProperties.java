package com.fraktalio.courier.command.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "courier")
public record CourierProperties(Integer snapshotTriggerThresholdCourier, Integer snapshotTriggerThresholdShipment) {

    public CourierProperties() {
        this(100, 100);
    }
}
