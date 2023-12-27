package io.github.phantomloader.processor;

import io.github.phantomloader.library.ModInitializer;

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

public abstract class ModAnnotationProcessor extends AbstractProcessor {

	protected final HashMap<String, HashSet<Element>> annotatedMethods = new HashMap<>();

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
		if(roundEnvironment.processingOver()) {
			this.generateModClass();
			this.generateModFile();
		} else {
			for(TypeElement typeElement : annotations) {
				for(Element element : roundEnvironment.getElementsAnnotatedWith(typeElement)) {
					if(element.getKind() == ElementKind.METHOD) {
						Set<Modifier> modifiers = element.getModifiers();
						if(!modifiers.contains(Modifier.STATIC) || !modifiers.contains(Modifier.PUBLIC)) {
							this.printError("The method annotated with ModInitializer must be public static", element);
						} else {
							ModInitializer annotation = element.getAnnotation(ModInitializer.class);
							if(annotation.modLoader() == ModInitializer.Loader.BOTH || annotation.modLoader() == this.loader()) {
								if(this.annotatedMethods.containsKey(annotation.type())) {
									this.annotatedMethods.get(annotation.type()).add(element);
								} else {
									HashSet<Element> set = new HashSet<>();
									set.add(element);
									this.annotatedMethods.put(annotation.type(), set);
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	protected abstract ModInitializer.Loader loader();

	protected abstract void generateModClass();

	protected abstract void generateModFile();

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Set.of(ModInitializer.class.getName());
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.RELEASE_17;
	}

	public void printError(String message, Element element) {
		this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
	}

	// TODO: Check for required options
}
