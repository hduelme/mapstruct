/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.test.bugs._3948;

import org.mapstruct.Mapper;

/**
 * @author hdulme
 */
@Mapper
public interface Issue3948Mapper {

    Target map(Source source);

    default <T> CompareWithSelfReferenceGenerics<T> longToCompareWithSelfReferenceGenerics(Long id) {
        return id == null ? null : new CompareWithSelfReferenceGenerics<>(id);
    }

    default ComparableField convert(String code) {
        return code == null ? null : new ComparableField(code);
    }

    class CompareWithSelfReferenceGenerics<T> implements Comparable<CompareWithSelfReferenceGenerics<T>> {
        private final long value;

        public CompareWithSelfReferenceGenerics(long value) {
            this.value = value;
        }

        @Override
        public int compareTo(CompareWithSelfReferenceGenerics<T> o) {
            return  Long.compare( this.value, o.value );
        }
    }

    class ComparableField implements Comparable<ComparableField> {

        private final String code;

        public ComparableField(String code) {
            this.code = code;
        }

        @Override
        public int compareTo(ComparableField o) {
            return code.compareTo( o.code );
        }
    }

    class Source {
        private long id;
        private String code;

        public String getCode() {
            return code;
        }

        public long getId() {
            return id;
        }
    }

    class Target {
        private ComparableField code;
        private CompareWithSelfReferenceGenerics<Target> id;

        public void setCode(ComparableField code) {
            this.code = code;
        }

        public void setId(CompareWithSelfReferenceGenerics<Target> id) {
            this.id = id;
        }
    }
}
