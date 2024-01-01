package io.github.phantomloader.processor.fabric;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestFabricAnnotationProcessor {

    @Test
    public void testBasicModClass() {
        testGeneratedClass("test1/TestMod.java", Set.of("test1/FabricInitializer.java"), Set.of("fabricVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId", "modVersion"));
    }

    @Test
    public void testFailOnMissingOptions() {
        FabricAnnotationProcessor annotationProcessor = new FabricAnnotationProcessor();
        JavaFileObject testModClass = JavaFileObjects.forResource("test1/TestMod.java");
        Compilation compilation = Compiler.javac().withProcessors(annotationProcessor).withOptions(getOptions("modName", "modDescription")).compile(testModClass);
        CompilationSubject.assertThat(compilation).failed();
    }

    @Test
    public void testModClassWithClientInit() {
        testGeneratedClass("test2/TestMod.java", Set.of("test2/FabricInitializer.java", "test2/FabricClientInitializer.java"), Set.of("fabricVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId", "modVersion"));
    }

    private static void testGeneratedClass(String inputClass, Set<String> expectedClasses, Set<String> options) {
        FabricAnnotationProcessor annotationProcessor = new FabricAnnotationProcessor();
        JavaFileObject testModClass = JavaFileObjects.forResource(inputClass);
        Compilation compilation = Compiler.javac().withProcessors(annotationProcessor).withOptions(getOptions(options)).compile(testModClass);
        CompilationSubject.assertThat(compilation).succeeded();
        for(String expectedClass : expectedClasses) {
            JavaFileObject expectedModClass = JavaFileObjects.forResource(expectedClass);
            String expectedModClassName = System.getProperty("modGroupId") + "." + System.getProperty("modId").toLowerCase().replace("_", "") + ".fabric." + expectedClass.substring(expectedClass.indexOf('/') + 1, expectedClass.lastIndexOf('.'));
            CompilationSubject.assertThat(compilation).generatedSourceFile(expectedModClassName).hasSourceEquivalentTo(expectedModClass);
        }
    }

    @Test
    public void testBasicModFile() {
        testGeneratedModFile("test1/TestMod.java", "test1/fabric.mod.json", "fabricVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId", "modVersion", "modName", "modLicense", "modAuthors", "modDescription", "modUrl", "modSource");
    }

    @Test
    public void testModFileWithFewerOption() {
        testGeneratedModFile("test2/TestMod.java", "test2/fabric.mod.json", "fabricVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId", "modVersion");
    }

    private static void testGeneratedModFile(String inputClass, String expectedFile, String... options) {
        FabricAnnotationProcessor annotationProcessor = new FabricAnnotationProcessor();
        JavaFileObject testModClass = JavaFileObjects.forResource(inputClass);
        Compilation compilation = Compiler.javac().withProcessors(annotationProcessor).withOptions(getOptions(options)).compile(testModClass);
        CompilationSubject.assertThat(compilation).succeeded();
        CompilationSubject.assertThat(compilation).generatedFile(StandardLocation.CLASS_OUTPUT, "fabric.mod.json").contentsAsUtf8String().isEqualTo(readResource(expectedFile) + "\n");
    }

    private static String readResource(String path) {
        try(InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if(inputStream == null) {
                throw new FileNotFoundException("File " + path + " not found");
            }
            return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static List<String> getOptions(Collection<String> options) {
        return options.stream().map(str -> "-A" + str + "=" + System.getProperty(str)).toList();
    }

    private static List<String> getOptions(String... options) {
        return Arrays.stream(options).map(str -> "-A" + str + "=" + System.getProperty(str)).toList();
    }
}
