package com.fraktalio.courier.command;

import com.fraktalio.api.AuditEntry;
import com.fraktalio.courier.command.api.CourierCreatedEvent;
import com.fraktalio.courier.command.api.CreateCourierCommand;
import com.fraktalio.courier.web.configuration.SpringSecurityReactorMessageDispatchInterceptor;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.Collections;

public class CourierTest {

    private AggregateTestFixture<Courier> testFixture;

    @BeforeEach
    void setUp() {
        testFixture = new AggregateTestFixture<>(Courier.class);
        testFixture.registerCommandDispatchInterceptor(new SpringSecurityReactorMessageDispatchInterceptor<>());
    }

    @Test
    void createCourierTest() {
        var createCourierCommand = new CreateCourierCommand("Ivan", "Dugalic", 10);
        var courierCreatedEvent = new CourierCreatedEvent(createCourierCommand.targetAggregateIdentifier(),
                                                          "Ivan",
                                                          "Dugalic",
                                                          10,
                                                          new AuditEntry("anonymous",
                                                                         Calendar.getInstance()
                                                                                 .getTime(),
                                                                         Collections.singletonList("anonymous")));
        testFixture.given()
                   .when(createCourierCommand)
                   .expectEvents(courierCreatedEvent);
    }
}
