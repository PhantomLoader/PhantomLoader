package absloader.library;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("test")
public class TestForge {

	public TestForge() {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
