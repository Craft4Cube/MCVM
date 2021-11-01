package li.netcube.mcvm.server.commands;

import li.netcube.mcvm.common.items.Items;
import li.netcube.mcvm.util.vm.MachineManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;


public class createHDD extends CommandBase {
    @Override
    public String getName() {
        return "createHDD";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "createHDD <name> <size>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException {
        ics.sendMessage(new TextComponentString("Creating \"" + args[0] + "\" ..."));
        if (server.isDedicatedServer()) {
            MachineManager.createHddImage(new File(server.getFolderName() + "/" + args[0] + ".qcow2"), args[1]);
        } else {
            MachineManager.createHddImage(new File("saves/" + server.getFolderName() + "/" + args[0] + ".qcow2"), args[1]);
        }


        ItemStack harddiskStack = new ItemStack(Items.harddisk);
        if (server.isDedicatedServer()) {
            Items.harddisk.setFilename(harddiskStack, server.getFolderName() + "/" + args[0] + ".qcow2");
        } else {
            Items.harddisk.setFilename(harddiskStack, "saves/" + server.getFolderName() + "/" + args[0] + ".qcow2");
        }
        getCommandSenderAsPlayer(ics).inventory.addItemStackToInventory(harddiskStack);

        ics.sendMessage(new TextComponentString("Done!"));
    }
}
