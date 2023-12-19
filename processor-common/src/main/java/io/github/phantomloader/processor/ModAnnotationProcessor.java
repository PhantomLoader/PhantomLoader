package io.github.phantomloader.processor;

import io.github.phantomloader.library.Mod;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Set;

public abstract class ModAnnotationProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
		for(TypeElement typeElement : set) {
			for(Element element : roundEnvironment.getElementsAnnotatedWith(typeElement)) {
				if(element.getKind() == ElementKind.METHOD) {
					if(this.validateOptions()) {
						Set<Modifier> modifiers = element.getModifiers();
						if(!modifiers.contains(Modifier.STATIC) || !modifiers.contains(Modifier.PUBLIC)) {
							this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Mod method must be public static", element);
						} else try {
							this.generateFile(element);
						} catch (IOException e) {
							throw new UncheckedIOException("Could not generate loader-specific code.", e);
						}
					}
				}
			}
		}
		return false;
	}

	protected abstract void generateFile(Element modMethod) throws IOException;

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Set.of(Mod.class.getName());
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.RELEASE_17;
	}

	private boolean validateOptions() {
		for(String option : this.getSupportedOptions()) {
			String value = this.processingEnv.getOptions().get(option);
			if(value == null) {
				this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Cannot get option " + option + ". Add compiler options using `options.compilerArgs.add \"-A" + option + "=${variable}` in gradle's compileJava task.");
				return false;
			} else if(value.isEmpty() || value.isBlank()) {
				this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Option " + option + " returned an empty or blank string");
				return false;
			}
		}
		return true;
	}
}
