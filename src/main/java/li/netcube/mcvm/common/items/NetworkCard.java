package li.netcube.mcvm.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class NetworkCard extends Item {

    private String type = "";
    private String description = "";

    public NetworkCard(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setMaxStackSize(1);
    }

    public NetworkCard(String name, CreativeTabs tab, String type, String description) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setMaxStackSize(1);
        setCreativeTab(tab);

        this.type = type;
        this.description = description;
    }

    public String getType()
    {
        return this.type;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, World world, List<String> lores, ITooltipFlag b)
    {
        if (b.isAdvanced()) {
            lores.add(description + " (" + type + ")");
        } else {
            lores.add(description);
        }

    }

}
