package li.netcube.mcvm.util.vm.commands;

import li.netcube.mcvm.common.blocks.ComputerBlock;
import li.netcube.mcvm.common.ui.ComputerContainerTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class RedstoneCommandParser implements ICommandParser {
    @Override
    public boolean canHandleCommand(String command, ComputerContainerTileEntity computerTileEntity) {
        String[] commandParts = command.split("\\.");
        if (commandParts.length >= 2) {
            if (commandParts[0].equals("redstone") && (commandParts[1].equals("get") || commandParts[1].equals("set"))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String parseCommand(String command, ComputerContainerTileEntity computerTileEntity) {
        String[] commandParts = command.split("\\.");
        if (commandParts.length >= 2) switch (commandParts[0] + "." + commandParts[1] + "." + commandParts[2]) {
            case "redstone.set.up":
                computerTileEntity.setOutputState(EnumFacing.UP, Integer.parseInt(commandParts[3]));
                return "ok";
            case "redstone.set.down":
                computerTileEntity.setOutputState(EnumFacing.DOWN, Integer.parseInt(commandParts[3]));
                return "ok";
            case "redstone.set.north":
                computerTileEntity.setOutputState(EnumFacing.NORTH.getOpposite(), Integer.parseInt(commandParts[3]));
                return "ok";
            case "redstone.set.east":
                computerTileEntity.setOutputState(EnumFacing.EAST.getOpposite(), Integer.parseInt(commandParts[3]));
                return "ok";
            case "redstone.set.south":
                computerTileEntity.setOutputState(EnumFacing.SOUTH.getOpposite(), Integer.parseInt(commandParts[3]));
                return "ok";
            case "redstone.set.west":
                computerTileEntity.setOutputState(EnumFacing.WEST.getOpposite(), Integer.parseInt(commandParts[3]));
                return "ok";
            case "redstone.set.top":
                computerTileEntity.setOutputState(EnumFacing.UP, Integer.parseInt(commandParts[3]));
                return "ok";
            case "redstone.set.bottom":
                computerTileEntity.setOutputState(EnumFacing.DOWN, Integer.parseInt(commandParts[3]));
                return "ok";
            case "redstone.set.front":
                computerTileEntity.setOutputState(convertDirectionToFace("front", computerTileEntity).getOpposite(), Integer.parseInt(commandParts[3]));
                return "ok";
            case "redstone.set.back":
                computerTileEntity.setOutputState(convertDirectionToFace("back", computerTileEntity).getOpposite(), Integer.parseInt(commandParts[3]));
                return "ok";
            case "redstone.set.left":
                computerTileEntity.setOutputState(convertDirectionToFace("left", computerTileEntity).getOpposite(), Integer.parseInt(commandParts[3]));
                return "ok";
            case "redstone.set.right":
                computerTileEntity.setOutputState(convertDirectionToFace("right", computerTileEntity).getOpposite(), Integer.parseInt(commandParts[3]));
                return "ok";

            case "redstone.get.up":
                return Integer.toString(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.UP), EnumFacing.UP.getOpposite()));
            case "redstone.get.down":
                return Integer.toString(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.DOWN), EnumFacing.DOWN.getOpposite()));
            case "redstone.get.north":
                return Integer.toString(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.NORTH), EnumFacing.NORTH.getOpposite()));
            case "redstone.get.east":
                return Integer.toString(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.EAST), EnumFacing.EAST.getOpposite()));
            case "redstone.get.south":
                return Integer.toString(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.SOUTH), EnumFacing.SOUTH.getOpposite()));
            case "redstone.get.west":
                return Integer.toString(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.WEST), EnumFacing.WEST.getOpposite()));
            case "redstone.get.top":
                return Integer.toString(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.UP), EnumFacing.UP.getOpposite()));
            case "redstone.get.bottom":
                return Integer.toString(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.DOWN), EnumFacing.DOWN.getOpposite()));
            case "redstone.get.front":
                return Integer.toString(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(convertDirectionToFace("front", computerTileEntity)), convertDirectionToFace("front", computerTileEntity).getOpposite()));
            case "redstone.get.back":
                return Integer.toString(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(convertDirectionToFace("back", computerTileEntity)), convertDirectionToFace("back", computerTileEntity).getOpposite()));
            case "redstone.get.left":
                return Integer.toString(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(convertDirectionToFace("left", computerTileEntity)), convertDirectionToFace("left", computerTileEntity).getOpposite()));
            case "redstone.get.right":
                return Integer.toString(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(convertDirectionToFace("right", computerTileEntity)), convertDirectionToFace("right", computerTileEntity).getOpposite()));
        }
        return "error.parse.failed";
    }


    public EnumFacing convertDirectionToFace(String dir, ComputerContainerTileEntity computerContainerTileEntity) {
        World world = computerContainerTileEntity.getWorld();
        EnumFacing computerFacing = world.getBlockState(computerContainerTileEntity.getPos()).getValue(ComputerBlock.Properties.FACING);

        switch (dir.toLowerCase()) {
            case "up": return EnumFacing.UP;
            case "down": return EnumFacing.DOWN;
            case "left":
                switch (computerFacing) {
                    case NORTH: return EnumFacing.EAST;
                    case EAST: return EnumFacing.SOUTH;
                    case SOUTH: return EnumFacing.WEST;
                    case WEST: return EnumFacing.NORTH;
                } break;
            case "right":
                switch (computerFacing) {
                    case NORTH: return EnumFacing.WEST;
                    case EAST: return EnumFacing.NORTH;
                    case SOUTH: return EnumFacing.EAST;
                    case WEST: return EnumFacing.SOUTH;
                } break;
            case "front": return computerFacing;
            case "back": return computerFacing.getOpposite();
        }

        return computerFacing;
    }
}