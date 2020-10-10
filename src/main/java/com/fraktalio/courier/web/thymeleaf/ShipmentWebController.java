package com.fraktalio.courier.web.thymeleaf;

import com.fraktalio.courier.command.api.Address;
import com.fraktalio.courier.command.api.AssignShipmentCommand;
import com.fraktalio.courier.command.api.CourierId;
import com.fraktalio.courier.command.api.CreateShipmentCommand;
import com.fraktalio.courier.command.api.MarkShipmentAsDeliveredCommand;
import com.fraktalio.courier.command.api.ShipmentId;
import com.fraktalio.courier.query.api.CourierModel;
import com.fraktalio.courier.query.api.FindAllCouriersQuery;
import com.fraktalio.courier.query.api.FindAllShipmentsQuery;
import com.fraktalio.courier.query.api.ShipmentModel;
import com.fraktalio.courier.web.api.AssignShipmentRequest;
import com.fraktalio.courier.web.api.CreateShipmentRequest;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javax.validation.Valid;

@Controller
public class ShipmentWebController {

    private final ReactorCommandGateway reactorCommandGateway;
    private final ReactorQueryGateway reactorQueryGateway;
    private final QueryGateway queryGateway;

    public ShipmentWebController(
            ReactorCommandGateway reactorCommandGateway,
            ReactorQueryGateway reactorQueryGateway,
            QueryGateway queryGateway) {
        this.reactorCommandGateway = reactorCommandGateway;
        this.reactorQueryGateway = reactorQueryGateway;
        this.queryGateway = queryGateway;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping(value = "/shipments-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Mono<String> shipmentsSSE(Model model) throws ExecutionException, InterruptedException {
        Flux<ShipmentModel> result =
                reactorQueryGateway.subscriptionQueryMany(new FindAllShipmentsQuery(), ShipmentModel.class);
        model.addAttribute("shipments", new ReactiveDataDriverContextVariable(result, 1));
        model.addAttribute("assignShipmentRequest", new AssignShipmentRequest());


        var  couriers = queryGateway.query(new FindAllCouriersQuery(), ResponseTypes.multipleInstancesOf(CourierModel.class));
        model.addAttribute("couriers", couriers.get());

        return Mono.just("sse/shipments-sse");
    }

    @PreAuthorize("hasRole('COURIER')")
    @GetMapping(value = "/courier-shipments-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Mono<String> courieerShipmentsSSE(Model model) throws ExecutionException, InterruptedException {
        //TODO return Shipments for specific courier only
        Flux<ShipmentModel> result =
                reactorQueryGateway.subscriptionQueryMany(new FindAllShipmentsQuery(), ShipmentModel.class);
        model.addAttribute("shipments", new ReactiveDataDriverContextVariable(result, 1));
        model.addAttribute("assignShipmentRequest", new AssignShipmentRequest());


        var  couriers = queryGateway.query(new FindAllCouriersQuery(), ResponseTypes.multipleInstancesOf(CourierModel.class));
        model.addAttribute("couriers", couriers.get());

        return Mono.just("sse/courier-shipments-sse");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/shipments")
    Mono<String> shipments(Model model) {
        model.addAttribute("createShipmentRequest", new CreateShipmentRequest());
        return Mono.just("shipments");
    }

    @PreAuthorize("hasRole('COURIER')")
    @GetMapping("/courier-shipments")
    Mono<String> courier_shipments(Model model) {
        model.addAttribute("createShipmentRequest", new CreateShipmentRequest());
        return Mono.just("courier-shipments");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/shipments")
    Mono<String> addShipment(@Valid @ModelAttribute CreateShipmentRequest createShipmentRequest,
                             BindingResult bindingResult,
                             Model model
    ) {

        var command = new CreateShipmentCommand(new Address(createShipmentRequest.getCity(),
                                                            createShipmentRequest.getStreet()));
        Mono<ShipmentId> result = reactorCommandGateway.send(command);

        return Mono
                .just(bindingResult)
                .map(Errors::hasErrors)
                .filter(aBoolean -> aBoolean == true)
                .flatMap(aBoolean -> Mono.just("shipments"))
                .switchIfEmpty(result.thenReturn("redirect:/shipments"));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/shipments/{shipmentId}/assigned")
    Mono<String> assignShipment(@Valid @ModelAttribute AssignShipmentRequest assignShipmentRequest,
                                BindingResult bindingResult,
                                @PathVariable UUID shipmentId) {
        var command = new AssignShipmentCommand(new ShipmentId(shipmentId.toString()),
                                                new CourierId(assignShipmentRequest.getCourierId()));
        Mono<Void> result = reactorCommandGateway.send(command);

        return result.thenReturn("redirect:/shipments");
    }


    @PreAuthorize("hasRole('COURIER')")
    @PostMapping("/courier-shipments/{shipmentId}/courier/{courierId}/delivered")
    Mono<String> markShipmentDelivered(@PathVariable UUID shipmentId, @PathVariable UUID courierId) {

        var command = new MarkShipmentAsDeliveredCommand(new ShipmentId(shipmentId.toString()),
                                                         new CourierId(courierId.toString()));
        Mono<Void> result = reactorCommandGateway.send(command);

        return result.thenReturn("redirect:/courier-shipments");
    }
}
