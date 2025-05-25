package robob27.mod.dropofflock.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import robob27.mod.dropofflock.config.DropOffConfig;
import robob27.mod.dropofflock.util.LockHandler;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SortingHandler {
  
  private final InventoryManager inventoryManager;
  private final ItemStackComparator itemStackComparator;
  private int startSlot;
  private int endSlot;
  private final boolean[] lockedArray;
  
  public SortingHandler(InventoryManager inventoryManager, boolean[] lockedArray) {
    this.inventoryManager = inventoryManager;
    this.lockedArray = lockedArray;
    itemStackComparator = new ItemStackComparator();
  }
  
  public void setStartSlot(int value) {
    startSlot = value;
  }
  
  public void setEndSlot(int value) {
    endSlot = value;
  }
  
  public void sort(IInventory inventory) {
    if (endSlot < InventoryManager.Slots.FIRST) {
      endSlot = inventory.getSizeInventory();
    }
    
    mergeStacks(inventory);
    
    List<ItemStack> stacks = new ArrayList<>();
    
    for (int i = startSlot; i < endSlot; ++i) {
      ItemStack currentStack = inventory.getStackInSlot(i);

      if (LockHandler.isSlotLocked(lockedArray, i)) {
        continue;
      }
      
      if (!currentStack.isEmpty()) {
        stacks.add(currentStack);
      }
      
      inventory.setInventorySlotContents(i, ItemStack.EMPTY);
    }
    
    stacks.sort(itemStackComparator);
    
    populateInventory(inventory, stacks);
  }
  
  /**
  * This method checks the config text field to determine whether to sort the inventory with the specified name
  * or not.
  */
  public boolean isSortRequired(IInventory inventory) {
    String name = inventoryManager.getItemStackName(inventory);
    
    String[] containerNames =
    StringUtils.split(DropOffConfig.INSTANCE.sortContainersWithNames, DropOffConfig.INSTANCE.delimiter);
    
    for (String containerName : containerNames) {
      String regex = containerName.replace("*", ".*").trim();
      
      if (name.matches(regex)) {
        return true;
      }
    }
    
    return false;
  }
  
  private void populateInventory(IInventory inventory, List<ItemStack> stacks) {
    int j = 0;  // index for stacks list
    for (int i = startSlot; i < endSlot; ++i) {
        // Skip locked slots when repopulating
        if (LockHandler.isSlotLocked(lockedArray, i)) {
            continue;
        }
        
        if (j < stacks.size()) {
            ItemStack currentStack = stacks.get(j);
            inventory.setInventorySlotContents(i, currentStack);
            j++;  // Only increment j when we actually place an item
        }
    }
  }
  
  private void mergeStacks(IInventory inventory) {
    for (int i = startSlot; i < endSlot; ++i) {
      ItemStack leftStack = inventory.getStackInSlot(i);
      
      if (leftStack.isEmpty()) {
        continue;
      }

      // Skip locked slots
      if (LockHandler.isSlotLocked(lockedArray, i)) {
        continue;
      }
      
      for (int j = i + 1; j < endSlot; ++j) {
        ItemStack rightStack = inventory.getStackInSlot(j);
        
        if (rightStack.isEmpty()) {
          continue;
        }

        // Skip locked slots
        if (LockHandler.isSlotLocked(lockedArray, j)) {
          continue;
        }
        
        if (inventoryManager.isStacksEqual(leftStack, rightStack)) {
          int maxSize = inventoryManager.getMaxAllowedStackSize(inventory, leftStack);
          
          if (leftStack.getCount() + rightStack.getCount() <= maxSize) {
            int leftStackNewSize = leftStack.getCount() + rightStack.getCount();
            leftStack.setCount(leftStackNewSize);
            
            inventory.setInventorySlotContents(j, ItemStack.EMPTY);
          } else {
            int leftToMax = maxSize - leftStack.getCount();
            int rightStackNewSize = rightStack.getCount() - leftToMax;
            
            leftStack.setCount(maxSize);
            rightStack.setCount(rightStackNewSize);
          }
        }
      }
    }
  }
  
}
