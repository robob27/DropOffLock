package robob27.mod.dropofflock.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import robob27.mod.dropofflock.config.DropOffConfig;
import robob27.mod.dropofflock.message.DropoffMessage;
import robob27.mod.dropofflock.util.ClientUtils;

public class GuiScreenEventHandler {
  
  public static final GuiScreenEventHandler INSTANCE = new GuiScreenEventHandler();
  
  @SubscribeEvent(priority = EventPriority.HIGH)
  public void onActionPreformed(GuiScreenEvent.ActionPerformedEvent.Pre event) {
    if (!DropOffConfig.INSTANCE.overrideQuarkButton ||
    !(event.getGui() instanceof GuiInventory || event.getGui() instanceof GuiContainerCreative) ||
    !event.getButton().getClass().getName().equals("vazkii.quark.management.client.gui.GuiButtonChest") ||
    !GuiScreen.isShiftKeyDown()) {
      return;
    }
    
    event.setCanceled(true);
    
    ClientUtils.sendNoSpectator(DropoffMessage.fromLockedArray());
  }

  // Not used for now - disabling locking of hotbar items
	// @SubscribeEvent
	// public void onRenderOverlayEvent(RenderGameOverlayEvent.Post e) {
  //     if (e.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE && e.getType() != RenderGameOverlayEvent.ElementType.JUMPBAR) {
  //       return;
  //     }
  //     if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null && !Minecraft.getMinecraft().player.isSpectator()) {
  //       RenderOverlay.drawHotbar();
  //     }
	// }
	
	@SubscribeEvent
	public void onContainerForegroundEvent(GuiContainerEvent.DrawForeground e) {
		GuiContainer gui = e.getGuiContainer();
		if (gui == null) {
			return;
		}
		
		RenderOverlay.drawScreen(gui);
	}
}
