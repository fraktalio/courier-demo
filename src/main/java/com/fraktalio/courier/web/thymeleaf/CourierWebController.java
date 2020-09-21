package com.fraktalio.courier.web.thymeleaf;

import com.fraktalio.courier.command.api.CourierId;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import com.fraktalio.courier.command.api.CreateCourierCommand;

import com.fraktalio.courier.query.api.FindAllCouriersQuery;

import com.fraktalio.courier.query.api.CourierModel;
import com.fraktalio.courier.web.api.CreateCourierRequest;

@Controller
public class CourierWebController {

    private final ReactorCommandGateway reactorCommandGateway;
    private final ReactorQueryGateway reactorQueryGateway;

    public CourierWebController(
            ReactorCommandGateway reactorCommandGateway,
            ReactorQueryGateway reactorQueryGateway) {
        this.reactorCommandGateway = reactorCommandGateway;
        this.reactorQueryGateway = reactorQueryGateway;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping(value = "/couriers-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Mono<String> couriersSSE(Model model) {
        Flux<CourierModel> result =
                reactorQueryGateway.subscriptionQueryMany(new FindAllCouriersQuery(), CourierModel.class);
        model.addAttribute("couriers", new ReactiveDataDriverContextVariable(result, 1));
        return Mono.just("sse/couriers-sse");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/couriers")
    Mono<String> couriers(Model model) {
        model.addAttribute("createCourierRequest", new CreateCourierRequest());
        return Mono.just("couriers");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/couriers")
    Mono<String> addCourier(@Valid @ModelAttribute CreateCourierRequest createCourierRequest,
                            BindingResult bindingResult,
                            Model model
    ) {

        var command = new CreateCourierCommand(createCourierRequest.getFirstName(), createCourierRequest.getLastName(),
                                               createCourierRequest.getMaxNumberOfActiveOrders());
        Mono<CourierId> result  = reactorCommandGateway.send(command);

        return Mono
                .just(bindingResult)
                .map(Errors::hasErrors)
                .filter(aBoolean -> aBoolean==true)
                .flatMap(aBoolean -> Mono.just("couriers"))
                .switchIfEmpty(result.thenReturn("redirect:/couriers"));
    }
}
