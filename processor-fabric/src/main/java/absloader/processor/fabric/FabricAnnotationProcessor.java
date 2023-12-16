package absloader.processor.fabric;

import absloader.processor.ModAnnotationProcessor;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;

public class FabricAnnotationProcessor extends ModAnnotationProcessor {

	@Override
	protected void generateFile(Element modMethod) {
		String className = modMethod.getEnclosingElement().getSimpleName().toString();
		String basePackage = this.processingEnv.getElementUtils().getPackageOf(modMethod).getQualifiedName().toString();
		Filer filer = this.processingEnv.getFiler();
		try {
			JavaFileObject sourceFile = filer.createSourceFile("FabricInitializer");
			try(PrintWriter writer = new PrintWriter(sourceFile.openWriter())) {
				writer.println("package " + basePackage + ".fabric;");
				writer.println();
				writer.println("import " + basePackage + "." + className + ";");
				writer.println();
				writer.println("import net.fabricmc.api.ModInitializer;");
				writer.println();
				writer.println("public class FabricInitializer implements ModInitializer {");
				writer.println();
				writer.println("	@Override");
				writer.println("	public void onInitialize() {");
				writer.println("		" + className + "." + modMethod.getSimpleName() + "();");
				writer.println("	}");
				writer.println("}");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Generated Fabric");
	}
}
