package li.netcube.mcvm.client.creativeTabs;

import li.netcube.mcvm.common.blocks.Blocks;
import li.netcube.mcvm.common.items.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCVM {
    public static final CreativeTabs mcvmTab = new CreativeTabs("mcvm") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Item.getItemFromBlock(Blocks.computer));
        }

        @Override @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(NonNullList<ItemStack> itemList)
        {
            super.displayAllRelevantItems(itemList);

            ItemStack cdromAlpineStandard = new ItemStack(Items.cdrom);
            Items.cdrom.setFilename(cdromAlpineStandard, "shared/Alpine-3.8.1.iso");
            Items.cdrom.setType(cdromAlpineStandard, "CD");
            itemList.add(cdromAlpineStandard);

            ItemStack blankFloppy = new ItemStack(Items.floppy);
            Items.floppy.setType(blankFloppy, "FD");
            itemList.add(blankFloppy);

            ItemStack blank4gHarddisk = new ItemStack(Items.harddisk);
            Items.harddisk.setType(blank4gHarddisk, "HD");
            Items.harddisk.setSize(blank4gHarddisk, "4G");
            itemList.add(blank4gHarddisk);

            ItemStack blank8gHarddisk = new ItemStack(Items.harddisk);
            Items.harddisk.setType(blank8gHarddisk, "HD");
            Items.harddisk.setSize(blank8gHarddisk, "8G");
            itemList.add(blank8gHarddisk);

            ItemStack blank16gHarddisk = new ItemStack(Items.harddisk);
            Items.harddisk.setType(blank16gHarddisk, "HD");
            Items.harddisk.setSize(blank16gHarddisk, "16G");
            itemList.add(blank16gHarddisk);

            ItemStack blank32gHarddisk = new ItemStack(Items.harddisk);
            Items.harddisk.setType(blank32gHarddisk, "HD");
            Items.harddisk.setSize(blank32gHarddisk, "32G");
            itemList.add(blank32gHarddisk);

            ItemStack blank64gHarddisk = new ItemStack(Items.harddisk);
            Items.harddisk.setType(blank64gHarddisk, "HD");
            Items.harddisk.setSize(blank64gHarddisk, "64G");
            itemList.add(blank64gHarddisk);

            ItemStack blank128gHarddisk = new ItemStack(Items.harddisk);
            Items.harddisk.setType(blank128gHarddisk, "HD");
            Items.harddisk.setSize(blank128gHarddisk, "128G");
            itemList.add(blank128gHarddisk);

        }
    };
}
