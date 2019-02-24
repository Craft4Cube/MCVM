package li.netcube.mcvm.common.ui;

import li.netcube.mcvm.MCVM;
import li.netcube.mcvm.common.items.Items;
import li.netcube.mcvm.common.items.NetworkCard;
import li.netcube.mcvm.common.items.VMStorageItem;
import li.netcube.mcvm.util.VMControlMessage;
import li.netcube.mcvm.util.vm.MachineManager;
import li.netcube.mcvm.util.vm.VirtualMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.io.File;
import java.util.UUID;

public class ComputerContainerTileEntity extends TileEntity implements ITickable {

    public Integer UID = 0;
    public String PASSWD = "";
    public boolean poweredOn = false;
    public VirtualMachine vm = new VirtualMachine(this, UID, PASSWD, "4M", null,null,null,null,null,null, null, null);

    private boolean redstoneChanged = true;

    //UP, DOWN, N,E,S,W
    public static int[] outPowerSides = {0,0,0,0,0,0};

    public static final int SIZE = 9;

    // This item handler will hold our nine inventory slots
    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted

            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                switch (slot) {
                    case 0:
                        ItemStack slot0 = itemStackHandler.getStackInSlot(0);
                        if (VMStorageItem.getFilename(slot0) == null) {
                            vm.hdd0 = null;
                        } else if (!MachineManager.checkImage(new File(VMStorageItem.getFilename(slot0)))) {
                            String uuid = UUID.randomUUID().toString();
                            String imgFile = "";
                            if (server.isDedicatedServer()) {
                                imgFile = server.getFolderName() + "/" + uuid + ".qcow2";
                            } else {
                                imgFile = "saves/" + server.getFolderName() + "/" + uuid + ".qcow2";
                            }
                            MachineManager.createHddImage(new File(imgFile), VMStorageItem.getSize(slot0));
                            VMStorageItem.setFilename(slot0, imgFile);
                            vm.hdd0 = new File(imgFile);
                        } else {
                            vm.hdd0 = new File(VMStorageItem.getFilename(slot0));
                        }
                        vm.stop();
                        break;
                    case 1:
                        ItemStack slot1 = itemStackHandler.getStackInSlot(1);
                        if (VMStorageItem.getFilename(slot1) == null) {
                            vm.hdd1 = null;
                        } else if (!MachineManager.checkImage(new File(VMStorageItem.getFilename(slot1)))) {
                            String uuid = UUID.randomUUID().toString();
                            String imgFile = "";
                            if (server.isDedicatedServer()) {
                                imgFile = server.getFolderName() + "/" + uuid + ".qcow2";
                            } else {
                                imgFile = "saves/" + server.getFolderName() + "/" + uuid + ".qcow2";
                            }
                            MachineManager.createHddImage(new File(imgFile), VMStorageItem.getSize(slot1));
                            VMStorageItem.setFilename(slot1, imgFile);
                            vm.hdd1 = new File(imgFile);
                        } else {
                            vm.hdd1 = new File(VMStorageItem.getFilename(slot1));
                        }
                        vm.stop();
                        break;
                    case 2:
                    case 3:
                        int memory = 0;
                        if (itemStackHandler.getStackInSlot(2).getItem() == Items.memory1) {
                            memory += 256;
                        } else if (itemStackHandler.getStackInSlot(2).getItem() == Items.memory2) {
                            memory += 512;
                        }
                        if (itemStackHandler.getStackInSlot(3).getItem() == Items.memory1) {
                            memory += 256;
                        } else if (itemStackHandler.getStackInSlot(3).getItem() == Items.memory2) {
                            memory += 512;
                        }
                        vm.memory = memory + "M";
                        vm.stop();
                        break;
                    case 4:
                        String cd0 = VMStorageItem.getFilename(itemStackHandler.getStackInSlot(4));
                        vm.setCd0((cd0 == null) ? null : new File(cd0));
                        break;
                    case 5:
                        String cd1 = VMStorageItem.getFilename(itemStackHandler.getStackInSlot(5));
                        vm.setCd1((cd1 == null) ? null : new File(cd1));
                        break;
                    case 6:
                        ItemStack slot6 = itemStackHandler.getStackInSlot(6);
                        if (VMStorageItem.getFilename(slot6) == null) {
                            vm.setFloppy0(null);
                        } else if (!MachineManager.checkImage(new File(VMStorageItem.getFilename(slot6)))) {
                            String uuid = UUID.randomUUID().toString();
                            String imgFile = "";
                            if (server.isDedicatedServer()) {
                                imgFile = server.getFolderName() + "/" + uuid + ".img";
                            } else {
                                imgFile = "saves/" + server.getFolderName() + "/" + uuid + ".img";
                            }
                            MachineManager.createFloppyImage(new File(imgFile));
                            VMStorageItem.setFilename(slot6, imgFile);
                            vm.setFloppy0(new File(imgFile));
                        } else {
                            vm.setFloppy0(new File(VMStorageItem.getFilename(slot6)));
                        }
                        break;
                    case 7:
                        ItemStack slot7 = itemStackHandler.getStackInSlot(7);
                        if (VMStorageItem.getFilename(slot7) == null) {
                            vm.setFloppy1(null);
                        } else if (!MachineManager.checkImage(new File(VMStorageItem.getFilename(slot7)))) {
                            String uuid = UUID.randomUUID().toString();
                            String imgFile = "";
                            if (server.isDedicatedServer()) {
                                imgFile = server.getFolderName() + "/" + uuid + ".img";
                            } else {
                                imgFile = "saves/" + server.getFolderName() + "/" + uuid + ".img";
                            }
                            MachineManager.createFloppyImage(new File(imgFile));
                            VMStorageItem.setFilename(slot7, imgFile);
                            vm.setFloppy1(new File(imgFile));
                        } else {
                            vm.setFloppy1(new File(VMStorageItem.getFilename(slot7)));
                        }
                        break;
                    case 8:
                        String nic = null;
                        if (!itemStackHandler.getStackInSlot(8).isEmpty()) {
                            nic = ((NetworkCard) itemStackHandler.getStackInSlot(8).getItem()).getType();
                        }
                        vm.nic = nic;
                        vm.stop();
                        break;
                }
            }
            ComputerContainerTileEntity.this.markDirty();
        }
    };

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
        if (compound.hasKey("uid")) {
            UID = compound.getInteger("uid");
            vm.setUid(UID);
            //System.out.println("Read ID: " + UID);
        }
        if (compound.hasKey("passwd")) {
            PASSWD = compound.getString("passwd");
            vm.setPassword(PASSWD);
            //System.out.println("Read Passwd: " + PASSWD);
        }
        if (compound.hasKey("poweredOn")) {
            poweredOn = compound.getBoolean("poweredOn");
            //System.out.println("Read poweredOn: " + poweredOn);
            try {
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            } catch (NullPointerException ignored) {

            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", itemStackHandler.serializeNBT());
        compound.setInteger("uid", UID);
        compound.setString("passwd", PASSWD);
        compound.setBoolean("poweredOn", poweredOn);
        //System.out.println("Wrote poweredOn: " + poweredOn);
        return compound;
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
        }
        return super.getCapability(capability, facing);
    }

    public ItemStackHandler getItemStackHandler() {
        return this.itemStackHandler;
    }

    public int getOutputState(EnumFacing side) {
        switch (side) {
            case UP: return outPowerSides[0];
            case DOWN: return outPowerSides[1];
            case NORTH: return outPowerSides[2];
            case EAST: return outPowerSides[3];
            case SOUTH: return outPowerSides[4];
            case WEST: return outPowerSides[5];
        }
        return 0;
    }

    public void setOutputState(EnumFacing side, int value) {
         switch (side) {
            case UP: outPowerSides[0] = value; break;
            case DOWN: outPowerSides[1] = value; break;
            case NORTH: outPowerSides[2] = value; break;
            case EAST: outPowerSides[3] = value; break;
            case SOUTH: outPowerSides[4] = value; break;
            case WEST: outPowerSides[5] = value; break;
        }
        redstoneChanged = true;
    }


    public void setUID(int id) {
        vm.setUid(id);
        //System.out.println("Set ID: " + id);
        UID = id;
        sendUpdates();
    }

    public void setPassword(String password) {
        vm.setPassword(password);
        //System.out.println("Set ID: " + id);
        PASSWD = password;
        sendUpdates();
    }

    public void setPoweredOn(boolean state) {
        poweredOn = state;
        //System.out.println("Set PoweredOn: " + state);
        sendUpdates();
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

    private void sendUpdates() {
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
        ComputerContainerTileEntity.this.markDirty();
    }

    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    public void powerOn() {
        MCVM.INSTANCE.sendToServer(new VMControlMessage(this.pos, 1));
    }

    public void powerOff() {
        MCVM.INSTANCE.sendToServer(new VMControlMessage(this.pos, 2));
    }

    public void resetSystem() {
        MCVM.INSTANCE.sendToServer(new VMControlMessage(this.pos, 3));
    }

    @Override
    public void update() {
        if (redstoneChanged) {
            world.notifyNeighborsOfStateChange(this.pos, world.getBlockState(this.pos).getBlock(), true);
            redstoneChanged = false;
        }
    }
}