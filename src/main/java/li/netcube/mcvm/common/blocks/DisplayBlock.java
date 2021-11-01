package li.netcube.mcvm.common.blocks;

import li.netcube.mcvm.MCVM;
import li.netcube.mcvm.common.items.Items;
import li.netcube.mcvm.common.items.VMStorageItem;
import li.netcube.mcvm.common.ui.ComputerContainerTileEntity;
import li.netcube.mcvm.client.ui.DisplayGUI;
import li.netcube.mcvm.util.VMControlMessage;
import li.netcube.mcvm.util.vm.VirtualMachine;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.Sys;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;

public class DisplayBlock extends Block {

    public static final int GUI_ID = 1;

    public static class Properties
    {
        public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    }

    public DisplayBlock(String name, Material material, CreativeTabs creativeTabs) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(creativeTabs);
        setHardness(5);
        setDefaultState( this.blockState.getBaseState()
                .withProperty( Properties.FACING, EnumFacing.NORTH )
        );
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer( this, Properties.FACING );
    }

    @Nonnull
    @Override
    @Deprecated
    public IBlockState getStateFromMeta( int meta )
    {
        EnumFacing dir = EnumFacing.getFront( meta & 0x7 );
        if( dir.getAxis() == EnumFacing.Axis.Y )
        {
            dir = EnumFacing.NORTH;
        }
        IBlockState state = getDefaultState().withProperty( Properties.FACING, dir );
        return state;
    }

    @Override
    public int getMetaFromState( IBlockState state )
    {
        int meta = state.getValue( Properties.FACING ).getIndex();
        return meta;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw).getOpposite();
        return this.getDefaultState().withProperty(Properties.FACING, enumfacing);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side,
                                    float hitX, float hitY, float hitZ) {

        if (world.isRemote) {
            BlockPos computer = getComputer(world, pos);
            //MCVM.INSTANCE.sendToServer(new VMControlMessage(computer, 1));

            if (computer != null) {
                ComputerContainerTileEntity ccte = (ComputerContainerTileEntity) world.getTileEntity(computer);
                if (ccte != null) {
                    if (ccte.poweredOn) {
                        DisplayGUI display = new DisplayGUI();
                        display.VNCPort = ccte.vm.VNCPort;
                        display.VNCPassword = ccte.vm.password;
                        //System.out.println(display.VNCPassword);
                        Minecraft.getMinecraft().displayGuiScreen(display);
                        return true;
                    }
                }
            }
        }
        return true;
    }

    public BlockPos getComputer(World world, BlockPos center)
    {
        ArrayList<BlockPos> possibleLocations = new ArrayList<>();
        possibleLocations.add(new BlockPos(center.getX()+1, center.getY(), center.getZ()));
        possibleLocations.add(new BlockPos(center.getX()-1, center.getY(), center.getZ()));
        possibleLocations.add(new BlockPos(center.getX(), center.getY()+1, center.getZ()));
        possibleLocations.add(new BlockPos(center.getX(), center.getY()-1, center.getZ()));
        possibleLocations.add(new BlockPos(center.getX(), center.getY(), center.getZ()+1));
        possibleLocations.add(new BlockPos(center.getX(), center.getY(), center.getZ()-1));

        for (BlockPos location : possibleLocations) {
            if (world.getTileEntity(location) instanceof ComputerContainerTileEntity) {
                return location;
            }
        }
        return null;
    }
}