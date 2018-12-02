package li.netcube.mcvm.common.blocks;

import li.netcube.mcvm.client.creativeTabs.MCVM;
import net.minecraft.block.material.Material;

public class Blocks {
    public static ComputerBlock computer = new ComputerBlock("computer", Material.IRON, MCVM.mcvmTab);
    public static DisplayBlock display = new DisplayBlock("display", Material.IRON, MCVM.mcvmTab);
}
