package com.fraktalio.courier.query;

import org.axonframework.config.ProcessingGroup;
import org.springframework.stereotype.Component;

/**
 * Tracking event processor - Eventual consistency
 */
@Component
@ProcessingGroup("CourierProcessor")
class CourierHandler {


}
