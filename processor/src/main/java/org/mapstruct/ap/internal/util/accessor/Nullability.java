/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
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
