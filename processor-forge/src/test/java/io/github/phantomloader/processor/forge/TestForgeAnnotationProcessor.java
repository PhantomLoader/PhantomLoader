package io.github.phantomloader.processor.forge;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestForgeAnnotationProcessor {

    @Test
    public void testBasicModClass() {
        testGeneratedClass("test1/TestMod.java", "test1/ForgeInitializer.java", "forgeVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId", "modVersion");
    }

    @Test
    public void testFailOnMissingOptions() {
        ForgeAnnotationProcessor annotationProcessor = new ForgeAnnotationProcessor();
        JavaFileObject testModClass = JavaFileObjects.forResource("test1/TestMod.java");
        Compilation compilation = Compiler.javac().withProcessors(annotationProcessor).withOptions(getOptions("modName", "modDescription")).compile(testModClass);
        CompilationSubject.assertThat(compilation).failed();
    }

    @Test
    public void testModClassWithClientInit() {
        testGeneratedClass("test2/TestMod.java", "test2/ForgeInitializer.java", "forgeVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId", "modVersion");
    }

    private static void testGeneratedClass(String inputClass, String expectedClass, String... options) {
        ForgeAnnotationProcessor annotationProcessor = new ForgeAnnotationProcessor();
        JavaFileObject testModClass = JavaFileObjects.forResource(inputClass);
        Compilation compilation = Compiler.javac().withProcessors(annotationProcessor).withOptions(getOptions(options)).compile(testModClass);
        JavaFileObject expectedModClass = JavaFileObjects.forResource(expectedClass);
        String expectedModClassName = System.getProperty("modGroupId") + "." + System.getProperty("modId").toLowerCase().replace("_", "") + ".forge.ForgeInitializer";
        CompilationSubject.assertThat(compilation).succeeded();
        CompilationSubject.assertThat(compilation).generatedSourceFile(expectedModClassName).hasSourceEquivalentTo(expectedModClass);
    }

    @Test
    public void testBasicModToml() {
        testGeneratedToml("test1/TestMod.java", "test1/mods.toml", "forgeVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId", "modVersion", "modName", "modLicense", "modAuthors", "modDescription", "modUrl", "modSource");
    }

    @Test
    public void testModsTomlWithFewerOptions() {
        testGeneratedToml("test2/TestMod.java", "test2/mods.toml", "forgeVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId");
    }

    private static void testGeneratedToml(String inputClass, String expectedFile, String... options) {
        ForgeAnnotationProcessor annotationProcessor = new ForgeAnnotationProcessor();
        JavaFileObject testModClass = JavaFileObjects.forResource(inputClass);
        Compilation compilation = Compiler.javac().withProcessors(annotationProcessor).withOptions(getOptions(options)).compile(testModClass);
        CompilationSubject.assertThat(compilation).succeeded();
        CompilationSubject.assertThat(compilation).generatedFile(StandardLocation.CLASS_OUTPUT, "META-INF/mods.toml").contentsAsUtf8String().isEqualTo(readResource(expectedFile) + "\n");
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

    private static List<String> getOptions(String... options) {
        return Arrays.stream(options).map(str -> "-A" + str + "=" + System.getProperty(str)).toList();
    }
}
