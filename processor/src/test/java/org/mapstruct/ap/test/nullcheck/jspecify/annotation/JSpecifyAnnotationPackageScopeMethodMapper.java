package org.mapstruct.ap.test.nullcheck.jspecify.annotation;

import org.jspecify.annotations.NullUnmarked;
import org.mapstruct.Mapper;
import org.mapstruct.ap.test.nullcheck.jspecify.SimpleSource;
import org.mapstruct.ap.test.nullcheck.jspecify.SimpleTarget;

@Mapper
public interface JSpecifyAnnotationPackageScopeMethodMapper {

    @NullUnmarked
    SimpleTarget map(SimpleSource source);
}
