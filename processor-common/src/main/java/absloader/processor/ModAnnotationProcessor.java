package absloader.processor;

import absloader.library.ModInitializer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public abstract class ModAnnotationProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
		set.forEach(typeElement -> roundEnvironment.getElementsAnnotatedWith(typeElement).forEach(element -> {
			if(element.getKind() == ElementKind.METHOD) {
				this.generateFile(element);
			}
		}));
		return false;
	}

	protected abstract void generateFile(Element modMethod);

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Set.of(ModInitializer.class.getName());
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.RELEASE_17;
	}
}
