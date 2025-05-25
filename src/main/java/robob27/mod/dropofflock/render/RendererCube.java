package robob27.mod.dropofflock.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import robob27.mod.dropofflock.config.DropOffConfig;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RendererCube {
  
  public static final RendererCube INSTANCE = new RendererCube();
  private List<RendererCubeTarget> rendererCubeTargets = new ArrayList<>();
  private long currentTime;
  
  public void draw(List<RendererCubeTarget> rendererCubeTargets) {
    this.rendererCubeTargets = rendererCubeTargets;
    currentTime = System.currentTimeMillis();
  }
  
  /**
  * This method called by RenderWorldLastEvent handler.
  * It does nothing until the draw() method assign the necessary delay to the global field named currentTime.
  */
  void tryToRender(RenderWorldLastEvent event) {
    if (System.currentTimeMillis() >= currentTime + DropOffConfig.INSTANCE.highlightDelay &&
    DropOffConfig.INSTANCE.highlightDelay >= 0L) {
      return;
    }
    
    for (RendererCubeTarget rendererCubeTarget : rendererCubeTargets) {
      prepareToRender(event, rendererCubeTarget.getColor());
      render(rendererCubeTarget.getBlockPos());
      finishRender();
    }
  }
  
  private void prepareToRender(RenderWorldLastEvent event, Color color) {
    EntityPlayerSP player = Minecraft.getMinecraft().player;
    
    double playerX = player.prevPosX + (player.posX - player.prevPosX) * event.getPartialTicks() - 0.5;
    double playerY = player.prevPosY + (player.posY - player.prevPosY) * event.getPartialTicks();
    double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.getPartialTicks() - 0.5;
    
    GL11.glDisable(GL11.GL_LIGHTING);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glEnable(GL11.GL_LINE_SMOOTH);
    GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
    GL11.glLineWidth(1.0f);
    GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
    GL11.glPushMatrix();
    GL11.glTranslated(-playerX, -playerY, -playerZ);
  }
  
  private void render(BlockPos blockPos) {
    float horOffset = 0.49f;
    float verOffset = 0.99f;
    
    float x = blockPos.getX();
    float y = blockPos.getY();
    float z = blockPos.getZ();
    
    // Edge 1.
    GL11.glBegin(GL11.GL_LINE_STRIP);
    GL11.glVertex3f(x + horOffset, y, z + horOffset);
    GL11.glVertex3f(x - horOffset, y, z + horOffset);
    GL11.glEnd();
    
    // Edge 2.
    GL11.glBegin(GL11.GL_LINE_STRIP);
    GL11.glVertex3f(x + horOffset, y, z - horOffset);
    GL11.glVertex3f(x - horOffset, y, z - horOffset);
    GL11.glEnd();
    
    // Edge 3.
    GL11.glBegin(GL11.GL_LINE_STRIP);
    GL11.glVertex3f(x + horOffset, y + verOffset, z + horOffset);
    GL11.glVertex3f(x - horOffset, y + verOffset, z + horOffset);
    GL11.glEnd();
    
    // Edge 4.
    GL11.glBegin(GL11.GL_LINE_STRIP);
    GL11.glVertex3f(x + horOffset, y + verOffset, z - horOffset);
    GL11.glVertex3f(x - horOffset, y + verOffset, z - horOffset);
    GL11.glEnd();
    
    // Edge 5.
    GL11.glBegin(GL11.GL_LINE_STRIP);
    GL11.glVertex3f(x + horOffset, y, z + horOffset);
    GL11.glVertex3f(x + horOffset, y + verOffset, z + horOffset);
    GL11.glEnd();
    
    // Edge 6.
    GL11.glBegin(GL11.GL_LINE_STRIP);
    GL11.glVertex3f(x + horOffset, y, z - horOffset);
    GL11.glVertex3f(x + horOffset, y + verOffset, z - horOffset);
    GL11.glEnd();
    
    // Edge 7.
    GL11.glBegin(GL11.GL_LINE_STRIP);
    GL11.glVertex3f(x - horOffset, y, z + horOffset);
    GL11.glVertex3f(x - horOffset, y + verOffset, z + horOffset);
    GL11.glEnd();
    
    // Edge 8.
    GL11.glBegin(GL11.GL_LINE_STRIP);
    GL11.glVertex3f(x - horOffset, y, z - horOffset);
    GL11.glVertex3f(x - horOffset, y + verOffset, z - horOffset);
    GL11.glEnd();
    
    // Edge 9.
    GL11.glBegin(GL11.GL_LINE_STRIP);
    GL11.glVertex3f(x + horOffset, y, z + horOffset);
    GL11.glVertex3f(x + horOffset, y, z - horOffset);
    GL11.glEnd();
    
    // Edge 10.
    GL11.glBegin(GL11.GL_LINE_STRIP);
    GL11.glVertex3f(x - horOffset, y, z + horOffset);
    GL11.glVertex3f(x - horOffset, y, z - horOffset);
    GL11.glEnd();
    
    // Edge 11.
    GL11.glBegin(GL11.GL_LINE_STRIP);
    GL11.glVertex3f(x + horOffset, y + verOffset, z + horOffset);
    GL11.glVertex3f(x + horOffset, y + verOffset, z - horOffset);
    GL11.glEnd();
    
    // Edge 12.
    GL11.glBegin(GL11.GL_LINE_STRIP);
    GL11.glVertex3f(x - horOffset, y + verOffset, z + horOffset);
    GL11.glVertex3f(x - horOffset, y + verOffset, z - horOffset);
    GL11.glEnd();
  }
  
  private void finishRender() {
    GL11.glEnable(GL11.GL_LIGHTING);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glPopMatrix();
  }
  
}
