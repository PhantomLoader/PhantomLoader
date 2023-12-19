package io.github.phantomloader.processor.forge;

import io.github.phantomloader.library.Mod;
import io.github.phantomloader.processor.ModAnnotationProcessor;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

public class ForgeAnnotationProcessor extends ModAnnotationProcessor {

	@Override
	protected void generateFile(Element modMethod) throws IOException {
		Mod mod = modMethod.getAnnotation(Mod.class);
		String className = modMethod.getEnclosingElement().getSimpleName().toString();
		String basePackage = this.processingEnv.getElementUtils().getPackageOf(modMethod).getQualifiedName().toString();
		Filer filer = this.processingEnv.getFiler();
		JavaFileObject modClass = filer.createSourceFile("ForgeInitializer");
		try(PrintWriter writer = new PrintWriter(modClass.openWriter())) {
			writer.println("package " + basePackage + ".forge;");
			writer.println();
			writer.println("import " + basePackage + "." + className + ";");
			writer.println();
			writer.println("import net.minecraftforge.common.MinecraftForge;");
			writer.println("import net.minecraftforge.fml.common.Mod;");
			writer.println();
			writer.println("@Mod(\"" + mod.id() + "\")");
			writer.println("public class ForgeInitializer {");
			writer.println();
			writer.println("	public ForgeInitializer() {");
			if(!modMethod.getModifiers().contains(Modifier.STATIC)) {
				this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "The annotated method should be static", modMethod);
				writer.println("		new " + className + "()." + modMethod.getSimpleName() + "();");
			} else {
				writer.println("		" + className + "." + modMethod.getSimpleName() + "();");
			}
			writer.println("		MinecraftForge.EVENT_BUS.register(this);");
			writer.println("	}");
			writer.println("}");
		}
		FileObject modFile = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/mods.toml");
		try(PrintWriter writer = new PrintWriter(modFile.openWriter())) {
			String forgeVersion = this.forgeVersion();
			writer.println("modLoader=\"javafml\"");
			writer.println("loaderVersion=\"" + forgeVersion + "\"");
			writer.println(tomlLine("license", mod.license()));
			writer.println(tomlLine("issueTrackerURL", mod.issueTracker()));
			writer.println();
			writer.println("[[mods]]");
			writer.println(tomlLine("    modId", mod.id()));
			writer.println(tomlLine("    version", mod.version(), "${file.jarVersion}", false));
			writer.println(tomlLine("    displayName", mod.name()));
			writer.println(tomlLine("    displayURL", mod.homepage(), mod.sources(), false));
			writer.println(tomlLine("    logoFile", mod.logo()));
			writer.println(tomlLine("    credits", mod.credits()));
			writer.println(tomlLine("    authors", mod.authors()));
			writer.println(tomlLine("    description", mod.description()));
			writer.println();
			writer.println("[[dependencies." + mod.id() + "]]");
			writer.println("    modId=\"forge\"");
			writer.println("    mandatory=true");
			writer.println("    versionRange=\"" + forgeVersion + "\"");
			writer.println("    ordering=\"NONE\"");
			writer.println("    side=\"BOTH\"");
			writer.println("[[dependencies." + mod.id() + "]]");
			writer.println("    modId=\"phantom\"");
			writer.println("    mandatory=true");
			writer.println("    versionRange=\"" + this.phantomVersion() + "\"");
			writer.println("    ordering=\"NONE\"");
			writer.println("    side=\"BOTH\"");
			writer.println("[[dependencies." + mod.id() + "]]");
			writer.println("    modId=\"minecraft\"");
			writer.println("    mandatory=true");
			writer.println("    versionRange=\"" + this.minecraftVersion() + "\"");
			writer.println("    ordering=\"NONE\"");
			writer.println("    side=\"BOTH\"");
		}
	}

	private static String tomlLine(String option, String value, String defaultValue, boolean comment) {
		if(value.isEmpty() || value.isBlank()) {
			if(comment || defaultValue.isEmpty()) {
				return "#" + option + "=\"" + defaultValue + "\"";
			}
			return option + "=\"" + defaultValue + "\"";
		}
		return option + "=\"" + value + "\"";
	}

	private static String tomlLine(String option, String value) {
		return tomlLine(option, value, "", true);
	}

	private static String tomlLine(String option, String[] values) {
		if(values.length == 0) {
			return "#" + option + "=";
		}
		return option + "=\"" + String.join(", ", values) + "\"";
	}

	@Override
	public Set<String> getSupportedOptions() {
		return Set.of("forgeVersion", "phantomVersion", "minecraftVersion");
	}

	private String forgeVersion() {
		String forgeVersion = this.processingEnv.getOptions().get("forgeVersion");
		String[] split = forgeVersion.split("\\.");
		if(split.length > 0) {
			return "[" + split[0] + ",)";
		}
		return "[" + forgeVersion + ",)";
	}

	private String phantomVersion() {
		String version = this.processingEnv.getOptions().get("phantomVersion");
		String[] split = version.split("\\.");
		if(split.length > 0) try {
			return "[" + version + "," + (Integer.parseInt(split[0]) + 1) + ".0)";
		} catch (NumberFormatException e) {
			this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Invalid number format in " + version);
		}
		return "[" + version + ",)";
	}

	private String minecraftVersion() {
		String version = this.processingEnv.getOptions().get("minecraftVersion");
		String[] split = version.split("\\.");
		if(split.length > 1) try {
			return "[" + version + ",1." + (Integer.parseInt(split[1]) + 1) + ")";
		} catch (NumberFormatException e) {
			this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Invalid number format in " + version);
		}
		return "[" + version + ",)";
	}
}
