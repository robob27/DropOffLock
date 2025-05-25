package robob27.mod.dropofflock;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import robob27.mod.dropofflock.message.DropoffMessage;
import robob27.mod.dropofflock.util.ClientUtils;
import robob27.mod.dropofflock.util.LockHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

import org.lwjgl.input.Keyboard;

public class KeyInputEventHandler {
  
  static final KeyInputEventHandler INSTANCE = new KeyInputEventHandler();
  
  final KeyBinding dropoffTaskKeybinding;
  final KeyBinding lockItemKeyBinding;
  
  private KeyInputEventHandler() {
    dropoffTaskKeybinding = new KeyBinding(DropOff.MOD_NAME, Keyboard.KEY_X, DropOff.MOD_NAME);
    lockItemKeyBinding = new KeyBinding(DropOff.MOD_NAME + " Lock Item", Keyboard.KEY_C, DropOff.MOD_NAME);
  }
  
  @SubscribeEvent
  public void onConnect(EntityJoinWorldEvent e) {
    if (e.getEntity() instanceof EntityPlayer && e.getEntity() == Minecraft.getMinecraft().player) {
      LockHandler.readFromFile();
    }
  }
  
  @SubscribeEvent
  public void onGuiKeyboardInputEvent(KeyboardInputEvent.Pre e) {
    if (GameSettings.isKeyDown(lockItemKeyBinding) && e.getGui() instanceof GuiContainer) {
      Slot slot = ((GuiContainer) e.getGui()).getSlotUnderMouse();
      
      if(slot != null && slot.inventory != null && slot.inventory instanceof InventoryPlayer) {
        // only main inventory slots, not hotbar, equipped items, or offhand
        if (slot.getSlotIndex() > 8 && slot.getSlotIndex() < 36) {
          LockHandler.toggleSlot(slot);
          return;
        }
      }
    }
  }
  
  @SubscribeEvent
  public void onKeyInput(InputEvent.KeyInputEvent event) {
    if (dropoffTaskKeybinding.isPressed()) {
      ClientUtils.sendNoSpectator(DropoffMessage.fromLockedArray());
      return;
    }
    
    return;
  }
}
