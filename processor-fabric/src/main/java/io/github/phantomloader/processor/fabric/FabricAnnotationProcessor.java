package io.github.phantomloader.processor.fabric;

import io.github.phantomloader.library.Mod;
import io.github.phantomloader.processor.ModAnnotationProcessor;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FabricAnnotationProcessor extends ModAnnotationProcessor {

	@Override
	protected void generateFile(Element modMethod) throws IOException {
		Mod mod = modMethod.getAnnotation(Mod.class);
		if(mod != null) {
			String className = modMethod.getEnclosingElement().getSimpleName().toString();
			String basePackage = this.processingEnv.getElementUtils().getPackageOf(modMethod).getQualifiedName().toString();
			Filer filer = this.processingEnv.getFiler();
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
			FileObject modFile = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "fabric.mod.json");
			try(PrintWriter writer = new PrintWriter(modFile.openWriter())) {
				writer.println("{");
				writer.println("  \"schemaVersion\": 1,");
				writer.println("  \"id\": \"" + mod.id() + "\",");
				writer.println("  \"version\": \"" + (mod.version().isEmpty() || mod.version().isBlank() ? "${version}" : mod.version()) + "\",");
				writer.println("  \"name\": \"" + mod.name() + "\",");
				writer.println("  \"description\": \"" + mod.description() + "\",");
				writer.println("  \"authors\": [" + Arrays.stream(mod.authors()).map(str -> "\"" + str + "\"").collect(Collectors.joining(", ")) + "],");
				writer.println("  \"contact\": {");
				writer.println("    \"homepage\": \"" + mod.homepage() + "\",");
				writer.println("    \"sources\": \"" + mod.sources() + "\"");
				writer.println("  },");
				writer.println("  \"license\": \"" + mod.license() + "\",");
				writer.println("  \"icon\": \"" + mod.icon() + "\",");
				writer.println("  \"entrypoints\": {");
				writer.println("    \"main\": [");
				writer.println("      \"" + basePackage + ".fabric.FabricInitializer\"");
				writer.println("    ]");
				writer.println("  },");
				writer.println("  \"depends\": {");
				writer.println("    \"phantom\": \">=0.1\",");
				writer.println("    \"fabricloader\": \">=0.14.23\","); // TODO: Get versions?
				writer.println("    \"minecraft\": \"~1.20.1\",");
				writer.println("    \"java\": \">=17\"");
				writer.println("  }");
				writer.println("}");
			}
		}
	}
}
