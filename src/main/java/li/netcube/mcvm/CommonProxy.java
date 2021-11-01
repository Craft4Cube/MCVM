package li.netcube.mcvm;

import li.netcube.mcvm.common.blocks.Blocks;
import li.netcube.mcvm.common.items.*;
import li.netcube.mcvm.common.ui.ComputerContainerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(Blocks.computer);
        event.getRegistry().registerAll(Blocks.display);
        GameRegistry.registerTileEntity(ComputerContainerTileEntity.class, MCVM.MODID + ":computerblock");
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemBlock(Blocks.computer).setRegistryName(Blocks.computer.getRegistryName()));
        event.getRegistry().registerAll(new ItemBlock(Blocks.display).setRegistryName(Blocks.display.getRegistryName()));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(Items.cdrom);
        event.getRegistry().register(Items.floppy);
        event.getRegistry().register(Items.harddisk);
        event.getRegistry().register(Items.memory1);
        event.getRegistry().register(Items.memory2);

        event.getRegistry().register(Items.pcb);
        event.getRegistry().register(Items.ic);
        event.getRegistry().register(Items.ic2);
        event.getRegistry().register(Items.ic3);
        event.getRegistry().register(Items.cpu);
        event.getRegistry().register(Items.mainboard);
        event.getRegistry().register(Items.cddrive);
        event.getRegistry().register(Items.floppydrive);
        event.getRegistry().register(Items.diskplatter);

        event.getRegistry().register(Items.nic_e1000);
        event.getRegistry().register(Items.nic_rtl8139);
        event.getRegistry().register(Items.nic_ne2k);
    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        registerRender(Items.cdrom);
        registerRender(Items.floppy);
        registerRender(Items.harddisk);
        registerRender(Items.memory1);
        registerRender(Items.memory2);

        registerRender(Items.pcb);
        registerRender(Items.ic);
        registerRender(Items.ic2);
        registerRender(Items.ic3);
        registerRender(Items.cpu);
        registerRender(Items.mainboard);
        registerRender(Items.cddrive);
        registerRender(Items.floppydrive);
        registerRender(Items.diskplatter);

        registerRender(Items.nic_e1000);
        registerRender(Items.nic_rtl8139);
        registerRender(Items.nic_ne2k);

        registerRender(Item.getItemFromBlock(Blocks.computer));
        registerRender(Item.getItemFromBlock(Blocks.display));
    }

    private static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
    }

}
