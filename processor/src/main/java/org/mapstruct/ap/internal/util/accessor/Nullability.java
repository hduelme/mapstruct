/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.internal.util.accessor;

import java.util.Objects;
import java.util.Optional;
import javax.lang.model.type.TypeMirror;

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

    public static Nullability voidNullability() {
        return new Nullability( NullabilityState.NON_NULL, NullabilityCause.VOID );
    }

    /**
     * Resolves the nullability for a primitive type.
     *
     * @param typeMirror the type to inspect
     * @return an {@code Optional} containing a {@code (NON_NULL, PRIMITIVE)} nullability if the type is a
     *         primitive, otherwise an empty {@code Optional} (including for a {@code null} type)
     */
    public static Optional<Nullability> getPrimitiveNullability( TypeMirror typeMirror ) {
        if ( typeMirror != null && typeMirror.getKind().isPrimitive() ) {
            return Optional.of( new Nullability( NullabilityState.NON_NULL, NullabilityCause.PRIMITIVE ) );
        }
        return Optional.empty();
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

    @Override
    public String toString() {
        return "Nullability{" +
                "state=" + state +
                ", cause=" + cause +
                '}';
    }
}
