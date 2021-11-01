package li.netcube.mcvm.common.ui;

import li.netcube.mcvm.common.items.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.Sys;

import javax.annotation.Nullable;

public class ComputerContainer extends Container {

    private ComputerContainerTileEntity te;

    public ComputerContainer(IInventory playerInventory, ComputerContainerTileEntity te) {
        this.te = te;

        // This container references items out of our own inventory (the 9 slots we hold ourselves)
        // as well as the slots from the player inventory so that the user can transfer items between
        // both inventories. The two calls below make sure that slots are defined for both inventories.
        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + 74;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 58 + 74;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int slotIndex = 0;

        addSlotToContainer(new ComputerSlot(itemHandler, slotIndex, 26, 19, new Item[] {Items.harddisk})); slotIndex++;
        addSlotToContainer(new ComputerSlot(itemHandler, slotIndex, 26, 37, new Item[] {Items.harddisk})); slotIndex++;
        addSlotToContainer(new ComputerSlot(itemHandler, slotIndex, 44, 19, new Item[] {Items.memory1, Items.memory2})); slotIndex++;
        addSlotToContainer(new ComputerSlot(itemHandler, slotIndex, 44, 37, new Item[] {Items.memory1, Items.memory2})); slotIndex++;

        addSlotToContainer(new ComputerSlot(itemHandler, slotIndex, 116, 19, new Item[] {Items.cdrom})); slotIndex++;
        addSlotToContainer(new ComputerSlot(itemHandler, slotIndex, 116, 37, new Item[] {Items.cdrom})); slotIndex++;
        addSlotToContainer(new ComputerSlot(itemHandler, slotIndex, 134, 19, new Item[] {Items.floppy})); slotIndex++;
        addSlotToContainer(new ComputerSlot(itemHandler, slotIndex, 134, 37, new Item[] {Items.floppy})); slotIndex++;

        addSlotToContainer(new ComputerSlot(itemHandler, slotIndex, 62, 19, new Item[] {Items.nic_e1000, Items.nic_rtl8139, Items.nic_ne2k})); slotIndex++;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
        ItemStack previous = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(fromSlot);
        //System.out.println(fromSlot);
        //System.out.println(slot);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if (slot instanceof ComputerSlot) {
                if (!this.mergeItemStack(current, 9, 9+36, false))
                    return ItemStack.EMPTY;
            } else {
                if (current.getItem() == Items.harddisk) {
                    if (!this.mergeItemStack(current, 0, 2, false))
                        return ItemStack.EMPTY;
                } else if (current.getItem() == Items.memory1 || current.getItem() == Items.memory2) {
                    if (!this.mergeItemStack(current, 2, 4, false))
                     return ItemStack.EMPTY;
                } else if (current.getItem() == Items.cdrom) {
                    if (!this.mergeItemStack(current, 4, 6, false))
                        return ItemStack.EMPTY;
                } else if (current.getItem() == Items.floppy) {
                    if (!this.mergeItemStack(current, 6, 8, false))
                        return ItemStack.EMPTY;
                } else if (current.getItem() == Items.nic_e1000 || current.getItem() == Items.nic_rtl8139 || current.getItem() == Items.nic_ne2k) {
                    if (!this.mergeItemStack(current, 8, 9, false))
                        return ItemStack.EMPTY;
                } else {
                    if (fromSlot >= 9 && fromSlot < 9+27) {
                        if (!this.mergeItemStack(current, 9+27, 9+36, false))
                            return ItemStack.EMPTY;
                    } else {
                        if (!this.mergeItemStack(current, 9, 9+27, false))
                            return ItemStack.EMPTY;
                    }
                }
            }
            if (current.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();

            if (current.getCount() == previous.getCount())
                return ItemStack.EMPTY;
        }


        return previous;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }
}