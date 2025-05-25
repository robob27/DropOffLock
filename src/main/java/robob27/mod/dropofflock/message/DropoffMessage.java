package robob27.mod.dropofflock.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import robob27.mod.dropofflock.inventory.InteractionResult;
import robob27.mod.dropofflock.inventory.InventoryData;
import robob27.mod.dropofflock.render.RendererCubeTarget;
import robob27.mod.dropofflock.task.DropoffTask;
import robob27.mod.dropofflock.util.ByteBufUtilsExt;
import robob27.mod.dropofflock.util.LockHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DropoffMessage implements IMessage {
  public boolean[] lockedArray;

  public static DropoffMessage fromLockedArray() {
    boolean[] lockedArray = LockHandler.getLockedArray();

    DropoffMessage message = new DropoffMessage(lockedArray);
    return message;
  }
  
  /**
  * Leave public default constructor for Netty.
  */
  @SuppressWarnings("WeakerAccess")
  public DropoffMessage() {
  }
  
  @Override
  public void toBytes(ByteBuf buf) {
    String lockedArrayString = Arrays.toString(lockedArray);
    buf.writeInt(lockedArray != null ? lockedArrayString.length() : 0);
    ByteBufUtil.writeUtf8(buf, lockedArray != null ? lockedArrayString : "null");
  }
  
  @Override
  public void fromBytes(ByteBuf buf) {
    int length = buf.readInt();
    if (length > 0) {
      String lockedArrayString = buf.readCharSequence(length, Charset.forName("utf-8")).toString();
      String[] lockedArrayStrings = lockedArrayString.substring(1, lockedArrayString.length() - 1).split(", ");
      this.lockedArray = new boolean[lockedArrayStrings.length];
      for (int i = 0; i < lockedArrayStrings.length; i++) {
        this.lockedArray[i] = Boolean.parseBoolean(lockedArrayStrings[i]);
      }
    } else {
      this.lockedArray = null;
    }
  }

  public DropoffMessage(boolean[] lockedArray) {
    this.lockedArray = lockedArray;
  }
  
  public static class Handler implements IMessageHandler<DropoffMessage, IMessage> {
    
    @Nullable
    private DropoffTask dropoffTask;
    
    @Override
    public IMessage onMessage(DropoffMessage message, @Nonnull MessageContext ctx) {
      EntityPlayerMP player = ctx.getServerHandler().player;

      // we can't actually read the locked array here in multiplayer because
      // the LockHandler file exists only on the client side
      boolean[] lockedArray = message.lockedArray;

      dropoffTask = new DropoffTask(player, lockedArray);
      dropoffTask.run();
      
      List<InventoryData> inventoryDataList = dropoffTask.getInventoryDataList();
      List<RendererCubeTarget> rendererCubeTargets = new ArrayList<>();
      int affectedContainers = 0;
      
      for (InventoryData inventoryData : inventoryDataList) {
        Color color;
        
        if (inventoryData.getInteractionResult() == InteractionResult.DROPOFF_SUCCESS) {
          ++affectedContainers;
          color = new Color(0, 255, 0);
        } else {
          color = new Color(255, 0, 0);
        }
        
        for (TileEntity entity : inventoryData.getEntities()) {
          RendererCubeTarget rendererCubeTarget = new RendererCubeTarget(entity.getPos(), color);
          
          rendererCubeTargets.add(rendererCubeTarget);
        }
      }
      
      return new ReportMessage(dropoffTask.getItemsCounter(), affectedContainers, inventoryDataList.size(),
      rendererCubeTargets);
    }
    
  }
  
}
