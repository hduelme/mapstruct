package org.mapstruct.ap.test.nullcheck.jspecify.annotation;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.ap.test.nullcheck.jspecify.SimpleSource;
import org.mapstruct.ap.test.nullcheck.jspecify.SimpleTarget;

@Mapper
@NullMarked
@NullUnmarked
public interface JSpecifyAnnotationScopeIgnoredMapper {

    @Nullable
    SimpleTarget mapTo(@NonNull SimpleSource simpleSource);
}
