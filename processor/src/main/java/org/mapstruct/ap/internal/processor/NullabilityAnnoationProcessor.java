/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.internal.processor;

import java.util.Optional;
import javax.lang.model.element.TypeElement;

import org.mapstruct.ap.internal.model.Annotation;
import org.mapstruct.ap.internal.model.GeneratedTypeMethod;
import org.mapstruct.ap.internal.model.Mapper;
import org.mapstruct.ap.internal.model.MappingMethod;
import org.mapstruct.ap.internal.model.NormalTypeMappingMethod;
import org.mapstruct.ap.internal.model.common.Parameter;
import org.mapstruct.ap.internal.model.common.Type;
import org.mapstruct.ap.internal.util.accessor.Nullability;
import org.mapstruct.ap.internal.util.accessor.NullabilityResolver;

import static org.mapstruct.ap.internal.util.accessor.JSpecifyConstants.NON_NULL_FQN;
import static org.mapstruct.ap.internal.util.accessor.JSpecifyConstants.NULLABLE_FQN;
import static org.mapstruct.ap.internal.util.accessor.JSpecifyConstants.NULL_MARKED_FQN;
import static org.mapstruct.ap.internal.util.accessor.JSpecifyConstants.NULL_UNMARKED_FQN;

public class NullabilityAnnoationProcessor implements ModelElementProcessor<Mapper, Mapper> {

    @Override
    public Mapper process(ProcessorContext context, TypeElement mapperTypeElement, Mapper mapper) {
        NullabilityResolver nullabilityResolver = context.getNullabilityResolver();
        if ( !nullabilityResolver.isjSpecifyEnabled() ) {
            return mapper;
        }
        NullabilityResolver.JspecifyNullabilityScope packageNullabilityScope =
                nullabilityResolver.getPackageNullabilityScope(
                        context.getElementUtils().getPackageElement( mapper.getPackageName() ) );
        NullabilityResolver.JspecifyNullabilityScope innerScope = packageNullabilityScope;
        NullabilityResolver.JspecifyNullabilityScope jspecifyNullabilityScope =
                nullabilityResolver.getParentTypeNullabilityScope(  mapperTypeElement );
        if ( packageNullabilityScope.needsScopeAnnotation( jspecifyNullabilityScope ) ) {
            mapper.addAnnotation( createNullabilityScopeAnnotation( context, jspecifyNullabilityScope ) );
            innerScope = jspecifyNullabilityScope;
            // Todo test NULL_UNMARKED_FQN propagation
        }
        for ( GeneratedTypeMethod method : mapper.getMethods() ) {
            if ( method instanceof MappingMethod ) {
                MappingMethod mappingMethod = (MappingMethod) method;
                if ( mappingMethod instanceof NormalTypeMappingMethod ) {
                    NormalTypeMappingMethod normalMappingMethod = (NormalTypeMappingMethod) method;
                    NullabilityResolver.JspecifyNullabilityScope methodeScopeNullability =
                            nullabilityResolver.getMethodeNullabilityScope( normalMappingMethod.getExecutable() );
                    if ( innerScope.needsScopeAnnotation( methodeScopeNullability ) ) {
                        Annotation annotation = createNullabilityScopeAnnotation( context, methodeScopeNullability );
                        normalMappingMethod.addAnnotation( annotation );
                        mapper.addExtraImportedType( annotation.getType() );
                        innerScope = methodeScopeNullability;
                    }
                }
                // Todo from here we need the return and param nullability check against methodeScopeNullability
                Nullability returnTypeNullability = mappingMethod.getReturnTypeNullability();
                if ( innerScope.needsAnnotation( returnTypeNullability ) ) {
                    createNullabilityAnnotation( context, returnTypeNullability )
                            .ifPresent( nullabilityAnnotation -> {
                                mappingMethod.addAnnotation( nullabilityAnnotation );
                                mapper.addExtraImportedType( nullabilityAnnotation.getType() );
                            } );
                }
                for ( Parameter parameter : mappingMethod.getParameters() ) {
                    if ( innerScope.needsAnnotation( parameter.getNullability() ) ) {
                        createNullabilityAnnotationType( context, parameter.getNullability() )
                                .ifPresent( nullabilityAnnotation -> {
                                    parameter.addAnnotation( nullabilityAnnotation );
                                    mapper.addExtraImportedType( nullabilityAnnotation );
                                } );
                    }
                }
            }
        }
        return mapper;
    }

    private static Annotation createNullabilityScopeAnnotation(ProcessorContext context,
                                                               NullabilityResolver.JspecifyNullabilityScope
                                                                       jspecifyNullabilityScope) {
        return new Annotation( context.getTypeFactory().getType(
                jspecifyNullabilityScope == NullabilityResolver.JspecifyNullabilityScope.NULL_MARKED
                        ? NULL_MARKED_FQN : NULL_UNMARKED_FQN ) );
    }

    private static Optional<Type> createNullabilityAnnotationType(ProcessorContext context, Nullability nullability) {
        String canonicalName = nullability.getState() == Nullability.NullabilityState.NON_NULL
                ? NON_NULL_FQN : NULLABLE_FQN;
        if ( !context.getTypeFactory().isTypeAvailable(  canonicalName ) ) {
            return Optional.empty();
        }
        return Optional.of( context.getTypeFactory().getType( canonicalName ) );
    }

    private static Optional<Annotation> createNullabilityAnnotation(ProcessorContext context,
                                                                   Nullability nullability) {
        return createNullabilityAnnotationType( context, nullability ).map( Annotation::new );
    }

    @Override
    public int getPriority() {
        return 1200;
    }
}
