package io.github.hexagonnico.phantom.processor;

import io.github.hexagonnico.phantom.library.Mod;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

public abstract class ModAnnotationProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
		set.forEach(typeElement -> roundEnvironment.getElementsAnnotatedWith(typeElement).forEach(element -> {
			if(element.getKind() == ElementKind.METHOD) {
				try {
					this.generateFile(element);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
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
}
