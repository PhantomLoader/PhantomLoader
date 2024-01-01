package io.github.phantomloader.processor;

import io.github.phantomloader.library.ModEntryPoint;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *     Annotation processor used to generate loader-specific code.
 * </p>
 *
 * @author Nico
 */
public abstract class ModAnnotationProcessor extends AbstractProcessor {

    /**
     * <p>
     *     Keeps track of all methods annotated with {@link ModEntryPoint} and groups them by {@link ModEntryPoint.Side}.
     * </p>
     */
    protected final HashMap<ModEntryPoint.Side, HashSet<Element>> annotatedMethods = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if(roundEnvironment.processingOver()) {
            if(this.checkRequiredOptions()) {
                // Annotated methods have been found, generate code
                this.generateModClass();
                this.generateModFile();
            }
        } else {
            // Look for annotated methods
            for(TypeElement typeElement : annotations) {
                for(Element element : roundEnvironment.getElementsAnnotatedWith(typeElement)) {
                    if(element.getKind() == ElementKind.METHOD) {
                        // Add the method to the map if it is supposed to run on this loader
                        ModEntryPoint annotation = element.getAnnotation(ModEntryPoint.class);
                        if(annotation.modLoader() == ModEntryPoint.Loader.COMMON || annotation.modLoader() == this.loader()) {
                            // Ensure the method is public static
                            Set<Modifier> modifiers = element.getModifiers();
                            if(!modifiers.contains(Modifier.STATIC) || !modifiers.contains(Modifier.PUBLIC)) {
                                this.printError("The method annotated with ModEntryPoint must be public static", element);
                            } else {
                                // Group methods by side
                                if(this.annotatedMethods.containsKey(annotation.side())) {
                                    this.annotatedMethods.get(annotation.side()).add(element);
                                } else {
                                    HashSet<Element> set = new HashSet<>();
                                    set.add(element);
                                    this.annotatedMethods.put(annotation.side(), set);
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * <p>
     *     Determines the mod loader that corresponds to this annotation processor.
     * </p>
     *
     * @return Either {@link ModEntryPoint.Loader#FORGE} or {@link ModEntryPoint.Loader#FABRIC}.
     */
    protected abstract ModEntryPoint.Loader loader();

    /**
     * <p>
     *     Generates a class or more classes for the mod loader.
     * </p>
     */
    protected abstract void generateModClass();

    /**
     * <p>
     *     Generates the mod file.
     * </p>
     * <ul>
     *     <li>In Forge, this is the {@code META-INF/mods.toml} file.</li>
     *     <li>In Fabric, this is the {@code fabric.mod.json} file.</li>
     * </ul>
     */
    protected abstract void generateModFile();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ModEntryPoint.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_17;
    }

    /**
     * <p>
     *     Returns a set containing the required compiler options for this annotation processor to work correctly.
     *     An error will be logged and the compilation will fail if any of these is missing.
     * </p>
     *
     * @return A set containing the required compiler options for this annotation processor to work correctly.
     */
    protected abstract Set<String> getRequiredOptions();

    /**
     * <p>
     *     Helper function used to print an error message.
     * </p>
     *
     * @param message The message to print.
     * @param element The corresponding element.
     * @see javax.annotation.processing.Messager#printMessage(Diagnostic.Kind, CharSequence, Element)
     */
    public void printError(String message, Element element) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    /**
     * <p>
     *     Helper function used to print an error message.
     * </p>
     *
     * @param message The message to print.
     * @see javax.annotation.processing.Messager#printMessage(Diagnostic.Kind, CharSequence)
     */
    public void printError(String message) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }

    /**
     * <p>
     *     Helper function used to print an error message.
     * </p>
     *
     * @param message The message to print.
     * @see javax.annotation.processing.Messager#printMessage(Diagnostic.Kind, CharSequence)
     */
    public void printWarning(String message) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, message);
    }

    /**
     * <p>
     *     Checks if all options listed in {@link ModAnnotationProcessor#getRequiredOptions()} are in the processing environment.
     *     Prints an error and returns false if they are not, otherwise true.
     * </p>
     *
     * @return False if an option is missing, true if they are all present.
     */
    private boolean checkRequiredOptions() {
        for(String option : this.getRequiredOptions()) {
            if(!this.processingEnv.getOptions().containsKey(option)) {
                this.printError(this.loader() + " annotation processor is missing the required option " + option + "\nAdd options by adding `compilerArgs.add \"-A" + option + "=${variable}\"` to gradle's compile task.");
                return false;
            }
        }
        return true;
    }
}
