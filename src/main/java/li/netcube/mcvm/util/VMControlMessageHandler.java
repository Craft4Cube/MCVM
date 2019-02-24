package li.netcube.mcvm.util;

import li.netcube.mcvm.common.items.NetworkCard;
import li.netcube.mcvm.common.items.VMStorageItem;
import li.netcube.mcvm.common.ui.ComputerContainerTileEntity;
import li.netcube.mcvm.util.vm.VirtualMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.ItemStackHandler;

import java.io.File;

public class VMControlMessageHandler implements IMessageHandler<VMControlMessage, IMessage> {
    @Override
    public IMessage onMessage(VMControlMessage message, MessageContext ctx) {
        // This is the player the packet was sent to the server from
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
        // The value that was sent
        BlockPos computer = message.computer;
        int command = message.command;
        // Execute the action on the main server thread by adding it as a scheduled task

        serverPlayer.getServerWorld().addScheduledTask(() -> {
            ComputerContainerTileEntity ccte = (ComputerContainerTileEntity) serverPlayer.getServerWorld().getTileEntity(computer);
            if (ccte != null) {
                switch (command) {
                    case 1:
                        if (!ccte.vm.isRunning()) {
                            ItemStackHandler ish = ccte.getItemStackHandler();
                            String hdd0 = VMStorageItem.getFilename(ish.getStackInSlot(0));
                            String hdd1 = VMStorageItem.getFilename(ish.getStackInSlot(1));
                            int memory = 0;
                            if (ish.getStackInSlot(2).getItem() == li.netcube.mcvm.common.items.Items.memory1) {
                                memory += 256;
                            } else if (ish.getStackInSlot(2).getItem() == li.netcube.mcvm.common.items.Items.memory2) {
                                memory += 512;
                            }
                            if (ish.getStackInSlot(3).getItem() == li.netcube.mcvm.common.items.Items.memory1) {
                                memory += 256;
                            } else if (ish.getStackInSlot(3).getItem() == li.netcube.mcvm.common.items.Items.memory2) {
                                memory += 512;
                            }
                            String cd0 = VMStorageItem.getFilename(ish.getStackInSlot(4));
                            String cd1 = VMStorageItem.getFilename(ish.getStackInSlot(5));
                            String floppy0 = VMStorageItem.getFilename(ish.getStackInSlot(6));
                            String floppy1 = VMStorageItem.getFilename(ish.getStackInSlot(7));

                            String nic = null;

                            if (!ish.getStackInSlot(8).isEmpty()) {
                                nic = ((NetworkCard) ish.getStackInSlot(8).getItem()).getType();
                            }

                            File fHdd0 = (hdd0 == null) ? null : new File(hdd0);
                            File fHdd1 = (hdd1 == null) ? null : new File(hdd1);
                            File fCd0 = (cd0 == null) ? null : new File(cd0);
                            File fCd1 = (cd1 == null) ? null : new File(cd1);
                            File fFloppy0 = (floppy0 == null) ? null : new File(floppy0);
                            File fFloppy1 = (floppy1 == null) ? null : new File(floppy1);


                            ccte.vm = new VirtualMachine(ccte, ccte.vm.id, ccte.PASSWD, memory + "M", fHdd0, fHdd1, fFloppy0, fFloppy1, fCd0, fCd1, nic, computer);
                            ccte.vm.start();
                        }
                        break;

                    case 2:
                        if (ccte.vm.isRunning()) {
                            ccte.vm.stop();
                        }
                        break;

                    case 3:
                        if (ccte.vm.isRunning()) {
                            ccte.vm.reset();
                        }
                        break;

                    default:
                        break;

                }

            }
        });
        // No response packet
        return null;
    }
}
