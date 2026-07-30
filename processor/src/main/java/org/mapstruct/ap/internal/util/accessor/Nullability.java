package org.mapstruct.ap.internal.util.accessor;

/**
 * Represents the nullability of the element
 */
public enum Nullability {
    NULLABLE,
    NON_NULL;

    public boolean needsNullCheck(Nullability source) {
        if ( this == NULLABLE ) {
            return false;
        }
        return source == NULLABLE;
    }
}
