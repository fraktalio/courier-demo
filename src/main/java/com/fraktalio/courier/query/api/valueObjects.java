package com.fraktalio.courier.query.api;

public class valueObjects {

    public static record CourierId(String identifier) {

        @Override
        public String toString() {
            return identifier;
        }
    }

    public static record PersonName(String firstName, String lastName) {

    }

    public static record ShipmentId(String identifier) {

        @Override
        public String toString() {
            return identifier;
        }
    }
}
