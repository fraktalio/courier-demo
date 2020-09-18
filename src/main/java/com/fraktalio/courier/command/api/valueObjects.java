package com.fraktalio.courier.command.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

public class valueObjects {

    public static record Address(String city, String street) {

    }

    public static record CourierId(String identifier) {

        public CourierId() {
            this(UUID.randomUUID().toString());
        }

        @Override
        public String toString() {
            return identifier;
        }
    }

    public static record PersonName(String firstName, String lastName) {

    }

    public static record ShipmentId(String identifier) {

        public ShipmentId() {
            this(UUID.randomUUID().toString());
        }

        @Override
        public String toString() {
            return identifier;
        }
    }

    public static enum ShipmentState {
        CREATED, ASSIGNED, DELIVERED, CANCEL_PENDING, CANCELLED
    }

    public static record AuditEntry(String who, Date when, Collection<String> auth) {

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            AuditEntry that = (AuditEntry) o;

            return new EqualsBuilder()
                    .append(who, that.who)
                    .append(auth, that.auth)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(who)
                    .append(auth)
                    .toHashCode();
        }
    }
}
