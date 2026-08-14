package org.mapstruct.ap.test.nullcheck.jspecify.builtin;

import org.junit.jupiter.api.extension.RegisterExtension;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.source.BuildInSource;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.target.BuildInTarget;
import org.mapstruct.ap.testutil.ProcessorTest;
import org.mapstruct.ap.testutil.WithClasses;
import org.mapstruct.ap.testutil.WithJSpecify;
import org.mapstruct.ap.testutil.WithPackageInfo;
import org.mapstruct.ap.testutil.runner.GeneratedSource;

@WithJSpecify
@WithClasses( {BuildInTarget.class, BuildInSource.class, BuildInMapper.class } )
public class JspecifyBuildInTest {

    @RegisterExtension
    final GeneratedSource generatedSource = new GeneratedSource();

    @ProcessorTest
    @WithPackageInfo( BuildInSource.class )
    public void testJspecifyBuildInSourceNoneNullableTargetNullable() {
        generatedSource.addComparisonToFixtureFor( BuildInMapper.class, "SourceNoneNullable" );
        // Todo assertions
    }

    @ProcessorTest
    @WithPackageInfo( { BuildInTarget.class, BuildInSource.class } )
    public void testJspecifyBuildInSourceNoneNullableTargetNoneNullable() {
        generatedSource.addComparisonToFixtureFor( BuildInMapper.class, "SourceNoneNullable" );
        // Todo assertions
    }

    @ProcessorTest
    @WithPackageInfo( BuildInTarget.class )
    public void testJspecifyBuildInSourceNullableTargetNoneNullable() {
        generatedSource.addComparisonToFixtureFor( BuildInMapper.class, "SourceNullable" );
        // Todo assertions
    }


}
