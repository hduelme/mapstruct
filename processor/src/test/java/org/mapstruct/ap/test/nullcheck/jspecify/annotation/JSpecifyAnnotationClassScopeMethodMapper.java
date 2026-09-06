package org.mapstruct.ap.test.nullcheck.jspecify.annotation;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.mapstruct.Mapper;
import org.mapstruct.ap.test.nullcheck.jspecify.SimpleSource;
import org.mapstruct.ap.test.nullcheck.jspecify.SimpleTarget;

@Mapper
@NullMarked
public interface JSpecifyAnnotationClassScopeMethodMapper {

    SimpleTarget mapMarked(SimpleSource source);

    @NullUnmarked
    SimpleTarget mapUnmarked(SimpleSource source);
}
