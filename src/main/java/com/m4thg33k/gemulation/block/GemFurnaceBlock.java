package com.m4thg33k.gemulation.block;

import com.m4thg33k.gemulation.api.GemulationStateProps;
import com.m4thg33k.gemulation.lib.Names;
import com.m4thg33k.gemulation.tiles.TileGemFurnace;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.EnumGem;

import java.util.List;

public class GemFurnaceBlock extends BaseBlock implements ITileEntityProvider{

    public static final PropertyEnum<EnumGem> VARIANT = PropertyEnum.create("variant", EnumGem.class);
    public static final PropertyBool ON = PropertyBool.create("on");

    public GemFurnaceBlock()
    {
        super(Names.GEM_FURNACE, Material.iron,5.0f,10.0f);

        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY).withProperty(GemulationStateProps.CARDINALS, EnumFacing.NORTH).withProperty(ON,false));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this,VARIANT,GemulationStateProps.CARDINALS,ON);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (int i=0;i<EnumGem.values().length;i++) {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT,EnumGem.get(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(VARIANT)).id;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity==null || !(tileEntity instanceof TileGemFurnace))
        {
            return state;
        }
        return state.withProperty(ON,((TileGemFurnace) tileEntity).getOn()).withProperty(GemulationStateProps.CARDINALS,((TileGemFurnace) tileEntity).getFacing());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this),1,this.getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity!=null && tileEntity instanceof TileGemFurnace)
        {
            ((TileGemFurnace) tileEntity).setFacing(placer.getHorizontalFacing().getOpposite());
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileGemFurnace(meta);
    }
}
