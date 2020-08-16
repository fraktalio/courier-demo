package com.fraktalio.courier.command.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "courier")
@ConstructorBinding
public record CourierProperties(Integer snapshotTriggerThresholdCourier, Integer snapshotTriggerThresholdShipment){

    public CourierProperties() {
        this(100, 100);
    }
}
