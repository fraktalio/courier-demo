package com.fraktalio.courier.command.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Date;

public record AuditEntry(String who, Date when, Collection<String> auth) {

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
