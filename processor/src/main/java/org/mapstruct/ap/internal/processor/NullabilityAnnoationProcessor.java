package org.mapstruct.ap.internal.processor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import org.mapstruct.ap.internal.model.Annotation;
import org.mapstruct.ap.internal.model.GeneratedTypeMethod;
import org.mapstruct.ap.internal.model.Mapper;
import org.mapstruct.ap.internal.model.MappingMethod;
import org.mapstruct.ap.internal.model.NormalTypeMappingMethod;
import org.mapstruct.ap.internal.model.common.Parameter;
import org.mapstruct.ap.internal.model.common.Type;
import org.mapstruct.ap.internal.util.ElementUtils;
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

        NullabilityResolver.JspecifyNullabilityScope packageNullabilityScope =
                nullabilityResolver.getPackageNullabilityScope(
                        getPackageElement( context.getElementUtils(), mapper.getPackageName() ) );
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

    private PackageElement getPackageElement(ElementUtils elementUtils, String packageName) {
        if ( packageName.contains( "." ) ) {
            // eclipse 1.6 has a problem when resolving a none existing package and creating it afterward.
            // After the bug is fixed by upgrading eclipse this methode can be replaced
            // with elementUtils.getPackageElement
            String parent = packageName.substring( 0, packageName.lastIndexOf( "." ) );
            PackageElement packageElement = elementUtils.getPackageElement( parent );
            if ( packageElement == null ) {
                return null;
            }
            List<PackageElement> packageElements = packageElement.getEnclosedElements().stream()
                    .filter( PackageElement.class::isInstance )
                    .map( PackageElement.class::cast )
                    .filter( p -> p.getQualifiedName().contentEquals( packageName ) )
                    .collect( Collectors.toList() );
            if ( packageElements.size() == 1 ) {
                // only if unique identified
                return packageElements.get( 0 );
            }
            return null;
        }
        else {
            return elementUtils.getPackageElement( packageName );
        }

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
