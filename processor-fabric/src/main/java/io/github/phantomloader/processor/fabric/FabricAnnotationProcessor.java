package io.github.phantomloader.processor.fabric;

import io.github.phantomloader.library.ModInitializer;
import io.github.phantomloader.processor.ModAnnotationProcessor;

import javax.lang.model.element.Element;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class FabricAnnotationProcessor extends ModAnnotationProcessor {

	private final HashMap<String, String> fabricInitializers = new HashMap<>();

	@Override
	protected ModInitializer.Loader loader() {
		return ModInitializer.Loader.FABRIC;
	}

	@Override
	protected void generateModClass() {
		this.generateModInitializer();
		this.generateClientInitializer();
	}

	private void generateModInitializer() {
		if(this.annotatedMethods.containsKey("main")) {
			String packageName = this.processingEnv.getOptions().get("modGroupId") + ".fabric";
			try(PrintWriter writer = new PrintWriter(this.processingEnv.getFiler().createSourceFile(packageName + ".FabricInitializer").openWriter())) {
				writer.println("package " + packageName + ";");
				writer.println("import net.fabricmc.api.ModInitializer;");
				for(Element method : this.annotatedMethods.get("main")) {
					String className = method.getEnclosingElement().getSimpleName().toString();
					String basePackage = this.processingEnv.getElementUtils().getPackageOf(method).getQualifiedName().toString();
					writer.println("import static " + basePackage + "." + className + "." + method.getSimpleName() + ";");
				}
				writer.println("public class FabricInitializer implements ModInitializer {");
				writer.println("	@Override");
				writer.println("	public void onInitialize() {");
				for(Element method : this.annotatedMethods.get("main")) {
					writer.println("		" + method.getSimpleName() + "();");
				}
				writer.println("	}");
				writer.println("}");
			} catch (IOException e) {
				throw new UncheckedIOException("Could not generate fabric mod initializer", e);
			}
			this.fabricInitializers.put("main", packageName + ".FabricInitializer");
		}
	}

	private void generateClientInitializer() {
		if(this.annotatedMethods.containsKey("client")) {
			String packageName = this.processingEnv.getOptions().get("modGroupId") + ".fabric";
			try(PrintWriter writer = new PrintWriter(this.processingEnv.getFiler().createSourceFile(packageName + ".FabricClientInitializer").openWriter())) {
				writer.println("package " + packageName + ";");
				writer.println("import net.fabricmc.api.ClientModInitializer;");
				for(Element method : this.annotatedMethods.get("client")) {
					String className = method.getEnclosingElement().getSimpleName().toString();
					String basePackage = this.processingEnv.getElementUtils().getPackageOf(method).getQualifiedName().toString();
					writer.println("import static " + basePackage + "." + className + "." + method.getSimpleName() + ";");
				}
				writer.println("public class FabricClientInitializer implements ClientModInitializer {");
				writer.println("	@Override");
				writer.println("	public void onInitializeClient() {");
				for(Element method : this.annotatedMethods.get("client")) {
					writer.println("		" + method.getSimpleName() + "();");
				}
				writer.println("	}");
				writer.println("}");
			} catch (IOException e) {
				throw new UncheckedIOException("Could not generate fabric client initializer", e);
			}
			this.fabricInitializers.put("client", packageName + ".FabricClientInitializer");
		}
	}

	@Override
	protected void generateModFile() {
		try(PrintWriter writer = new PrintWriter(this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "fabric.mod.json").openWriter())) {
			writer.println("{");
			writer.println("  \"schemaVersion\": 1,");
			writer.println("  \"id\": \"" + this.processingEnv.getOptions().get("modId") + "\",");
			writer.println("  \"version\": \"" + this.getOption("modVersion", "${version}") + "\",");
			writer.println("  \"name\": \"" + this.getOption("modName", "Unnamed") + "\",");
			writer.println("  \"description\": \"" + this.getOption("modDescription") + "\",");
			writer.println("  \"authors\": [" + Arrays.stream(this.getOption("modAuthors").split(",")).map(str -> "\"" + str + "\"").collect(Collectors.joining(", ")) + "],");
			writer.println("  \"contact\": {");
			writer.println("    \"homepage\": \"" + this.getOption("modUrl") + "\",");
			writer.println("    \"sources\": \"" + this.getOption("modSource") + "\"");
			writer.println("  },");
			writer.println("  \"license\": \"" + this.getOption("modLicense", "All rights reserved") + "\",");
			writer.println("  \"icon\": \"" + this.getOption("modIcon", "icon.png") + "\",");
			writer.println("  \"entrypoints\": {");
			writer.println(this.fabricInitializers.entrySet().stream().map(entry -> "    \"" + entry.getKey() + "\": [\n      \"" + entry.getValue() + "\"\n    ]").collect(Collectors.joining(",\n")));
			writer.println("  },");
			writer.println("  \"depends\": {");
			writer.println("    \"phantom\": \">=" + this.processingEnv.getOptions().get("phantomVersion") + "\",");
			writer.println("    \"fabricloader\": \">=" + this.processingEnv.getOptions().get("fabricVersion") + "\",");
			writer.println("    \"minecraft\": \"~" + this.processingEnv.getOptions().get("minecraftVersion") + "\",");
			writer.println("    \"java\": \">=17\"");
			writer.println("  }");
			writer.println("}");
		} catch (IOException e) {
			throw new UncheckedIOException("Could not generate fabric.mod.json file", e);
		}
	}

	private String getOption(String option, String defaultValue) {
		String value = this.processingEnv.getOptions().get(option);
		if(value == null || value.isEmpty() || value.isBlank()) {
			return defaultValue;
		}
		return value;
	}

	private String getOption(String option) {
		return this.getOption(option, "");
	}

	@Override
	public Set<String> getSupportedOptions() {
		return Set.of("fabricVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId", "modVersion", "modName", "modLicense", "modAuthors", "modDescription", "modUrl", "modSource", "modIcon");
	}
}
