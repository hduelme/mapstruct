package org.mapstruct.ap.test.bugs._3485;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface Issue3485Mapper {

    Issue3485Mapper INSTANCE = Mappers.getMapper( Issue3485Mapper.class );
    class Target {
        private final String value;

        public Target( String value ) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    @Mapping(target = ".")
    Target targetFromExpression(String s);
}
