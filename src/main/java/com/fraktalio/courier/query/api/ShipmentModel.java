package com.fraktalio.courier.query.api;

import com.fraktalio.courier.command.api.ShipmentState;

public record ShipmentModel(String aggregateIdentifier, String courierId, ShipmentState state,
                            String address) {

}
