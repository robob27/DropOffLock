package robob27.mod.dropofflock;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import robob27.mod.dropofflock.config.ConfigChangeEventHandler;
import robob27.mod.dropofflock.gui.GuiOpenEventHandler;
import robob27.mod.dropofflock.gui.GuiScreenEventHandler;
import robob27.mod.dropofflock.render.RenderWorldLastEventHandler;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {
  
  @Override
  public void preInit(FMLPreInitializationEvent event) {
    // Register key bindings.
    ClientRegistry.registerKeyBinding(KeyInputEventHandler.INSTANCE.dropoffTaskKeybinding);
    ClientRegistry.registerKeyBinding(KeyInputEventHandler.INSTANCE.lockItemKeyBinding);
  }
  
  @Override
  public void init(FMLInitializationEvent event) {
    // Register event handlers.
    MinecraftForge.EVENT_BUS.register(RenderWorldLastEventHandler.INSTANCE);
    MinecraftForge.EVENT_BUS.register(GuiOpenEventHandler.INSTANCE);
    MinecraftForge.EVENT_BUS.register(GuiScreenEventHandler.INSTANCE);
    MinecraftForge.EVENT_BUS.register(KeyInputEventHandler.INSTANCE);
    MinecraftForge.EVENT_BUS.register(ConfigChangeEventHandler.INSTANCE);
  }
  
}
