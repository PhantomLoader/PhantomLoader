package io.github.phantomloader.library.forge.platform;

import io.github.phantomloader.library.platform.Platform;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

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
}
