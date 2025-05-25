package robob27.mod.dropofflock.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

import java.util.List;
import java.util.Objects;

public class InventoryData {
  
  private final List<TileEntity> entities;
  private final IInventory inventory;
  private InteractionResult interactionResult;
  
  @SuppressWarnings("SameParameterValue")
  InventoryData(List<TileEntity> entities, IInventory inventory, InteractionResult interactionResult) {
    this.entities = entities;
    this.inventory = inventory;
    this.interactionResult = interactionResult;
  }
  
  public List<TileEntity> getEntities() {
    return entities;
  }
  
  public IInventory getInventory() {
    return inventory;
  }
  
  public InteractionResult getInteractionResult() {
    return interactionResult;
  }
  
  @SuppressWarnings("SameParameterValue")
  void setInteractionResult(InteractionResult interactionResult) {
    this.interactionResult = interactionResult;
  }
  
  @Override
  public String toString() {
    return "Entities: [" + entities.toString() + "] " +
    "Inventory: [" + inventory.toString() + "] " +
    "InteractionResult: [" + interactionResult + "]";
  }
  
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    
    if (!(o instanceof InventoryData)) {
      return false;
    }
    
    InventoryData inventoryData = (InventoryData) o;
    
    return Objects.equals(entities, inventoryData.entities) &&
    Objects.equals(inventory, inventoryData.inventory) &&
    Objects.equals(interactionResult, inventoryData.interactionResult);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(entities, inventory, interactionResult);
  }
  
}
