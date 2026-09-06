package org.mapstruct.ap.test.nullcheck.jspecify.annotation;

import org.junit.jupiter.api.extension.RegisterExtension;
import org.mapstruct.ap.test.nullcheck.jspecify.SimpleSource;
import org.mapstruct.ap.test.nullcheck.jspecify.SimpleTarget;
import org.mapstruct.ap.test.nullcheck.jspecify.packages.OuterPackageNullmarkedProvider;
import org.mapstruct.ap.testutil.ProcessorTest;
import org.mapstruct.ap.testutil.WithClasses;
import org.mapstruct.ap.testutil.WithJSpecify;
import org.mapstruct.ap.testutil.WithPackageInfo;
import org.mapstruct.ap.testutil.runner.GeneratedSource;

@WithJSpecify
@WithClasses({SimpleSource.class, SimpleTarget.class})
public class JSpecifyAnnotationTest {

    @RegisterExtension
    final GeneratedSource generatedSource = new GeneratedSource();

    @ProcessorTest
    @WithClasses( JSpecifyAnnotationMethodeMapper.class )
    void checkMethodeAnnotations() {
        generatedSource.addComparisonToFixtureFor( JSpecifyAnnotationMethodeMapper.class );
    }

    @ProcessorTest
    @WithClasses( JSpecifyAnnotationMethodeMapper.class )
    @WithPackageInfo( JSpecifyAnnotationMethodeMapper.class )
    void checkMethodeAnnotationsInNullMarkedTargetPackage() {
        generatedSource.addComparisonToFixtureFor( JSpecifyAnnotationMethodeMapper.class, "InNullMarkedTargetPackage" );
    }
}
