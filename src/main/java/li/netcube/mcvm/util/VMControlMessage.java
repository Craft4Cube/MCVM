package li.netcube.mcvm.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class VMControlMessage implements IMessage {

    public VMControlMessage(){}

    public BlockPos computer;
    public int command;


    public VMControlMessage(BlockPos computer, int command) {
        this.computer = computer;
        this.command = command;
    }

    @Override public void toBytes(ByteBuf buf) {
        // Writes the int into the buf
        buf.writeInt(computer.getX());
        buf.writeInt(computer.getY());
        buf.writeInt(computer.getZ());
        buf.writeInt(command);
    }

    @Override public void fromBytes(ByteBuf buf) {
        // Reads the int back from the buf. Note that if you have multiple values, you must read in the same order you wrote.
        computer = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        command = buf.readInt();
    }
}
