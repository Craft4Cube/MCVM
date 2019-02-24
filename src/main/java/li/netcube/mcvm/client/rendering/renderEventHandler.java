package li.netcube.mcvm.client.rendering;

import li.netcube.mcvm.common.blocks.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class renderEventHandler {
    @SubscribeEvent
    public void renderOverlay(RenderBlockOverlayEvent event)
    {
        //if (event.getOverlayType() != RenderBlockOverlayEvent.OverlayType.WATER)
        //    return;

        Block target = event.getPlayer().getEntityWorld().getBlockState(event.getBlockPos()).getBlock();
        if (target != Blocks.display)
            event.setCanceled(true);
    }

}
