package com.fraktalio.courier.command.configuration;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.messaging.interceptors.LoggingInterceptor;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class CommandConfiguration {

    // ############### Command & Event Bus Interceptors ###############

    @Autowired
    public void registerCommandInterceptorsOnBus(CommandBus commandBus) {
        commandBus.registerHandlerInterceptor(new LoggingInterceptor<>());
    }

    @Autowired
    public void registerEventInterceptors(EventBus eventBus) {
        eventBus.registerDispatchInterceptor(new LoggingInterceptor<>());
    }

    @Autowired
    public void configure(EventProcessingConfigurer config) {
        config.registerDefaultHandlerInterceptor((t, u) -> new LoggingInterceptor<>(u));
    }

    // ############### Aggregate Cache ###############

    @Bean("cache")
    public org.axonframework.common.caching.Cache cache() {
        return new WeakReferenceCache();
    }

    // ############### Aggregate Snapshotting ###############

    @Bean
    public SpringAggregateSnapshotterFactoryBean snapshotter() {
        SpringAggregateSnapshotterFactoryBean springAggregateSnapshotterFactoryBean = new SpringAggregateSnapshotterFactoryBean();
        //Setting async executors
        springAggregateSnapshotterFactoryBean.setExecutor(Executors.newSingleThreadExecutor());
        return springAggregateSnapshotterFactoryBean;
    }

    @Bean("courierSnapshotTriggerDefinition")
    EventCountSnapshotTriggerDefinition courierSnapshotTriggerDefinition(Snapshotter snapshotter, CourierProperties courierProperties) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, courierProperties.snapshotTriggerThresholdCourier());
    }

    @Bean("shipmentSnapshotTriggerDefinition")
    EventCountSnapshotTriggerDefinition shipmentSnapshotTriggerDefinition(Snapshotter snapshotter, CourierProperties courierProperties) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, courierProperties.snapshotTriggerThresholdShipment());
    }
}
