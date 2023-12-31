package io.github.phantomloader.library.fabric.platform;

import io.github.phantomloader.library.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

/**
 * <p>
 *     Fabric implementation of {@link Platform}.
 * </p>
 *
 * @author Nico
 */
public class FabricPlatform implements Platform {

    @Override
    public String platformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String mod) {
        return FabricLoader.getInstance().isModLoaded(mod);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isClientSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public boolean isServerSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
    }
}
