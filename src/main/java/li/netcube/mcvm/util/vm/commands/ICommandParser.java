package li.netcube.mcvm.util.vm.commands;

import li.netcube.mcvm.common.ui.ComputerContainerTileEntity;

public interface ICommandParser {
    boolean canHandleCommand(String command, ComputerContainerTileEntity computerTileEntity);
    String parseCommand(String command, ComputerContainerTileEntity computerTileEntity);
}
