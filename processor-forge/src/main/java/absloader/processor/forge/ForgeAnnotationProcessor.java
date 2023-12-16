package absloader.processor.forge;

import absloader.processor.ModAnnotationProcessor;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;

public class ForgeAnnotationProcessor extends ModAnnotationProcessor {

	@Override
	protected void generateFile(Element modMethod) {
		String className = modMethod.getEnclosingElement().getSimpleName().toString();
		String basePackage = this.processingEnv.getElementUtils().getPackageOf(modMethod).getQualifiedName().toString();
		Filer filer = this.processingEnv.getFiler();
		try {
			JavaFileObject sourceFile = filer.createSourceFile("ForgeInitializer");
			try(PrintWriter writer = new PrintWriter(sourceFile.openWriter())) {
				writer.println("package " + basePackage + ".forge;");
				writer.println();
				writer.println("import " + basePackage + "." + className + ";");
				writer.println();
				writer.println("import net.minecraftforge.common.MinecraftForge;");
				writer.println("import net.minecraftforge.fml.common.Mod;");
				writer.println();
				writer.println("@Mod(\"generated\")");
				writer.println("public class ForgeInitializer {");
				writer.println();
				writer.println("	public ForgeInitializer() {");
				writer.println("		" + className + "." + modMethod.getSimpleName() + "();");
				writer.println("		MinecraftForge.EVENT_BUS.register(this);");
				writer.println("	}");
				writer.println("}");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Generated Forge");
	}
}
