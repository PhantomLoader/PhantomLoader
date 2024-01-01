package io.github.phantomloader.library.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge mod initializer.
 *
 * @author Nico
 */
@Mod("phantom")
public class ForgeInitializer {

    /**
     * Mod constructor.
     */
    public ForgeInitializer() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
