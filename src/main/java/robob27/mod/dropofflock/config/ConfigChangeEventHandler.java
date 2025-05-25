package robob27.mod.dropofflock.config;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import robob27.mod.dropofflock.DropOff;

public class ConfigChangeEventHandler {
  
  public static final ConfigChangeEventHandler INSTANCE = new ConfigChangeEventHandler();
  
  @SubscribeEvent
  public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (!event.getModID().equals(DropOff.MOD_ID)) {
      return;
    }
    
    DropOff.LOGGER.info("Configuration changed.");
    DropOffConfig.INSTANCE.sync(false);
  }
  
}
