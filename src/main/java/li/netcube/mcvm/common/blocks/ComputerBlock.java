package li.netcube.mcvm.common.blocks;

import li.netcube.mcvm.MCVM;
import li.netcube.mcvm.UsedUidData;
import li.netcube.mcvm.common.ui.ComputerContainer;
import li.netcube.mcvm.common.ui.ComputerContainerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.Random;

public class ComputerBlock extends BlockDirectional implements ITileEntityProvider {

    public static class Properties
    {
        public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        public static final PropertyBool ON = PropertyBool.create("on");
    }

    public static final int GUI_ID = 1;

    public ComputerBlock(String name, Material material, CreativeTabs creativeTabs) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(creativeTabs);
        setHardness(5);
        setDefaultState( this.blockState.getBaseState()
                .withProperty( Properties.FACING, EnumFacing.NORTH )
                .withProperty( Properties.ON, false )
        );
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer( this, Properties.FACING, Properties.ON );
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

    @Nonnull
    @Override
    @Deprecated
    public IBlockState getActualState( @Nonnull IBlockState state, IBlockAccess world, BlockPos pos )
    {
        TileEntity tile = world.getTileEntity( pos );
        if( tile != null && tile instanceof ComputerContainerTileEntity )
        {
            ComputerContainerTileEntity computer = ((ComputerContainerTileEntity)tile);
            if( computer != null && computer.poweredOn )
            {
                return state.withProperty( Properties.ON, true );
            }
            else
            {
                return state.withProperty( Properties.ON, false );
            }
        }
        return state.withProperty( Properties.ON, false );
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ComputerContainerTileEntity();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        ComputerContainerTileEntity ccte = (ComputerContainerTileEntity)worldIn.getTileEntity(pos);

        try {
            UsedUidData uud = UsedUidData.get(worldIn);
            uud.freeUid(ccte.UID);
            ccte.vm.stop();
        } catch (Exception e) {}

        for (int i = 0; i < 9; i++) {
            if (!worldIn.isRemote)
            {
                EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), ccte.getItemStackHandler().getStackInSlot(i));
                worldIn.spawnEntity(item);
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        // Only execute on the server
        if (world.isRemote) {
            return true;
        }
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof ComputerContainerTileEntity)) {
            return false;
        }
        player.openGui(MCVM.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw).getOpposite();
        return this.getDefaultState().withProperty(Properties.FACING, enumfacing);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, player, stack);
        ComputerContainerTileEntity ccte = (ComputerContainerTileEntity)world.getTileEntity(pos);

        if(ccte != null)
        {
            ccte.setWorld( world ); // Not sure why this is necessary
            ccte.setPos( pos ); // Not sure why this is necessary
        }

        UsedUidData uud = UsedUidData.get(world);
        Integer uid = uud.getNextFree();
        ccte.setUID(uid);
        ccte.setPassword(generatePassword());
    }

    public String generatePassword() {
        Random rand = new Random();

        String password = "";
        String charTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-_";
        for (int i = 0; i < 8; i++) {
            password = password + (charTable.charAt(rand.nextInt(64)));
        }

        //System.out.println("Passwd: " + password);
        return password;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess access, BlockPos pos, @Nullable EnumFacing facing) {
        return true;
    }

    @Override
    public boolean canProvidePower(IBlockState iBlockState)
    {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof ComputerContainerTileEntity) { // prevent a crash if not the right type, or is null
            return ((ComputerContainerTileEntity) tileentity).getOutputState(side);
        }

        return 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return 0;
    }

    @Override
    public void observedNeighborChange(IBlockState thisState, World world, BlockPos thisPos, Block changedBlock, BlockPos changedBlockPos) {
        TileEntity tileentity = world.getTileEntity(thisPos);
        if (tileentity instanceof ComputerContainerTileEntity) {
            //((ComputerContainerTileEntity) tileentity).vm.serial.getOutputStream()
        }
    }
}