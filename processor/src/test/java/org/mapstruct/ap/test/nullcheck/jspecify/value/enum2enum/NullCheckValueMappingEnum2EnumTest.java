/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.test.nullcheck.jspecify.value.enum2enum;

import javax.tools.Diagnostic.Kind;

import org.junit.jupiter.api.extension.RegisterExtension;
import org.mapstruct.ap.test.value.ExternalOrderType;
import org.mapstruct.ap.test.value.OrderType;
import org.mapstruct.ap.testutil.ProcessorTest;
import org.mapstruct.ap.testutil.WithClasses;
import org.mapstruct.ap.testutil.WithJSpecify;
import org.mapstruct.ap.testutil.compilation.annotation.CompilationResult;
import org.mapstruct.ap.testutil.compilation.annotation.Diagnostic;
import org.mapstruct.ap.testutil.compilation.annotation.ExpectedCompilationOutcome;
import org.mapstruct.ap.testutil.runner.GeneratedSource;

/**
 * JSpecify nullability checks for {@code @ValueMapping} methods that map an enum to an enum.
 *
 * <p>Cases 1-2 cover the null-guard skip (verified via generated-source fixture).
 *
 * <p>Cases 3-6 expect JSpecify-aware diagnostics that do not exist yet on {@code ValueMappingMethod}; they are
 * expected to fail until a JSpecify integration is added.
 */
@WithJSpecify
public class NullCheckValueMappingEnum2EnumTest {

    @RegisterExtension
    final GeneratedSource generatedSource = new GeneratedSource();

    @ProcessorTest
    @WithClasses( { OrderType.class, ExternalOrderType.class, NullGuardSkippedNonNullMapper.class } )
    void nullGuardSkippedForNonNullParameter() {
        generatedSource.addComparisonToFixtureFor( NullGuardSkippedNonNullMapper.class );
    }

    @ProcessorTest
    @WithClasses( { OrderType.class, ExternalOrderType.class, NullGuardSkippedNullMarkedMapper.class } )
    void nullGuardSkippedForNullMarkedMethod() {
        generatedSource.addComparisonToFixtureFor( NullGuardSkippedNullMarkedMapper.class );
    }

    @ProcessorTest
    @WithClasses( { OrderType.class, ExternalOrderType.class, NullMappingDeadMapper.class } )
    @ExpectedCompilationOutcome(
        value = CompilationResult.SUCCEEDED,
        diagnostics = {
            @Diagnostic(
                type = NullMappingDeadMapper.class,
                kind = Kind.WARNING,
                line = 22,
                alternativeLine = 28,
                message = "Source parameter can't be null so source = \"NULL\" is ignored."
            )
        }
    )
    void warningForDeadNullMappingWhenParameterIsNonNull() {
    }

    @ProcessorTest
    @WithClasses( { OrderType.class, ExternalOrderType.class, NonNullReturnNullSourceMapper.class } )
    @ExpectedCompilationOutcome(
        value = CompilationResult.FAILED,
            diagnostics = {
                @Diagnostic(
                        type = NonNullReturnNullSourceMapper.class,
                        kind = Kind.ERROR,
                        line = 24,
                        alternativeLine = 29,
                        message = "JSpecify value mapping method has a @NonNull return type but source =\"STANDARD\" " +
                                "mapped to target = \"NULL\"."
                ),
                @Diagnostic(
                        type = NonNullReturnNullSourceMapper.class,
                        kind = Kind.ERROR,
                        line = 36,
                        message = "JSpecify value mapping method has a @NonNull return type but the implementation " +
                                "can return null. Either define a default value for null with source = \"NULL\" or " +
                                "implement a custom EnumMappingStrategy."
                )
            }
    )
    void errorWhenNonNullReturnYieldsNullFromExplicitNullTarget() {
    }

    @ProcessorTest
    @WithClasses( { OrderType.class, ExternalOrderType.class, NonNullReturnAnyRemainingMapper.class } )
    @ExpectedCompilationOutcome(
        value = CompilationResult.FAILED,
        diagnostics = {
            @Diagnostic(
                type = NonNullReturnAnyRemainingMapper.class,
                kind = Kind.ERROR,
                line = 26,
                alternativeLine = 28,
                message = "JSpecify value mapping method has a @NonNull return type but source =\"<ANY_REMAINING>\" " +
                        "mapped to target = \"NULL\"."
            )
        }
    )
    void errorWhenNonNullReturnYieldsNullFromAnyRemaining() {
    }

    @ProcessorTest
    @WithClasses( { OrderType.class, ExternalOrderType.class, NonNullReturnAnyUnmappedMapper.class } )
    @ExpectedCompilationOutcome(
        value = CompilationResult.FAILED,
        diagnostics = {
            @Diagnostic(
                type = NonNullReturnAnyUnmappedMapper.class,
                kind = Kind.ERROR,
                    line = 26,
                    alternativeLine = 28,
                    message = "JSpecify value mapping method has a @NonNull return type but " +
                            "source =\"<ANY_UNMAPPED>\" mapped to target = \"NULL\"."
            )
        }
    )
    void errorWhenNonNullReturnYieldsNullFromAnyUnmapped() {
    }
}
