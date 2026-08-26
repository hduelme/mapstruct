/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.test.nullcheck.jspecify.builtin;

import org.junit.jupiter.api.extension.RegisterExtension;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.sources.BuildInSource;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.sources.JakartaJaxbElementListProperty;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.sources.JakartaJaxbElementProperty;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.sources.JaxbElementListProperty;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.sources.JaxbElementProperty;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.targets.BigDecimalProperty;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.targets.BuildInTarget;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.targets.SomeType;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.targets.SomeTypeProperty;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.targets.StringProperty;
import org.mapstruct.ap.testutil.ProcessorTest;
import org.mapstruct.ap.testutil.WithClasses;
import org.mapstruct.ap.testutil.WithJSpecify;
import org.mapstruct.ap.testutil.WithJakartaJaxb;
import org.mapstruct.ap.testutil.WithJavaxJaxb;
import org.mapstruct.ap.testutil.WithPackageInfo;
import org.mapstruct.ap.testutil.runner.GeneratedSource;

@WithJSpecify
public class JspecifyBuildInTest {

    @RegisterExtension
    final GeneratedSource generatedSource = new GeneratedSource();

    @ProcessorTest
    @WithClasses( {
            JaxbMapper.class,
            JaxbElementProperty.class,
            StringProperty.class,
            BigDecimalProperty.class,
            SomeTypeProperty.class,
            SomeType.class
    } )
    @WithJavaxJaxb
    @WithPackageInfo( BuildInSource.class )
    public void testJspecifyJAXBElementBuildInSourceNoneNullableTargetNullable()  {
        generatedSource.addComparisonToFixtureFor( JaxbMapper.class, "TargetNullable" );
    }


    @ProcessorTest
    @WithClasses( {
            JaxbMapper.class,
            JaxbElementProperty.class,
            StringProperty.class,
            BigDecimalProperty.class,
            SomeTypeProperty.class,
            SomeType.class
    } )
    @WithJavaxJaxb
    @WithPackageInfo( { BuildInTarget.class, BuildInSource.class } )
    public void testJspecifyJAXBElementBuildInSourceNoneNullableTargetNoneNullable()  {
        generatedSource.addComparisonToFixtureFor( JaxbMapper.class, "TargetNoneNullable" );
    }

    @ProcessorTest
    @WithClasses( {
            JaxbMapper.class,
            JaxbElementProperty.class,
            StringProperty.class,
            BigDecimalProperty.class,
            SomeTypeProperty.class,
            SomeType.class
    } )
    @WithJavaxJaxb
    @WithPackageInfo( { BuildInTarget.class } )
    public void testJspecifyJAXBElementBuildInSourceNullableTargetNoneNullable()  {
        generatedSource.addComparisonToFixtureFor( JaxbMapper.class, "TargetNoneNullable" );
    }

    @ProcessorTest
    @WithClasses( {
            JakartaJaxbMapper.class,
            JakartaJaxbElementProperty.class,
    } )
    @WithJakartaJaxb
    public void shouldApplyBuiltInOnJakartaJaxbElement()  {
    }

    @ProcessorTest
    @WithClasses( {
            JaxbMapper.class,
            JaxbElementProperty.class,
    } )
    @WithJavaxJaxb
    public void shouldApplyBuiltInOnJAXBElementExtra()  {
    }

    @ProcessorTest
    @WithClasses( {
            JakartaJaxbMapper.class,
            JakartaJaxbElementProperty.class,
    } )
    @WithJakartaJaxb
    public void shouldApplyBuiltInOnJakartaJAXBElementExtra()  {
    }

    @ProcessorTest
    @WithClasses( {
            JaxbListMapper.class,
            JaxbElementListProperty.class,
    } )
    @WithJavaxJaxb
    public void shouldApplyBuiltInOnJAXBElementList() {
    }

    @ProcessorTest
    @WithClasses( {
            JakartaJaxbListMapper.class,
            JakartaJaxbElementListProperty.class,
    } )
    @WithJakartaJaxb
    public void shouldApplyBuiltInOnJakartaJAXBElementList() {
    }

    @ProcessorTest
    @WithClasses( {BuildInTarget.class, BuildInSource.class, BuildInMapper.class } )
    @WithPackageInfo( BuildInSource.class )
    public void testJspecifyBuildInSourceNoneNullableTargetNullable() {
        generatedSource.addComparisonToFixtureFor( BuildInMapper.class, "SourceNoneNullable" );
    }

    @ProcessorTest
    @WithClasses( {BuildInTarget.class, BuildInSource.class, BuildInMapper.class } )
    @WithPackageInfo( { BuildInTarget.class, BuildInSource.class } )
    public void testJspecifyBuildInSourceNoneNullableTargetNoneNullable() {
        generatedSource.addComparisonToFixtureFor( BuildInMapper.class, "SourceNoneNullable" );
    }

    @ProcessorTest
    @WithClasses( {BuildInTarget.class, BuildInSource.class, BuildInMapper.class } )
    @WithPackageInfo( BuildInTarget.class )
    public void testJspecifyBuildInSourceNullableTargetNoneNullable() {
        generatedSource.addComparisonToFixtureFor( BuildInMapper.class, "SourceNullable" );
    }

}
