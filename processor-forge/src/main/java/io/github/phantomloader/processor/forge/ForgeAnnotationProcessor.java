package io.github.phantomloader.processor.forge;

import io.github.phantomloader.library.Mod;
import io.github.phantomloader.processor.ModAnnotationProcessor;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;

public class ForgeAnnotationProcessor extends ModAnnotationProcessor {

	@Override
	protected void generateFile(Element modMethod) throws IOException {
		Mod mod = modMethod.getAnnotation(Mod.class);
		if(mod != null) {
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
				writer.println("		" + className + "." + modMethod.getSimpleName() + "();");
				writer.println("		MinecraftForge.EVENT_BUS.register(this);");
				writer.println("	}");
				writer.println("}");
			}
			FileObject modFile = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/mods.toml");
			try(PrintWriter writer = new PrintWriter(modFile.openWriter())) {
				writer.println("modLoader=\"javafml\"");
				writer.println("loaderVersion=\"[47,)\""); // TODO: How to determine forge version?
				writer.println(tomlLine("license", mod.license()));
				writer.println(tomlLine("issueTrackerURL", mod.issues()));
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
				writer.println("    versionRange=\"[47,)\"");
				writer.println("    ordering=\"NONE\"");
				writer.println("    side=\"BOTH\"");
				writer.println("[[dependencies." + mod.id() + "]]");
				writer.println("    modId=\"phantom\"");
				writer.println("    mandatory=true");
				writer.println("    versionRange=\"[0.1,)\"");
				writer.println("    ordering=\"NONE\"");
				writer.println("    side=\"BOTH\"");
				writer.println("[[dependencies." + mod.id() + "]]");
				writer.println("    modId=\"minecraft\"");
				writer.println("    mandatory=true");
				writer.println("    versionRange=\"[1.20.1,1.21)\""); // TODO: How to determine minecraft version?
				writer.println("    ordering=\"NONE\"");
				writer.println("    side=\"BOTH\"");
			}
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
}
