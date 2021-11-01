package li.netcube.mcvm.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.io.File;
import java.util.regex.Pattern;

import java.util.List;

public class VMStorageItem extends Item {

    public VMStorageItem(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setMaxStackSize(1);
    }

    public VMStorageItem(String name, CreativeTabs tab) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setMaxStackSize(1);
        setCreativeTab(tab);
    }

    public static void setFilename(ItemStack stack, String filename)
    {
        NBTTagCompound nbt;

        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
        }

        nbt.setString("filename", filename);

        stack.setTagCompound(nbt);
    }

    public static String getFilename(ItemStack stack)
    {
        try {
            NBTTagCompound nbt = stack.getTagCompound();
            return nbt.getString("filename");
        } catch (Exception e) {
            return null;
        }
    }

    public static void setType(ItemStack stack, String type)
    {
        NBTTagCompound nbt;

        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
        }

        nbt.setString("type", type);

        stack.setTagCompound(nbt);
    }

    public static String getType(ItemStack stack)
    {
        try {
            NBTTagCompound nbt = stack.getTagCompound();
            return nbt.getString("type");
        } catch (Exception e) {
            return null;
        }
    }

    public static void setSize(ItemStack stack, String size)
    {
        NBTTagCompound nbt;

        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
        }

        nbt.setString("size", size);

        stack.setTagCompound(nbt);
    }

    public static String getSize(ItemStack stack)
    {
        try {
            NBTTagCompound nbt = stack.getTagCompound();
            return nbt.getString("size");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onCreated(ItemStack p_onCreated_1_, World p_onCreated_2_, EntityPlayer p_onCreated_3_) {
        //System.out.println("onCreated!");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, World world, List<String> lores, ITooltipFlag b)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("size"))
        {
            lores.add(stack.getTagCompound().getString("size")+"B");
        }
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("filename"))
        {
            if (b.isAdvanced()) {
                lores.add(stack.getTagCompound().getString("filename"));
            } else {
                lores.add(new File(stack.getTagCompound().getString("filename")).getName());
            }
        }
    }
}
