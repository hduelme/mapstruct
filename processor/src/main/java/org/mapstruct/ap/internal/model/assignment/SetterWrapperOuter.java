/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.internal.model.assignment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mapstruct.ap.internal.model.common.Assignment;
import org.mapstruct.ap.internal.model.common.Type;

/**
 * Wraps the assignment in a target setter.
 *
 * @author Sjaak Derksen
 */
public class SetterWrapperOuter extends SetterWrapper {

    private final String targetVariableName;
    private final Type returnType;
    private final boolean needsSourceParameterNullCheck;

    public SetterWrapperOuter(Assignment rhs,
                              List<Type> thrownTypesToExclude,
                              boolean fieldAssignment,
                              boolean needsSourceParameterNullCheck,
                              boolean setExplicitlyToNull,
                              boolean setExplicitlyToDefault,
                              boolean mustCastForNull,
                              Type nullCastType,
                              String targetVariableName, Type returnType) {

        super( rhs, thrownTypesToExclude, fieldAssignment, true, setExplicitlyToNull,
                setExplicitlyToDefault, mustCastForNull, nullCastType );
        this.targetVariableName = targetVariableName;
        this.returnType = returnType;
        this.needsSourceParameterNullCheck = needsSourceParameterNullCheck;
    }

    @Override
    public Set<Type> getImportTypes() {
        Set<Type> imported = new HashSet<>( super.getImportTypes() );
        imported.add( returnType );
        return imported;
    }

    @Override
    public String getSourceLocalVarName() {
        return targetVariableName;
    }

    public boolean isNeedsSourceParameterNullCheck() {
        return needsSourceParameterNullCheck;
    }

    public Type getTargetType() {
        return returnType;
    }
}
