package com.fraktalio.courier.command;

import com.fraktalio.api.AuditEntry;
import com.fraktalio.courier.command.api.*;
import com.fraktalio.courier.web.configuration.SpringSecurityReactorMessageDispatchInterceptor;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;

public class ShipmentTest {

    private AggregateTestFixture<Shipment> testFixture;

    private CourierProjectionRepository courierProjectionRepository;

    @BeforeEach
    void setUp() {
        courierProjectionRepository = Mockito.mock(CourierProjectionRepository.class);
        testFixture = new AggregateTestFixture<>(Shipment.class);
        testFixture.registerCommandDispatchInterceptor(new SpringSecurityReactorMessageDispatchInterceptor<>());
        testFixture.registerInjectableResource(courierProjectionRepository);
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
        Mockito.when(courierProjectionRepository.findById(courierId.identifier())).thenReturn(
                Optional.of(new CourierProjection(shipmentId.identifier(), 3, 1)
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
        Mockito.when(courierProjectionRepository.findById(courierId.identifier())).thenReturn(
                Optional.of(new CourierProjection(shipmentId.identifier(), 3, 3)
                ));
        var assignShipmentCommand = new AssignShipmentCommand(shipmentId, courierId);
        var shipmentNotAssignedEvent = new ShipmentNotAssignedEvent(shipmentId, courierId, auditEntry);
        testFixture.given(shipmentCreatedEvent)
                .when(assignShipmentCommand)
                .expectEvents(shipmentNotAssignedEvent);
    }

    @Test
    void assignShipmentFailedWithExceptionTest() {
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
        Mockito.when(courierProjectionRepository.findById(courierId.identifier())).thenReturn(
                Optional.of(new CourierProjection(shipmentId.identifier(), 3, 3)
                ));
        var assignShipmentCommand = new AssignShipmentCommand(shipmentId, courierId);
        var shipmentNotAssignedEvent = new ShipmentNotAssignedEvent(shipmentId, courierId, auditEntry);
        testFixture.given(shipmentCreatedEvent, shipmentAssignedEvent)
                .when(assignShipmentCommand)
                .expectException(CommandExecutionException.class);
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
        var markShipmentAsDeliveredCommand = new MarkShipmentAsDeliveredCommand(shipmentId, courierId);
        var shipmentDeliveredEvent = new ShipmentDeliveredEvent(shipmentId, courierId, auditEntry);

        testFixture.given(shipmentCreatedEvent, shipmentAssignedEvent)
                .when(markShipmentAsDeliveredCommand)
                .expectEvents(shipmentDeliveredEvent);
    }

    @Test
    void shipmentDeliveredWithExceptionTest() {
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
        var markShipmentAsDeliveredCommand = new MarkShipmentAsDeliveredCommand(shipmentId, courierId);

        testFixture.given(shipmentCreatedEvent)
                .when(markShipmentAsDeliveredCommand)
                .expectException(CommandExecutionException.class);
    }
}
