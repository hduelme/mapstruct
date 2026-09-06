/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.internal.model.assignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mapstruct.ap.internal.model.common.Assignment;
import org.mapstruct.ap.internal.model.common.Type;
import org.mapstruct.ap.internal.util.accessor.Nullability;

/**
 * Wraps the assignment in a target setter.
 *
 * @author Sjaak Derksen
 */
public class SetterWrapperOuter extends AssignmentWrapper {

    private final List<Type> thrownTypesToExclude;
    private final boolean setExplicitlyToNull;
    private final boolean setExplicitlyToDefault;
    private final boolean mustCastForNull;
    private final Type nullCastType;
    private final String v;
    private final Type returnType;

    public SetterWrapperOuter(Assignment rhs,
                              List<Type> thrownTypesToExclude,
                              boolean fieldAssignment,
                              boolean setExplicitlyToNull,
                              boolean setExplicitlyToDefault,
                              boolean mustCastForNull,
                              Type nullCastType,
                              String source, Type returnType) {

        super( rhs, fieldAssignment );
        this.thrownTypesToExclude = thrownTypesToExclude;
        this.setExplicitlyToDefault = setExplicitlyToDefault;
        this.setExplicitlyToNull = setExplicitlyToNull;
        this.mustCastForNull = mustCastForNull;
        this.nullCastType = nullCastType;
        this.v = source;
        this.returnType = returnType;
    }

    @Override
    public List<Type> getThrownTypes() {
        List<Type> parentThrownTypes = super.getThrownTypes();
        List<Type> result = new ArrayList<>( parentThrownTypes );
        for ( Type thrownTypeToExclude : thrownTypesToExclude ) {
            for ( Type parentThrownType : parentThrownTypes ) {
                if ( parentThrownType.isAssignableTo( thrownTypeToExclude ) ) {
                    result.remove( parentThrownType );
                }
            }
        }
        return result;
    }

    @Override
    public Set<Type> getImportTypes() {
        Set<Type> imported = new HashSet<>( super.getImportTypes() );
        if ( isSetExplicitlyToNull() && isMustCastForNull() ) {
            imported.add( nullCastType );
        }
        imported.add( returnType );
        return imported;
    }

    public boolean isSetExplicitlyToNull() {
        return setExplicitlyToNull;
    }

    public boolean isSetExplicitlyToDefault() {
        return setExplicitlyToDefault;
    }

    public boolean isIncludeSourceNullCheck() {
        return true;
    }

    public boolean isMustCastForNull() {
        return mustCastForNull;
    }

    @Override
    public Nullability getSourceNullability() {
       return Nullability.voidNullability();
    }

    @Override
    public String getSourceLocalVarName() {
        return v;
    }

    public Type getTargetType() {
        return returnType;
    }
}
