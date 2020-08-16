package com.fraktalio.courier.command;

import com.fraktalio.courier.web.configuration.SpringSecurityReactorMessageDispatchInterceptor;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.Collections;

import static com.fraktalio.courier.command.api.commands.CreateCourierCommand;
import static com.fraktalio.courier.command.api.events.CourierCreatedEvent;
import static com.fraktalio.courier.command.api.valueObjects.AuditEntry;
import static com.fraktalio.courier.command.api.valueObjects.PersonName;

public class CourierTest {

    private AggregateTestFixture<Courier> testFixture;

    @BeforeEach
    void setUp() {
        testFixture = new AggregateTestFixture<>(Courier.class);
        testFixture.registerCommandDispatchInterceptor(new SpringSecurityReactorMessageDispatchInterceptor<>());
    }

    @Test
    void createCourierTest(){
        PersonName personName = new PersonName("Ivan", "Dugalic");
        CreateCourierCommand createCourierCommand = new CreateCourierCommand(personName, 10);
        CourierCreatedEvent courierCreatedEvent = new CourierCreatedEvent(createCourierCommand.targetAggregateIdentifier(), personName, 10, new AuditEntry("anonymous",
                                                                                                                                                           Calendar.getInstance()
                                                                                                                                                                   .getTime(),
                                                                                                                                                           Collections.singletonList("anonymous")) );
        testFixture.given()
                   .when(createCourierCommand)
                   .expectEvents(courierCreatedEvent);

    }

}
