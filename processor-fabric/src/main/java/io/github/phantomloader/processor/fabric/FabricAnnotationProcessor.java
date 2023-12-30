package io.github.phantomloader.processor.fabric;

import io.github.phantomloader.library.ModEntryPoint;
import io.github.phantomloader.library.integration.FabricCustomEntryPoint;
import io.github.phantomloader.processor.ModAnnotationProcessor;

import javax.lang.model.element.Element;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *     Implementation of {@link ModAnnotationProcessor} for Fabric.
 * </p>
 *
 * @author Nico
 */
public class FabricAnnotationProcessor extends ModAnnotationProcessor {

	/**
	 * <p>
	 *     Stores the generated classes so that they can be added as entry points in the {@code fabric.mod.json} file.
	 * </p>
	 */
	private final HashMap<String, String> fabricInitializers = new HashMap<>();

	@Override
	protected ModEntryPoint.Loader loader() {
		return ModEntryPoint.Loader.FABRIC;
	}

	@Override
	protected void generateModClass() {
		this.generateFabricInitializer(ModEntryPoint.Side.COMMON, "FabricInitializer", "net.fabricmc.api.ModInitializer");
		this.generateFabricInitializer(ModEntryPoint.Side.CLIENT, "FabricClientInitializer", "net.fabricmc.api.ClientModInitializer");
		this.generateFabricInitializer(ModEntryPoint.Side.SERVER, "FabricServerInitializer", "net.fabricmc.api.DedicatedServerModInitializer");
		this.generateCustomInitializers();
	}

	/**
	 * <p>
	 *     Generates a mod entry point for Fabric if any method for the given {@link ModEntryPoint.Side} exists.
	 * </p>
	 *
	 * @param side The side of this entry point.
	 * @param className The simple name of the class to generate.
	 * @param interfaceName The fully qualified name of the interface to implement.
	 */
	private void generateFabricInitializer(ModEntryPoint.Side side, String className, String interfaceName) {
		if(this.annotatedMethods.containsKey(side)) {
			String packageName = this.processingEnv.getOptions().get("modGroupId") + "." + this.lowercaseModId() + ".fabric";
			try(PrintWriter writer = new PrintWriter(this.processingEnv.getFiler().createSourceFile(packageName + "." + className).openWriter())) {
				writer.println("package " + packageName + ";");
				for(Element method : this.annotatedMethods.get(side)) {
					if(method.getAnnotation(FabricCustomEntryPoint.class) == null) {
						String methodClass = method.getEnclosingElement().getSimpleName().toString();
						String methodPackage = this.processingEnv.getElementUtils().getPackageOf(method).getQualifiedName().toString();
						writer.println("import static " + methodPackage + "." + methodClass + "." + method.getSimpleName() + ";");
					}
				}
				writer.println("public class " + className + " implements " + interfaceName + " {");
				Method[] interfaceMethods = Class.forName(interfaceName).getMethods();
				if(interfaceMethods.length > 0) {
					writer.println("	@Override");
					writer.println("	public void " + interfaceMethods[0].getName() + "() {");
					for(Element method : this.annotatedMethods.get(side)) {
						if(method.getAnnotation(FabricCustomEntryPoint.class) == null) {
							writer.println("		" + method.getSimpleName() + "();");
						}
					}
					writer.println("	}");
				}
				writer.println("}");
			} catch (IOException e) {
				throw new UncheckedIOException("Could not generate class " + packageName + "." + className, e);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Could not find interface " + interfaceName, e);
			}
			this.fabricInitializers.put(sideName(side), packageName + "." + className);
		}
	}

	/**
	 * <p>
	 *     Generates custom initializers for methods annotated with {@link FabricCustomEntryPoint}.
	 * </p>
	 */
	private void generateCustomInitializers() {
		HashMap<NameInterfacePair, HashSet<Element>> customInitializers = new HashMap<>();
		for(ModEntryPoint.Side side : this.annotatedMethods.keySet()) {
			for(Element method : this.annotatedMethods.get(side)) {
				FabricCustomEntryPoint customEntryPoint = method.getAnnotation(FabricCustomEntryPoint.class);
				if(customEntryPoint != null) {
					NameInterfacePair pair = new NameInterfacePair(customEntryPoint.name(), customEntryPoint.interfaceName());
					if(customInitializers.containsKey(pair)) {
						customInitializers.get(pair).add(method);
					} else {
						HashSet<Element> set = new HashSet<>();
						set.add(method);
						customInitializers.put(pair, set);
					}
				}
			}
		}
		for(NameInterfacePair pair : customInitializers.keySet()) {
			this.generateCustomInitializer(pair.name(), pair.interfaceName(), customInitializers.get(pair));
		}
	}

	/**
	 * <p>
	 *     Generate a custom mod entry point.
	 * </p>
	 *
	 * @param name Name of the entry point.
	 * @param interfaceName Name of the interface to implement.
	 * @param methods Set of methods to call from this initializer.
	 */
	private void generateCustomInitializer(String name, String interfaceName, HashSet<Element> methods) {
		String packageName = this.processingEnv.getOptions().get("modGroupId") + ".fabric.integration";
		String className = name.substring(0, 1).toUpperCase() + name.substring(1) + "Initializer";
		try(PrintWriter writer = new PrintWriter(this.processingEnv.getFiler().createSourceFile(packageName + "." + className).openWriter())) {
			writer.println("package " + packageName + ";");
			for(Element method : methods) {
				String methodClass = method.getEnclosingElement().getSimpleName().toString();
				String methodPackage = this.processingEnv.getElementUtils().getPackageOf(method).getQualifiedName().toString();
				writer.println("import static " + methodPackage + "." + methodClass + "." + method.getSimpleName() + ";");
			}
			writer.println("public class " + className + " implements " + interfaceName + " {");
			Method[] interfaceMethods = Class.forName(interfaceName).getMethods();
			if(interfaceMethods.length > 0) {
				writer.println("	@Override");
				writer.println("	public void " + interfaceMethods[0].getName() + "() {");
				for(Element method : methods) {
					writer.println("		" + method.getSimpleName() + "();");
				}
				writer.println("	}");
			}
			writer.println("}");
		} catch (IOException e) {
			throw new UncheckedIOException("Could not generate class " + packageName + "." + className, e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Could not find interface " + interfaceName, e);
		}
		this.fabricInitializers.put(name, packageName + "." + className);
	}

	/**
	 * <p>
	 *     Converts the {@link ModEntryPoint.Side} enum into the string used by Fabric.
	 * </p>
	 *
	 * @param side Mod entry point side.
	 * @return The string value used by Fabric.
	 */
	private static String sideName(ModEntryPoint.Side side) {
		return switch (side) {
			case COMMON -> "main";
			case CLIENT -> "client";
			case SERVER -> "server";
		};
	}

	@Override
	protected void generateModFile() {
		try(PrintWriter writer = new PrintWriter(this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "fabric.mod.json").openWriter())) {
			writer.println("{");
			writer.println("  \"schemaVersion\": 1,");
			writer.println("  \"id\": \"" + this.processingEnv.getOptions().get("modId") + "\",");
			writer.println("  \"version\": \"" + this.processingEnv.getOptions().get("modVersion") + "\",");
			writer.println("  \"name\": \"" + this.getOption("modName", "Unnamed") + "\",");
			writer.println("  \"description\": \"" + this.getOption("modDescription") + "\",");
			writer.println("  \"authors\": [");
			writer.println("    " + Arrays.stream(this.getOption("modAuthors").split(",")).map(str -> "\"" + str.trim() + "\"").collect(Collectors.joining(",\n    ")));
			writer.println("  ],");
			writer.println("  \"contact\": {");
			writer.println("    \"homepage\": \"" + this.getOption("modUrl") + "\",");
			writer.println("    \"sources\": \"" + this.getOption("modSource") + "\"");
			writer.println("  },");
			writer.println("  \"license\": \"" + this.getOption("modLicense", "All rights reserved") + "\",");
			writer.println("  \"icon\": \"" + this.getOption("modIcon", "icon.png") + "\",");
			writer.println("  \"entrypoints\": {");
			writer.println(this.fabricInitializers.entrySet().stream().map(entry -> "    \"" + entry.getKey() + "\": [\n      \"" + entry.getValue() + "\"\n    ]").collect(Collectors.joining(",\n")));
			writer.println("  },");
			if(this.processingEnv.getOptions().containsKey("modMixin") || this.processingEnv.getOptions().containsKey("fabricMixin")) {
				writer.println("  \"mixins\": [");
				if(this.processingEnv.getOptions().containsKey("modMixin")) {
					writer.print("    \"" + this.processingEnv.getOptions().get("modMixin") + "\"");
				}
				if(this.processingEnv.getOptions().containsKey("fabricMixin")) {
					writer.println(",");
					writer.println("    \"" + this.processingEnv.getOptions().get("fabricMixin") + "\"");
				} else {
					writer.println();
				}
				writer.println("  ],");
			}
			writer.println("  \"depends\": {");
			writer.println("    \"phantom\": \"~" + this.processingEnv.getOptions().get("phantomVersion") + "\",");
			writer.println("    \"fabricloader\": \">=" + this.processingEnv.getOptions().get("fabricVersion") + "\",");
			writer.println("    \"minecraft\": \"~" + this.processingEnv.getOptions().get("minecraftVersion") + "\",");
			writer.println("    \"java\": \">=17\"");
			writer.println("  }");
			writer.println("}");
		} catch (IOException e) {
			throw new UncheckedIOException("Could not generate fabric.mod.json file", e);
		}
	}

	/**
	 * <p>
	 *     Returns the value of the requested compiler option or the default value.
	 * </p>
	 *
	 * @param option The option to get.
	 * @param defaultValue The default value to use.
	 * @return The value of the requested option or its default value.
	 */
	private String getOption(String option, String defaultValue) {
		String value = this.processingEnv.getOptions().get(option);
		if(value == null || value.isEmpty() || value.isBlank()) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * <p>
	 *     Returns the value of the requested compiler option or an empty string.
	 * </p>
	 *
	 * @param option The option to get.
	 * @return The value of the requested option or an empty string.
	 */
	private String getOption(String option) {
		return this.getOption(option, "");
	}

	@Override
	public Set<String> getSupportedOptions() {
		return Set.of("fabricVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId", "modVersion", "modName", "modLicense", "modAuthors", "modDescription", "modUrl", "modSource", "modIcon");
	}

	@Override
	protected Set<String> getRequiredOptions() {
		return Set.of("fabricVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId", "modVersion");
	}

	private String lowercaseModId() {
		return this.processingEnv.getOptions().get("modId").toLowerCase().replace("_", "");
	}

	/**
	 * <p>
	 *     Record used to pair the name of a custom entry point with its interface.
	 * </p>
	 *
	 * @param name Entry point name.
	 * @param interfaceName The interface's fully qualified name.
	 */
	private record NameInterfacePair(String name, String interfaceName) {

	}
}
