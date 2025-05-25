package robob27.mod.dropofflock;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import robob27.mod.dropofflock.config.DropOffConfig;
import robob27.mod.dropofflock.message.DropoffMessage;
import robob27.mod.dropofflock.message.ReportMessage;
import robob27.mod.dropofflock.util.LogMessageFactory;
import robob27.mod.dropofflock.util.compat.InvTweaksCompat;

import java.io.File;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
@Mod(modid = DropOff.MOD_ID, name = DropOff.MOD_NAME, version = DropOff.MOD_VERSION, guiFactory = DropOff.GUI_FACTORY, dependencies = "required-after:unknownlibs@[1.1.2,)")
public class DropOff {
  
  public static final String MOD_ID = "dropofflock";
  public static final String MOD_NAME = "DropOffLock";
  public static final Logger LOGGER = LogManager.getLogger(MOD_ID, LogMessageFactory.INSTANCE);
  public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
  
  static final String MOD_VERSION = "1.12.2-1.0.2b";
  static final String GUI_FACTORY = "robob27.mod.dropofflock.gui.GuiFactory";
  
  private static final String SERVER_SIDE = "robob27.mod.dropofflock.CommonProxy";
  private static final String CLIENT_SIDE = "robob27.mod.dropofflock.ClientProxy";

  public static File dir;
  
  @SuppressWarnings("NullableProblems")
  @SidedProxy(serverSide = SERVER_SIDE, clientSide = CLIENT_SIDE)
  private static CommonProxy commonProxy;
  
  @SuppressWarnings("NullableProblems")
  @Mod.Instance
  DropOff dropOff;
  
  @Mod.EventHandler
  void preInit(FMLPreInitializationEvent event) {
    LOGGER.info("Beginning pre-initialization...");
    commonProxy.preInit(event);
    
    // Register messages.
    NETWORK.registerMessage(DropoffMessage.Handler.class, DropoffMessage.class, 0, Side.SERVER);
    NETWORK.registerMessage(ReportMessage.Handler.class, ReportMessage.class, 1, Side.CLIENT);
    
    // Initialize config.
    DropOffConfig.INSTANCE.init(event.getSuggestedConfigurationFile());

    // Initialize inventory tweaks config
    InvTweaksCompat.dir = Paths.get(event.getModConfigurationDirectory() + "/InvTweaksRules.txt");

    dir = new File(event.getModConfigurationDirectory(), "dropofflock");
    if (!dir.exists()) {
      if (dir.mkdirs()) {
        LOGGER.info("Created directory: {}", dir.getAbsolutePath());
      } else {
        LOGGER.warn("Failed to create directory: {}", dir.getAbsolutePath());
      }
    }
  }
  
  @Mod.EventHandler
  void init(FMLInitializationEvent event) {
    LOGGER.info("Beginning initialization...");
    commonProxy.init(event);
  }
  
}
