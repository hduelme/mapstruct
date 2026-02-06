/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.testutil.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.SourceVersion;

import org.codehaus.plexus.compiler.CompilerConfiguration;
import org.codehaus.plexus.compiler.CompilerException;
import org.codehaus.plexus.compiler.CompilerResult;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.eclipse.tycho.compiler.jdt.JDTCompiler;
import org.mapstruct.ap.MappingProcessor;
import org.mapstruct.ap.testutil.compilation.model.CompilationOutcomeDescriptor;

/**
 * Extension that uses the Eclipse JDT compiler to compile.
 *
 * @author Andreas Gudian
 * @author Filip Hrisafov
 */
class EclipseCompilingExtension extends CompilingExtension {

    private static final List<String> ECLIPSE_COMPILER_CLASSPATH = buildEclipseCompilerClasspath();

    private static final ClassLoader DEFAULT_ECLIPSE_COMPILER_CLASSLOADER =
        new ModifiableURLClassLoader( newFilteringClassLoaderForEclipse() )
            .withPaths( ECLIPSE_COMPILER_CLASSPATH )
            .withPaths( PROCESSOR_CLASSPATH )
            .withOriginOf( ClassLoaderExecutor.class );

    EclipseCompilingExtension() {
        super( Compiler.ECLIPSE );
    }

    @Override
    protected CompilationOutcomeDescriptor compileWithSpecificCompiler(CompilationRequest compilationRequest,
                                                                       String sourceOutputDir,
                                                                       String classOutputDir,
                                                                       String additionalCompilerClasspath) {
        Collection<String> processorClassPaths = getProcessorClasspathDependencies(
            compilationRequest,
            additionalCompilerClasspath
        );
        ClassLoader compilerClassloader;
        if ( processorClassPaths.isEmpty() ) {
            compilerClassloader = DEFAULT_ECLIPSE_COMPILER_CLASSLOADER;
        }
        else {
            ModifiableURLClassLoader loader = new ModifiableURLClassLoader(
                newFilteringClassLoaderForEclipse()
                .hidingClasses( compilationRequest.getServices().values() ) );

            compilerClassloader = loader.withPaths( ECLIPSE_COMPILER_CLASSPATH )
                  .withPaths( PROCESSOR_CLASSPATH )
                  .withOriginOf( ClassLoaderExecutor.class )
                  .withPaths( processorClassPaths )
                  .withOriginsOf( compilationRequest.getServices().values() );
        }

        ClassLoaderHelper clHelper =
            (ClassLoaderHelper) loadAndInstantiate( compilerClassloader, ClassLoaderExecutor.class );

        return clHelper.compileInOtherClassloader(
            compilationRequest,
            getTestCompilationClasspath( compilationRequest, classOutputDir ),
            getSourceFiles( compilationRequest.getSourceClasses() ),
            SOURCE_DIR,
            sourceOutputDir,
            classOutputDir );
    }

    private static List<String> getTestCompilationClasspath(CompilationRequest request, String classOutputDir) {
        Collection<String> testDependencies = request.getTestDependencies();
        Collection<String> processorDependencies = request.getProcessorDependencies();
        Collection<String> kotlinSources = request.getKotlinSources();
        if ( testDependencies.isEmpty() && processorDependencies.isEmpty() && kotlinSources.isEmpty() ) {
            return TEST_COMPILATION_CLASSPATH;
        }

        List<String> testCompilationPaths = new ArrayList<>(
            TEST_COMPILATION_CLASSPATH.size() + testDependencies.size() + processorDependencies.size() + 1 );

        testCompilationPaths.addAll( TEST_COMPILATION_CLASSPATH );
        testCompilationPaths.addAll( filterBootClassPath( testDependencies ) );
        testCompilationPaths.addAll( filterBootClassPath( processorDependencies ) );
        if ( !kotlinSources.isEmpty() ) {
            testCompilationPaths.add( classOutputDir );
        }
        return testCompilationPaths;
    }

    private static FilteringParentClassLoader newFilteringClassLoaderForEclipse() {
        return new FilteringParentClassLoader(
            // reload eclipse compiler classes
            "org.eclipse.",
            "kotlin.",
            // reload mapstruct processor classes
            "org.mapstruct.ap.internal.",
            "org.mapstruct.ap.spi.",
            "org.mapstruct.ap.MappingProcessor")
        .hidingClass( ClassLoaderExecutor.class );
    }

    public interface ClassLoaderHelper {
        CompilationOutcomeDescriptor compileInOtherClassloader(CompilationRequest compilationRequest,
                                                               List<String> testCompilationClasspath,
                                                               Set<File> sourceFiles,
                                                               String sourceDir,
                                                               String sourceOutputDir,
                                                               String classOutputDir);
    }

    public static final class ClassLoaderExecutor implements ClassLoaderHelper {

        private static final Map<String, String> ECLIPSE_SUPPRESS_ALL_WARNINGS = buildEclipseSuppressAllWarnings();

        @Override
        public CompilationOutcomeDescriptor compileInOtherClassloader(CompilationRequest compilationRequest,
                                                                      List<String> testCompilationClasspath,
                                                                      Set<File> sourceFiles,
                                                                      String sourceDir,
                                                                      String sourceOutputDir,
                                                                      String classOutputDir) {
            JDTCompiler compiler = new JDTCompiler();
            compiler.enableLogging( new ConsoleLogger( 5, "JDT-Compiler" ) );

            CompilerConfiguration config = new CompilerConfiguration();

            config.setClasspathEntries( testCompilationClasspath );
            config.setOutputLocation( classOutputDir );
            config.setGeneratedSourcesDirectory( new File( sourceOutputDir ) );
            config.setAnnotationProcessors( new String[] { MappingProcessor.class.getName() } );
            config.setSourceFiles( sourceFiles );
            String version = getSourceVersion();
            config.setShowWarnings( true );
            config.setCustomCompilerArgumentsAsMap( ECLIPSE_SUPPRESS_ALL_WARNINGS );
            config.setSourceVersion( version );
            config.setTargetVersion( version );

            for ( String option : compilationRequest.getProcessorOptions() ) {
                config.addCompilerCustomArgument( option, null );
            }

            CompilerResult compilerResult;
            try {
                compilerResult = compiler.performCompile( config );
            }
            catch ( CompilerException e ) {
                throw new RuntimeException( e );
            }

            return CompilationOutcomeDescriptor.forResult(
                sourceDir,
                compilerResult );
        }

        private static String getSourceVersion() {
            SourceVersion latest = SourceVersion.latest();
            if ( latest == SourceVersion.RELEASE_8 ) {
                return "1.8";
            }
            return "11";
        }

        private static Map<String, String> buildEclipseSuppressAllWarnings() {
            Map<String, String> suppressWarnings = new HashMap<>();
            suppressWarnings.put( "-warn:-all-static-method", null );
            suppressWarnings.put( "-warn:-allDeprecation", null );
            suppressWarnings.put( "-warn:-allJavadoc", null );
            suppressWarnings.put( "-warn:-assertIdentifier", null );
            suppressWarnings.put( "-warn:-boxing", null );
            suppressWarnings.put( "-warn:-charConcat", null );
            suppressWarnings.put( "-warn:-compareIdentical", null );
            suppressWarnings.put( "-warn:-conditionAssign", null );
            suppressWarnings.put( "-warn:-constructorName", null );
            suppressWarnings.put( "-warn:-deadCode", null );
            suppressWarnings.put( "-warn:-dep-ann", null );
            suppressWarnings.put( "-warn:-discouraged", null );
            suppressWarnings.put( "-warn:-emptyBlock", null );
            suppressWarnings.put( "-warn:-enumIdentifier", null );
            suppressWarnings.put( "-warn:-enumSwitch", null );
            suppressWarnings.put( "-warn:-enumSwitchPedantic", null );
            suppressWarnings.put( "-warn:-exports", null );
            suppressWarnings.put( "-warn:-fallthrough", null );
            suppressWarnings.put( "-warn:-fieldHiding", null );
            suppressWarnings.put( "-warn:-finalBound", null );
            suppressWarnings.put( "-warn:-finally", null );
            suppressWarnings.put( "-warn:-forbidden", null );
            suppressWarnings.put( "-warn:-hashCode", null );
            suppressWarnings.put( "-warn:-hiding", null );
            suppressWarnings.put( "-warn:-includeAssertNull", null );
            suppressWarnings.put( "-warn:-indirectStatic", null );
            suppressWarnings.put( "-warn:-inheritNullAnnot", null );
            suppressWarnings.put( "-warn:-interfaceNonInherited", null );
            suppressWarnings.put( "-warn:-intfAnnotation", null );
            suppressWarnings.put( "-warn:-intfNonInherited", null );
            suppressWarnings.put( "-warn:-invalidJavadoc", null );
            suppressWarnings.put( "-warn:-javadoc", null );
            suppressWarnings.put( "-warn:-localHiding", null );
            suppressWarnings.put( "-warn:-maskedCatchBlock", null );
            suppressWarnings.put( "-warn:-maskedCatchBlocks", null );
            suppressWarnings.put( "-warn:-missingJavadocComments", null );
            suppressWarnings.put( "-warn:-missingJavadocTags", null );
            suppressWarnings.put( "-warn:-module", null );
            suppressWarnings.put( "-warn:-nls", null );
            suppressWarnings.put( "-warn:-noEffectAssign", null );
            suppressWarnings.put( "-warn:-noImplicitStringConversion", null );
            suppressWarnings.put( "-warn:-nonnullNotRepeated", null );
            suppressWarnings.put( "-warn:-null", null );
            suppressWarnings.put( "-warn:-nullAnnotConflict", null );
            suppressWarnings.put( "-warn:-nullAnnotRedundant", null );
            suppressWarnings.put( "-warn:-nullDereference", null );
            suppressWarnings.put( "-warn:-nullUncheckedConversion", null );
            suppressWarnings.put( "-warn:-over-ann", null );
            suppressWarnings.put( "-warn:-over-sync", null );
            suppressWarnings.put( "-warn:-packageDefaultMethod", null );
            suppressWarnings.put( "-warn:-paramAssign", null );
            suppressWarnings.put( "-warn:-pkgDefaultMethod", null );
            suppressWarnings.put( "-warn:-raw", null );
            suppressWarnings.put( "-warn:-redundantSuperinterface", null );
            suppressWarnings.put( "-warn:-resource", null );
            suppressWarnings.put( "-warn:-semicolon", null );
            suppressWarnings.put( "-warn:-serial", null );
            suppressWarnings.put( "-warn:-specialParamHiding", null );
            suppressWarnings.put( "-warn:-static-access", null );
            suppressWarnings.put( "-warn:-staticReceiver", null );
            suppressWarnings.put( "-warn:-super", null );
            suppressWarnings.put( "-warn:-suppress", null );
            suppressWarnings.put( "-warn:-switchDefault", null );
            suppressWarnings.put( "-warn:-syncOverride", null );
            suppressWarnings.put( "-warn:-syntacticAnalysis", null );
            suppressWarnings.put( "-warn:-synthetic-access", null );
            suppressWarnings.put( "-warn:-syntheticAccess", null );
            suppressWarnings.put( "-warn:-typeHiding", null );
            suppressWarnings.put( "-warn:-unavoidableGenericProblems", null );
            suppressWarnings.put( "-warn:-unchecked", null );
            suppressWarnings.put( "-warn:-unlikelyCollectionMethodArgumentType", null );
            suppressWarnings.put( "-warn:-unlikelyEqualsArgumentType", null );
            suppressWarnings.put( "-warn:-unnecessaryElse", null );
            suppressWarnings.put( "-warn:-unqualified-field-access", null );
            suppressWarnings.put( "-warn:-unqualifiedField", null );
            suppressWarnings.put( "-warn:-unsafe", null );
            suppressWarnings.put( "-warn:-unused", null );
            suppressWarnings.put( "-warn:-unusedParamImplementing", null );
            suppressWarnings.put( "-warn:-unusedParamIncludeDoc", null );
            suppressWarnings.put( "-warn:-unusedParamOverriding", null );
            suppressWarnings.put( "-warn:-unusedThrownExemptExceptionThrowable", null );
            suppressWarnings.put( "-warn:-unusedThrownIncludeDocComment", null );
            suppressWarnings.put( "-warn:-unusedThrownWhenOverriding", null );
            suppressWarnings.put( "-warn:-unusedTypeParameter", null );
            suppressWarnings.put( "-warn:-uselessTypeCheck", null );
            suppressWarnings.put( "-warn:-varargsCast", null );
            suppressWarnings.put( "-warn:-warningToken", null );
            return Collections.unmodifiableMap( suppressWarnings );
        }

    }

    private static List<String> buildEclipseCompilerClasspath() {
        Collection<String> whitelist = Arrays.asList(
                "tycho-compiler",
                "ecj",
                "plexus-compiler-api",
                "plexus-component-annotations"
        );

        return filterBootClassPath( whitelist );
    }
}
