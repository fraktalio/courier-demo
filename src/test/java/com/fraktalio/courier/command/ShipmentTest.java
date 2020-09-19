package com.fraktalio.courier.command;

import com.fraktalio.courier.command.api.commands;
import com.fraktalio.courier.web.configuration.SpringSecurityReactorMessageDispatchInterceptor;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;

import static com.fraktalio.courier.command.api.commands.AssignShipmentCommand;
import static com.fraktalio.courier.command.api.commands.CreateShipmentCommand;
import static com.fraktalio.courier.command.api.events.*;
import static com.fraktalio.courier.command.api.valueObjects.*;

public class ShipmentTest {

    private AggregateTestFixture<Shipment> testFixture;

    private CourierRepository courierRepository;

    @BeforeEach
    void setUp() {
        courierRepository = Mockito.mock(CourierRepository.class);
        testFixture = new AggregateTestFixture<>(Shipment.class);
        testFixture.registerCommandDispatchInterceptor(new SpringSecurityReactorMessageDispatchInterceptor<>());
        testFixture.registerInjectableResource(courierRepository);
    }

    @Test
    void createShipmentTest() {
        var address = new Address("city", "name");
        var shipmentId = new ShipmentId();
        var createShipmentCommand = new CreateShipmentCommand(shipmentId, address);
        var shipmentCreatedEvent = new ShipmentCreatedEvent(createShipmentCommand.targetAggregateIdentifier(),
                                                            address,
                                                            new AuditEntry("anonymous",
                                                                           Calendar.getInstance()
                                                                                   .getTime(),
                                                                           Collections.singletonList("anonymous")));

        testFixture.given()
                   .when(createShipmentCommand)
                   .expectEvents(shipmentCreatedEvent);
    }

    @Test
    void assignShipmentTest() {
        var address = new Address("city", "name");
        var shipmentId = new ShipmentId();
        var courierId = new CourierId();
        var auditEntry = new AuditEntry("anonymous",
                                        Calendar.getInstance()
                                                .getTime(),
                                        Collections.singletonList("anonymous"));

        var shipmentCreatedEvent = new ShipmentCreatedEvent(shipmentId,
                                                            address,
                                                            auditEntry);
        Mockito.when(courierRepository.findById(courierId.identifier())).thenReturn(
                Optional.of(new CourierEntity(shipmentId.identifier(), 3, 1)
                ));
        var assignShipmentCommand = new AssignShipmentCommand(shipmentId, courierId);
        var shipmentAssignedEvent = new ShipmentAssignedEvent(shipmentId, courierId, auditEntry);
        testFixture.given(shipmentCreatedEvent)
                   .when(assignShipmentCommand)
                   .expectEvents(shipmentAssignedEvent);
    }

    @Test
    void assignShipmentFailedTest() {
        var address = new Address("city", "name");
        var shipmentId = new ShipmentId();
        var courierId = new CourierId();
        var auditEntry = new AuditEntry("anonymous",
                                        Calendar.getInstance()
                                                .getTime(),
                                        Collections.singletonList("anonymous"));

        var shipmentCreatedEvent = new ShipmentCreatedEvent(shipmentId,
                                                            address,
                                                            auditEntry);
        Mockito.when(courierRepository.findById(courierId.identifier())).thenReturn(
                Optional.of(new CourierEntity(shipmentId.identifier(), 3, 3)
                ));
        var assignShipmentCommand = new AssignShipmentCommand(shipmentId, courierId);
        var shipmentNotAssignedEvent = new ShipmentNotAssignedEvent(shipmentId, courierId, auditEntry);
        testFixture.given(shipmentCreatedEvent)
                   .when(assignShipmentCommand)
                   .expectEvents(shipmentNotAssignedEvent);
    }

    @Test
    void shipmentDeliveredTest() {
        var address = new Address("city", "name");
        var shipmentId = new ShipmentId();
        var courierId = new CourierId();
        var auditEntry = new AuditEntry("anonymous",
                                        Calendar.getInstance()
                                                .getTime(),
                                        Collections.singletonList("anonymous"));

        var shipmentCreatedEvent = new ShipmentCreatedEvent(shipmentId,
                                                            address,
                                                            auditEntry);
        var shipmentAssignedEvent = new ShipmentAssignedEvent(shipmentId, courierId, auditEntry);
        var markShipmentAsDeliveredCommand = new commands.MarkShipmentAsDeliveredCommand(shipmentId, courierId);
        var shipmentDeliveredEvent = new ShipmentDeliveredEvent(shipmentId, courierId, auditEntry);

        testFixture.given(shipmentCreatedEvent, shipmentAssignedEvent)
                   .when(markShipmentAsDeliveredCommand)
                   .expectEvents(shipmentDeliveredEvent);
    }
}
