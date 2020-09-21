package com.fraktalio.courier.query.configuration;

import org.axonframework.messaging.interceptors.LoggingInterceptor;
import org.axonframework.queryhandling.QueryBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourierQueryConfiguration {

    /************************************************/
    /* Register interceptors on the bus */

    /************************************************/

    @Autowired
    public void registerQueryInterceptorsOnBus(QueryBus queryBus) {
        queryBus.registerHandlerInterceptor(new LoggingInterceptor<>());
    }
}
