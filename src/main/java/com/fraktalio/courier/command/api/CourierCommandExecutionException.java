package com.fraktalio.courier.command.api;

import org.axonframework.commandhandling.CommandExecutionException;

public class CourierCommandExecutionException extends CommandExecutionException {

    public CourierCommandExecutionException(String message, Throwable cause, Object details) {
        super(message, cause, details);
    }
}
