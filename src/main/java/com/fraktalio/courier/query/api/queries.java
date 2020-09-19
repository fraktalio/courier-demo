package com.fraktalio.courier.query.api;

import static com.fraktalio.courier.query.api.valueObjects.CourierId;
import static com.fraktalio.courier.query.api.valueObjects.ShipmentId;

public class queries {

    public static record FindAllCouriers() {

    }

    public static record FindCourier(CourierId courierId) {

    }

    public static record FindAllShipments() {

    }

    public static record FindShipment(ShipmentId shipmentId) {

    }
}
