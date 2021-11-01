package li.netcube.mcvm.server.commands;

import li.netcube.mcvm.common.items.Items;
import li.netcube.mcvm.util.vm.MachineManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;


public class createCD extends CommandBase {
    @Override
    public String getName() {
        return "createCD";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "createCD <name> [path]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException {
        if (args.length > 0) {
            ics.sendMessage(new TextComponentString("Creating \"" + args[0] + "\" ..."));
            ItemStack cdStack = new ItemStack(Items.cdrom);
            String filename = DimensionManager.getCurrentSaveRootDirectory() + server.getFolderName() + "/" + args[0] + ".iso";
            if (args.length > 1) {
                filename = args[1];
            }
            if (MachineManager.checkImage(new File(filename))) {
                Items.cdrom.setFilename(cdStack, filename);
                getCommandSenderAsPlayer(ics).inventory.addItemStackToInventory(cdStack);
                ics.sendMessage(new TextComponentString("Done!"));
            } else {
                ics.sendMessage(new TextComponentString("Failed: Not Found!"));
            }
        } else {
            ics.sendMessage(new TextComponentString("/createCD <name> [path]"));
        }
    }
}
