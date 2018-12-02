package li.netcube.mcvm.common.ui;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Arrays;

public class ComputerSlot extends SlotItemHandler {

    public Item[] allowedItems;

    public ComputerSlot(IItemHandler itemHandler, int par2, int par3, int par4, Item[] allowedItems) {
        super(itemHandler, par2, par3, par4);
        this.allowedItems = allowedItems;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        //if (itemstack.getCount() <= 1) {
            return Arrays.asList(allowedItems).contains(itemstack.getItem());
        //} else {
        //    return false;
        //}
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }
}
