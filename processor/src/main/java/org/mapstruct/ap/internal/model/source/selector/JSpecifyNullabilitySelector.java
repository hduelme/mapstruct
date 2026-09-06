/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.internal.model.source.selector;

import java.util.List;

import org.mapstruct.ap.internal.model.source.Method;

public class JSpecifyNullabilitySelector implements MethodSelector {
    @Override
    public <T extends Method> List<SelectedMethod<T>> getMatchingMethods(List<SelectedMethod<T>> candidates,
                                                                         SelectionContext context) {
        // Todo this must be used ensure own methods nullability return type matches the target one
        // This only appllies for Jspecify
        return candidates;
    }
}
