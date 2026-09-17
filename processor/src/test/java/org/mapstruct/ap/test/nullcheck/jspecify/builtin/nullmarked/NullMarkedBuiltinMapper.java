/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.test.nullcheck.jspecify.builtin.nullmarked;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NullMarkedBuiltinMapper {
    NullMarkedBuiltinMapper INSTANCE = Mappers.getMapper( NullMarkedBuiltinMapper.class );

    @Mapping(target = "str", source = "xml", dateFormat = "dd.MM.yyyy" )
    @Mapping(target = "xml", source = "str", dateFormat = "dd.MM.yyyy" )
    NullMarkedBuiltinTarget map(NullMarkedBuiltinSource source);
}
