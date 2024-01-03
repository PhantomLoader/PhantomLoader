package io.github.phantomloader.library.forge.platform;

import io.github.phantomloader.library.platform.Platform;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;

/**
 * <p>
 *     Forge implementation of {@link Platform}.
 * </p>
 *
 * @author Nico
 */
public class ForgePlatform implements Platform {

    @Override
    public String platformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String mod) {
        return ModList.get().isLoaded(mod);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public boolean isClientSide() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    @Override
    public boolean isServerSide() {
        return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
    }
}
