package li.netcube.mcvm.util.vm.commands;

import li.netcube.mcvm.common.ui.ComputerContainerTileEntity;
import com.google.gson.JsonObject;

public interface ICommandParser {
    boolean canHandleCommand(JsonObject command, ComputerContainerTileEntity computerTileEntity);
    JsonObject parseCommand(JsonObject command, ComputerContainerTileEntity computerTileEntity);
}
