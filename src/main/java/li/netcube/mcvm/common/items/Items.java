package li.netcube.mcvm.common.items;

import li.netcube.mcvm.client.creativeTabs.MCVM;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.List;
import java.util.UUID;

public class Items {
    public static VMStorageItem cdrom = new VMStorageItem("cdrom");
    public static VMStorageItem floppy = new VMStorageItem("floppy");
    public static VMStorageItem harddisk = new VMStorageItem("harddisk");

    public static ItemBasic memory1 = new ItemBasic("memory1", MCVM.mcvmTab) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void addInformation(ItemStack stack, World world, List<String> lores, ITooltipFlag b)
        {
            lores.add("256MB");
        }
    };
    public static ItemBasic memory2 = new ItemBasic("memory2", MCVM.mcvmTab) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void addInformation(ItemStack stack, World world, List<String> lores, ITooltipFlag b)
        {
            lores.add("512MB");
        }
    };

    public static ItemBasic pcb = new ItemBasic("pcb", MCVM.mcvmTab);
    public static ItemBasic ic = new ItemBasic("ic", MCVM.mcvmTab);
    public static ItemBasic ic2 = new ItemBasic("ic2", MCVM.mcvmTab);
    public static ItemBasic ic3 = new ItemBasic("ic3", MCVM.mcvmTab);
    public static ItemBasic cpu = new ItemBasic("cpu", MCVM.mcvmTab);
    public static ItemBasic mainboard = new ItemBasic("mainboard", MCVM.mcvmTab);
    public static ItemBasic diskplatter = new ItemBasic("diskplatter", MCVM.mcvmTab);

    public static ItemBasic cddrive = new ItemBasic("cddrive", MCVM.mcvmTab);
    public static ItemBasic floppydrive = new ItemBasic("floppydrive", MCVM.mcvmTab);

    public static NetworkCard nic_e1000 = new NetworkCard("nic_e1000", MCVM.mcvmTab, "e1000", "Intel Gigabit Ethernet 10/100/1000");
    public static NetworkCard nic_rtl8139 = new NetworkCard("nic_rtl8139", MCVM.mcvmTab, "rtl8139", "Realtek Fast Ethernet 10/100");
    public static NetworkCard nic_ne2k = new NetworkCard("nic_ne2k", MCVM.mcvmTab, "ne2k_pci", "NE2000 10");
}
