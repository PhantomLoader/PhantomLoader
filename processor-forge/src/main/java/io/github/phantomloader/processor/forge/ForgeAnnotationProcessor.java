package io.github.phantomloader.processor.forge;

import io.github.phantomloader.library.ModEntryPoint;
import io.github.phantomloader.processor.ModAnnotationProcessor;

import javax.lang.model.element.Element;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.Set;

/**
 * <p>
 *     Implementation of {@link ModAnnotationProcessor} for Forge.
 * </p>
 *
 * @author Nico
 */
public class ForgeAnnotationProcessor extends ModAnnotationProcessor {

    @Override
    protected ModEntryPoint.Loader loader() {
        return ModEntryPoint.Loader.FORGE;
    }

    @Override
    protected void generateModClass() {
        String modId = this.processingEnv.getOptions().get("modId");
        String packageName = this.processingEnv.getOptions().get("modGroupId") + "." + modId.toLowerCase().replace("_", "") + ".forge";
        try(PrintWriter writer = new PrintWriter(this.processingEnv.getFiler().createSourceFile(packageName + ".ForgeInitializer").openWriter())) {
            writer.println("package " + packageName + ";");
            writer.println("@net.minecraftforge.fml.common.Mod(\"" + modId + "\")");
            writer.println("public class ForgeInitializer {");
            writer.println("    public ForgeInitializer() {");
            if(this.annotatedMethods.containsKey(ModEntryPoint.Side.INIT)) {
                for(Element method : this.annotatedMethods.get(ModEntryPoint.Side.INIT)) {
                    String methodClass = method.getEnclosingElement().getSimpleName().toString();
                    String methodPackage = this.processingEnv.getElementUtils().getPackageOf(method).getQualifiedName().toString();
                    writer.println("        " + methodPackage + "." + methodClass + "." + method.getSimpleName() + "();");
                }
            }
            if(this.annotatedMethods.containsKey(ModEntryPoint.Side.COMMON) || this.annotatedMethods.containsKey(ModEntryPoint.Side.CLIENT) || this.annotatedMethods.containsKey(ModEntryPoint.Side.SERVER)) {
                writer.println("        net.minecraftforge.eventbus.api.IEventBus eventBus = net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus();");
                if(this.annotatedMethods.containsKey(ModEntryPoint.Side.COMMON)) {
                    writer.println("        eventBus.addListener(this::commonSetup);");
                }
                if(this.annotatedMethods.containsKey(ModEntryPoint.Side.CLIENT)) {
                    writer.println("        eventBus.addListener(this::clientSetup);");
                }
                if(this.annotatedMethods.containsKey(ModEntryPoint.Side.SERVER)) {
                    writer.println("        eventBus.addListener(this::serverSetup);");
                }
            }
            writer.println("        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);");
            writer.println("    }");
            if(this.annotatedMethods.containsKey(ModEntryPoint.Side.COMMON)) {
                writer.println("    private void commonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent setupEvent) {");
                writer.println("        setupEvent.enqueueWork(() -> {");
                for(Element method : this.annotatedMethods.get(ModEntryPoint.Side.COMMON)) {
                    String methodClass = method.getEnclosingElement().getSimpleName().toString();
                    String methodPackage = this.processingEnv.getElementUtils().getPackageOf(method).getQualifiedName().toString();
                    writer.println("            " + methodPackage + "." + methodClass + "." + method.getSimpleName() + "();");
                }
                writer.println("        });");
                writer.println("    }");
            }
            if(this.annotatedMethods.containsKey(ModEntryPoint.Side.CLIENT)) {
                writer.println("    private void clientSetup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent setupEvent) {");
                writer.println("        setupEvent.enqueueWork(() -> {");
                for(Element method : this.annotatedMethods.get(ModEntryPoint.Side.CLIENT)) {
                    String methodClass = method.getEnclosingElement().getSimpleName().toString();
                    String methodPackage = this.processingEnv.getElementUtils().getPackageOf(method).getQualifiedName().toString();
                    writer.println("            " + methodPackage + "." + methodClass + "." + method.getSimpleName() + "();");
                }
                writer.println("        });");
                writer.println("    }");
            }
            if(this.annotatedMethods.containsKey(ModEntryPoint.Side.SERVER)) {
                writer.println("    private void serverSetup(net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent setupEvent) {");
                writer.println("        setupEvent.enqueueWork(() -> {");
                for(Element method : this.annotatedMethods.get(ModEntryPoint.Side.SERVER)) {
                    String methodClass = method.getEnclosingElement().getSimpleName().toString();
                    String methodPackage = this.processingEnv.getElementUtils().getPackageOf(method).getQualifiedName().toString();
                    writer.println("            " + methodPackage + "." + methodClass + "." + method.getSimpleName() + "();");
                }
                writer.println("        });");
                writer.println("    }");
            }
            writer.println("}");
        } catch (IOException e) {
            throw new UncheckedIOException("Could not generate mod class for forge", e);
        }
    }

    @Override
    protected void generateModFile() {
        try(PrintWriter writer = new PrintWriter(this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/mods.toml").openWriter())) {
            writer.println(tomlLine("modLoader", "javafml"));
            String forgeVersion = this.forgeVersion();
            writer.println(tomlLine("loaderVersion", forgeVersion));
            writer.println(tomlLine("license", this.processingEnv.getOptions().get("modLicense"), "All rights reserved"));
            writer.println(tomlLine("issueTrackerURL", this.processingEnv.getOptions().get("issueTracker"), this.processingEnv.getOptions().get("modSource")));
            writer.println("[[mods]]");
            writer.println(tomlLine("modId", this.processingEnv.getOptions().get("modId")));
            writer.println(tomlLine("version", this.processingEnv.getOptions().get("modVersion"), "${file.jarVersion}"));
            writer.println(tomlLine("displayName", this.processingEnv.getOptions().get("modName"), "Unnamed"));
            writer.println(tomlLine("displayURL", this.processingEnv.getOptions().get("modUrl"), this.processingEnv.getOptions().get("modSource")));
            writer.println(tomlLine("logoFile", this.processingEnv.getOptions().get("modLogo"), "logo.png"));
            writer.println(tomlLine("credits", this.processingEnv.getOptions().get("modCredits")));
            writer.println(tomlLine("authors", this.processingEnv.getOptions().get("modAuthors")));
            writer.println(tomlLine("description", this.processingEnv.getOptions().get("modDescription")));
            writer.println("[[dependencies." + this.processingEnv.getOptions().get("modId") + "]]");
            writer.println("modId=\"forge\"");
            writer.println("mandatory=true");
            writer.println(tomlLine("versionRange", forgeVersion));
            writer.println("ordering=\"NONE\"");
            writer.println("side=\"BOTH\"");
            writer.println("[[dependencies." + this.processingEnv.getOptions().get("modId") + "]]");
            writer.println("modId=\"phantom\"");
            writer.println("mandatory=true");
            writer.println(tomlLine("versionRange", this.phantomVersion()));
            writer.println("ordering=\"NONE\"");
            writer.println("side=\"BOTH\"");
            writer.println("[[dependencies." + this.processingEnv.getOptions().get("modId") + "]]");
            writer.println("modId=\"minecraft\"");
            writer.println("mandatory=true");
            writer.println(tomlLine("versionRange", this.minecraftVersion()));
            writer.println("ordering=\"NONE\"");
            writer.println("side=\"BOTH\"");
        } catch (IOException e) {
            throw new UncheckedIOException("Could not generate mods.toml file", e);
        }
    }

    /**
     * <p>
     *     Helper function used to generate a toml line.
     * </p>
     *
     * @param option The name of the option.
     * @param value The option's value.
     * @param defaultValue The default value to use in case {@code value} is null or an empty string.
     * @return The generated toml line.
     */
    private static String tomlLine(String option, String value, String defaultValue) {
        if(value == null || value.isEmpty() || value.isBlank()) {
            if(defaultValue == null || defaultValue.isEmpty() || defaultValue.isBlank()) {
                return "#" + option + "=\"\"";
            }
            return option + "=\"" + defaultValue + "\"";
        }
        return option + "=\"" + value + "\"";
    }

    /**
     * <p>
     *     Helper function used to generate a toml line.
     * </p>
     *
     * @param option The name of the option.
     * @param value The option's value.
     * @return The generated toml line.
     */
    private static String tomlLine(String option, String value) {
        return tomlLine(option, value, "");
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Set.of("forgeVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId", "modVersion", "modName", "modLicense", "modAuthors", "modCredits", "modDescription", "modUrl", "modSource", "modLogo", "issueTracker");
    }

    @Override
    protected Set<String> getRequiredOptions() {
        return Set.of("forgeVersion", "phantomVersion", "minecraftVersion", "modId", "modGroupId");
    }

    /**
     * <p>
     *     Helper function used to generate Forge's version range.
     * </p>
     *
     * @return Forge's version range.
     */
    private String forgeVersion() {
        String forgeVersion = this.processingEnv.getOptions().get("forgeVersion");
        String[] split = forgeVersion.split("\\.");
        if(split.length > 0) {
            return "[" + split[0] + ",)";
        }
        return "[" + forgeVersion + ",)";
    }

    /**
     * <p>
     *     Helper function used to generate Phantom's version range.
     * </p>
     *
     * @return Phantom's version range.
     */
    private String phantomVersion() {
        String version = this.processingEnv.getOptions().get("phantomVersion");
        String[] split = version.split("\\.");
        if(split.length > 0) try {
            return "[" + version + "," + (Integer.parseInt(split[0]) + 1) + ".0)";
        } catch (NumberFormatException e) {
            this.printWarning("Invalid number format in " + version);
        }
        return "[" + version + ",)";
    }

    /**
     * <p>
     *     Helper function used to generate Minecraft's version range.
     * </p>
     *
     * @return Minecraft's version range.
     */
    private String minecraftVersion() {
        String version = this.processingEnv.getOptions().get("minecraftVersion");
        String[] split = version.split("\\.");
        if(split.length > 1) try {
            return "[" + version + ",1." + (Integer.parseInt(split[1]) + 1) + ")";
        } catch (NumberFormatException e) {
            this.printWarning("Invalid number format in " + version);
        }
        return "[" + version + ",)";
    }
}
