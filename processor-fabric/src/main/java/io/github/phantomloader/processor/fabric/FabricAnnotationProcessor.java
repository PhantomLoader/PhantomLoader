package io.github.phantomloader.processor.fabric;

import io.github.phantomloader.library.ModInitializer;
import io.github.phantomloader.processor.ModAnnotationProcessor;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FabricAnnotationProcessor extends ModAnnotationProcessor {

	@Override
	protected ModInitializer.Loader loader() {
		return ModInitializer.Loader.FABRIC;
	}

	@Override
	protected void generateModClass() {

	}

	@Override
	protected void generateModFile() {

	}

	// TODO: Redo this

	//	@Override
//	protected void generateFile(Element modMethod) throws IOException {
//		ModInitializer modInitializer = modMethod.getAnnotation(ModInitializer.class);
//		String className = modMethod.getEnclosingElement().getSimpleName().toString();
//		String basePackage = this.processingEnv.getElementUtils().getPackageOf(modMethod).getQualifiedName().toString();
//		Filer filer = this.processingEnv.getFiler();
//		JavaFileObject sourceFile = filer.createSourceFile("FabricInitializer");
//		try(PrintWriter writer = new PrintWriter(sourceFile.openWriter())) {
//			writer.println("package " + basePackage + ".fabric;");
//			writer.println();
//			writer.println("import " + basePackage + "." + className + ";");
//			writer.println();
//			writer.println("import net.fabricmc.api.ModInitializer;");
//			writer.println();
//			writer.println("public class FabricInitializer implements ModInitializer {");
//			writer.println();
//			writer.println("	@Override");
//			writer.println("	public void onInitialize() {");
//			writer.println("		" + className + "." + modMethod.getSimpleName() + "();");
//			writer.println("	}");
//			writer.println("}");
//		}
//		FileObject modFile = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "fabric.modInitializer.json");
//		try(PrintWriter writer = new PrintWriter(modFile.openWriter())) {
//			writer.println("{");
//			writer.println("  \"schemaVersion\": 1,");
//			writer.println("  \"id\": \"" + modInitializer.id() + "\",");
//			writer.println("  \"version\": \"" + (modInitializer.version().isEmpty() || modInitializer.version().isBlank() ? "${version}" : modInitializer.version()) + "\",");
//			writer.println("  \"name\": \"" + modInitializer.name() + "\",");
//			writer.println("  \"description\": \"" + modInitializer.description() + "\",");
//			writer.println("  \"authors\": [" + Arrays.stream(modInitializer.authors()).map(str -> "\"" + str + "\"").collect(Collectors.joining(", ")) + "],");
//			writer.println("  \"contact\": {");
//			writer.println("    \"homepage\": \"" + modInitializer.homepage() + "\",");
//			writer.println("    \"sources\": \"" + modInitializer.sources() + "\"");
//			writer.println("  },");
//			writer.println("  \"license\": \"" + modInitializer.license() + "\",");
//			writer.println("  \"icon\": \"" + modInitializer.icon() + "\",");
//			writer.println("  \"entrypoints\": {");
//			writer.println("    \"main\": [");
//			writer.println("      \"" + basePackage + ".fabric.FabricInitializer\"");
//			writer.println("    ]");
//			writer.println("  },");
//			writer.println("  \"depends\": {");
//			writer.println("    \"phantom\": \">=" + this.processingEnv.getOptions().get("phantomVersion") + "\",");
//			writer.println("    \"fabricloader\": \">=" + this.processingEnv.getOptions().get("fabricVersion") + "\",");
//			writer.println("    \"minecraft\": \"~" + this.processingEnv.getOptions().get("minecraftVersion") + "\",");
//			writer.println("    \"java\": \">=17\"");
//			writer.println("  }");
//			writer.println("}");
//		}
//	}

	@Override
	public Set<String> getSupportedOptions() {
		return Set.of("phantomVersion", "fabricVersion", "minecraftVersion");
	}
}
