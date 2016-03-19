package com.m4thg33k.gemulation.block;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.api.GemulationStateProps;
import com.m4thg33k.gemulation.gui.GemulationGuiHandler;
import com.m4thg33k.gemulation.lib.Names;
import com.m4thg33k.gemulation.tiles.TileGemFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ImprovedFurnaceBlock extends BaseBlock {

    public static final PropertyBool ON = PropertyBool.create("on");

    public ImprovedFurnaceBlock()
    {
        super(Names.IMPROVED_FURNACE, Material.rock,2.0f,5.0f);

        this.setDefaultState(this.blockState.getBaseState().withProperty(GemulationStateProps.CARDINALS, EnumFacing.NORTH).withProperty(ON,false));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this,GemulationStateProps.CARDINALS,ON);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileGemFurnace tileEntity = (TileGemFurnace)worldIn.getTileEntity(pos);
        return state.withProperty(ON,(tileEntity).getOn()).withProperty(GemulationStateProps.CARDINALS,(tileEntity.getFacing()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileGemFurnace tileEntity = (TileGemFurnace)worldIn.getTileEntity(pos);
        tileEntity.setFacing(placer.getHorizontalFacing().getOpposite());
        if (stack.hasDisplayName())
        {
            tileEntity.setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileGemFurnace(-1); //special case
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote)
        {
            playerIn.openGui(Gemulation.instance, GemulationGuiHandler.GEM_FURNACE_GUI, worldIn,pos.getX(),pos.getY(),pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileGemFurnace te = (TileGemFurnace)worldIn.getTileEntity(pos);

        InventoryHelper.dropInventoryItems(worldIn,pos,te);
        super.breakBlock(worldIn, pos, state);
    }
}
