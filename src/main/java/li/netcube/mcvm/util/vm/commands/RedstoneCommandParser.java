package li.netcube.mcvm.util.vm.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import li.netcube.mcvm.common.blocks.ComputerBlock;
import li.netcube.mcvm.common.ui.ComputerContainerTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class RedstoneCommandParser implements ICommandParser {
    @Override
    public boolean canHandleCommand(JsonObject command, ComputerContainerTileEntity computerTileEntity) {
        if (command.has("subsystem"))
        if (command.get("subsystem").getAsString().equals("redstone")) {
            return true;
        }
        return false;
    }

    @Override
    public JsonObject parseCommand(JsonObject command, ComputerContainerTileEntity computerTileEntity) {
        JsonObject response = new JsonObject();

        String side = "";
        String action = "";
        int value = 0;

        if (command.has("subsystem")) {
            response.add("subsystem", new JsonPrimitive(command.get("subsystem").getAsString()));
        }

        if (command.has("side")) {
            side = command.get("side").getAsString();
            response.add("side", new JsonPrimitive(command.get("side").getAsString()));
        }

        if (command.has("action")) {
            action = command.get("action").getAsString();
            response.add("action", new JsonPrimitive(command.get("action").getAsString()));
        }

        if (command.has("value")) {
            value = command.get("value").getAsInt();
        }

        if (action.toLowerCase().equals("set")) {
            if (!command.has("value")) {
                response.add("result", new JsonPrimitive("error"));
                response.add("error", new JsonPrimitive("invalid parameter"));
                response.add("parameter", new JsonPrimitive("value"));
                return response;
            }
            switch (side) {
                case "up":
                    computerTileEntity.setOutputState(EnumFacing.UP, value);
                    response.add("result", new JsonPrimitive("ok"));
                    return response;
                case "down":
                    computerTileEntity.setOutputState(EnumFacing.DOWN, value);
                    response.add("result", new JsonPrimitive("ok"));
                    return response;
                case "north":
                    computerTileEntity.setOutputState(EnumFacing.NORTH.getOpposite(), value);
                    response.add("result", new JsonPrimitive("ok"));
                    return response;
                case "east":
                    computerTileEntity.setOutputState(EnumFacing.EAST.getOpposite(), value);
                    response.add("result", new JsonPrimitive("ok"));
                    return response;
                case "south":
                    computerTileEntity.setOutputState(EnumFacing.SOUTH.getOpposite(), value);
                    response.add("result", new JsonPrimitive("ok"));
                    return response;
                case "west":
                    computerTileEntity.setOutputState(EnumFacing.WEST.getOpposite(), value);
                    response.add("result", new JsonPrimitive("ok"));
                    return response;
                case "top":
                    computerTileEntity.setOutputState(EnumFacing.UP, value);
                    response.add("result", new JsonPrimitive("ok"));
                    return response;
                case "bottom":
                    computerTileEntity.setOutputState(EnumFacing.DOWN, value);
                    response.add("result", new JsonPrimitive("ok"));
                    return response;
                case "front":
                    computerTileEntity.setOutputState(convertDirectionToFace("front", computerTileEntity).getOpposite(), value);
                    response.add("result", new JsonPrimitive("ok"));
                    return response;
                case "back":
                    computerTileEntity.setOutputState(convertDirectionToFace("back", computerTileEntity).getOpposite(), value);
                    response.add("result", new JsonPrimitive("ok"));
                    return response;
                case "left":
                    computerTileEntity.setOutputState(convertDirectionToFace("left", computerTileEntity).getOpposite(), value);
                    response.add("result", new JsonPrimitive("ok"));
                    return response;
                case "right":
                    computerTileEntity.setOutputState(convertDirectionToFace("right", computerTileEntity).getOpposite(), value);
                    response.add("result", new JsonPrimitive("ok"));
                    return response;
                default:
                    response.add("result", new JsonPrimitive("error"));
                    response.add("error", new JsonPrimitive("invalid parameter"));
                    response.add("parameter", new JsonPrimitive("side"));
                    return response;
            }
        } else if (action.toLowerCase().equals("get")) {
            switch (side) {
                case "up":
                    response.add("result", new JsonPrimitive("ok"));
                    response.add("value", new JsonPrimitive(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.UP), EnumFacing.UP.getOpposite())));
                    return response;
                case "down":
                    response.add("result", new JsonPrimitive("ok"));
                    response.add("value", new JsonPrimitive(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.DOWN), EnumFacing.DOWN.getOpposite())));
                    return response;
                case "north":
                    response.add("result", new JsonPrimitive("ok"));
                    response.add("value", new JsonPrimitive(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.NORTH), EnumFacing.NORTH.getOpposite())));
                    return response;
                case "redstone.get.east":
                    response.add("result", new JsonPrimitive("ok"));
                    response.add("value", new JsonPrimitive(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.EAST), EnumFacing.EAST.getOpposite())));
                    return response;
                case "redstone.get.south":
                    response.add("result", new JsonPrimitive("ok"));
                    response.add("value", new JsonPrimitive(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.SOUTH), EnumFacing.SOUTH.getOpposite())));
                    return response;
                case "redstone.get.west":
                    response.add("result", new JsonPrimitive("ok"));
                    response.add("value", new JsonPrimitive(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.WEST), EnumFacing.WEST.getOpposite())));
                    return response;
                case "redstone.get.top":
                    response.add("result", new JsonPrimitive("ok"));
                    response.add("value", new JsonPrimitive(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.UP), EnumFacing.UP.getOpposite())));
                    return response;
                case "redstone.get.bottom":
                    response.add("result", new JsonPrimitive("ok"));
                    response.add("value", new JsonPrimitive(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(EnumFacing.DOWN), EnumFacing.DOWN.getOpposite())));
                    return response;
                case "redstone.get.front":
                    response.add("result", new JsonPrimitive("ok"));
                    response.add("value", new JsonPrimitive(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(convertDirectionToFace("front", computerTileEntity)), convertDirectionToFace("front", computerTileEntity).getOpposite())));
                    return response;
                case "redstone.get.back":
                    response.add("result", new JsonPrimitive("ok"));
                    response.add("value", new JsonPrimitive(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(convertDirectionToFace("back", computerTileEntity)), convertDirectionToFace("back", computerTileEntity).getOpposite())));
                    return response;
                case "redstone.get.left":
                    response.add("result", new JsonPrimitive("ok"));
                    response.add("value", new JsonPrimitive(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(convertDirectionToFace("left", computerTileEntity)), convertDirectionToFace("left", computerTileEntity).getOpposite())));
                    return response;
                case "redstone.get.right":
                    response.add("result", new JsonPrimitive("ok"));
                    response.add("value", new JsonPrimitive(computerTileEntity.getWorld().getRedstonePower(computerTileEntity.getPos().offset(convertDirectionToFace("right", computerTileEntity)), convertDirectionToFace("right", computerTileEntity).getOpposite())));
                    return response;
                default:
                    response.add("result", new JsonPrimitive("error"));
                    response.add("error", new JsonPrimitive("invalid parameter"));
                    response.add("parameter", new JsonPrimitive("side"));
                    return response;
            }
        } else {
            response.add("result", new JsonPrimitive("error"));
            response.add("error", new JsonPrimitive("invalid action"));
            response.add("parameter", new JsonPrimitive("side"));
            return response;
        }
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