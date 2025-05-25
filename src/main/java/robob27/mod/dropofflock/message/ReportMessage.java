package robob27.mod.dropofflock.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import robob27.mod.dropofflock.render.RendererCubeTarget;
import robob27.mod.dropofflock.task.ReportTask;
import robob27.mod.dropofflock.util.ByteBufUtilsExt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ReportMessage implements IMessage {
  
  private int itemsCounter;
  private int affectedContainers;
  private int totalContainers;
  private List<RendererCubeTarget> rendererCubeTargets = new ArrayList<>();
  
  /**
  * Leave public default constructor for Netty.
  */
  @SuppressWarnings("unused")
  public ReportMessage() {
    //
  }
  
  ReportMessage(int itemsCounter, int affectedContainers, int totalContainers,
  List<RendererCubeTarget> rendererCubeTargets) {
    this.itemsCounter = itemsCounter;
    this.affectedContainers = affectedContainers;
    this.totalContainers = totalContainers;
    this.rendererCubeTargets = rendererCubeTargets;
  }
  
  @Override
  public void toBytes(@Nonnull ByteBuf buf) {
    buf.writeInt(itemsCounter);
    buf.writeInt(affectedContainers);
    buf.writeInt(totalContainers);
    
    ByteBufUtilsExt byteBufUtilsExt = new ByteBufUtilsExt(buf);
    byteBufUtilsExt.writeRendererCubeTargets(rendererCubeTargets);
  }
  
  @Override
  public void fromBytes(@Nonnull ByteBuf buf) {
    itemsCounter = buf.readInt();
    affectedContainers = buf.readInt();
    totalContainers = buf.readInt();
    
    ByteBufUtilsExt byteBufUtilsExt = new ByteBufUtilsExt(buf);
    rendererCubeTargets = byteBufUtilsExt.readRendererCubeTargets();
  }
  
  public static class Handler implements IMessageHandler<ReportMessage, IMessage> {
    
    @Nullable
    @Override
    public IMessage onMessage(@Nonnull ReportMessage message, MessageContext ctx) {
      ReportTask reportTask = new ReportTask(message.itemsCounter, message.affectedContainers,
      message.totalContainers, message.rendererCubeTargets);
      
      reportTask.run();
      
      return null;
    }
    
  }
  
}
