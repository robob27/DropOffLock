package robob27.mod.dropofflock.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import robob27.mod.dropofflock.config.DropOffGuiConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

@SuppressWarnings("unused")
public class GuiFactory implements IModGuiFactory {
  
  @Override
  public void initialize(Minecraft minecraft) {
    //
  }
  
  @Override
  public boolean hasConfigGui() {
    return true;
  }
  
  @Override
  public GuiScreen createConfigGui(@Nonnull GuiScreen parentScreen) {
    return new DropOffGuiConfig(parentScreen);
  }
  
  @Nullable
  @Override
  public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
    return null;
  }
  
}
