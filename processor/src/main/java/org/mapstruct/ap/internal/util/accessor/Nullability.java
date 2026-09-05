/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.internal.util.accessor;

import java.util.Objects;

/**
 * Represents the nullability of the element
 */
public class Nullability {

    private final NullabilityState state;
    private final NullabilityCause cause;

    Nullability(NullabilityState state, NullabilityCause cause) {
        this.state = state;
        this.cause = cause;
    }

    public static Nullability hardcodedNullability(NullabilityState state) {
        return new Nullability(state, NullabilityCause.HARDCODED);
    }

    public enum NullabilityState {
        NULLABLE,
        NON_NULL
    }

    public enum NullabilityCause {
        HARDCODED,
        JSPECIFY,
        DEFAULT,
        PRIMITIVE,
        VOID
    }

    public boolean isNullable() {
        return state == NullabilityState.NULLABLE;
    }

    public boolean isNonNullable() {
        return state == NullabilityState.NON_NULL;
    }

    public NullabilityState getState() {
        return state;
    }

    public NullabilityCause getCause() {
        return cause;
    }

    public Nullability chain(Nullability parent) {
        if ( parent.state == NullabilityState.NULLABLE ) {
            return parent;
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Nullability that = (Nullability) o;
        return state == that.state && cause == that.cause;
    }

    @Override
    public int hashCode() {
        return Objects.hash( state, cause );
    }
}
