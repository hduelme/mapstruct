package org.mapstruct.ap.internal.processor;

import javax.lang.model.element.TypeElement;

import org.mapstruct.ap.internal.model.Annotation;
import org.mapstruct.ap.internal.model.Mapper;
import org.mapstruct.ap.internal.util.accessor.NullabilityResolver;

import static org.mapstruct.ap.internal.util.accessor.JSpecifyConstants.NULL_MARKED_FQN;
import static org.mapstruct.ap.internal.util.accessor.JSpecifyConstants.NULL_UNMARKED_FQN;

public class NullabilityScopeProcessor implements ModelElementProcessor<Mapper, Mapper> {

    @Override
    public Mapper process(ProcessorContext context, TypeElement mapperTypeElement, Mapper mapper) {
        NullabilityResolver.JspecifyNullabilityScope jspecifyNullabilityScope =
                NullabilityResolver.findScopeAnnotation(  mapperTypeElement );
        if ( jspecifyNullabilityScope != NullabilityResolver.JspecifyNullabilityScope.UNKNOWN ) {
            mapper.addAnnotation( new Annotation( context.getTypeFactory().getType(
                    jspecifyNullabilityScope == NullabilityResolver.JspecifyNullabilityScope.NULL_MARKED
                            ? NULL_MARKED_FQN : NULL_UNMARKED_FQN ) ) );
            // Todo test NULL_UNMARKED_FQN propergation
        }
        return mapper;
    }

    @Override
    public int getPriority() {
        return 1200;
    }
}
