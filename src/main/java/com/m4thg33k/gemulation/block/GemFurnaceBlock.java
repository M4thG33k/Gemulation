package com.m4thg33k.gemulation.block;

import com.m4thg33k.gemulation.lib.Names;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.EnumGem;

import java.util.List;

public class GemFurnaceBlock extends BaseBlock{

    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

    public GemFurnaceBlock()
    {
        super(Names.GEM_FURNACE, Material.iron,5.0f,10.0f);

        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this,new IProperty[]{VARIANT});
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(itemIn,1,0));
        list.add(new ItemStack(itemIn,1,1));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT,EnumGem.get(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumGem) state.getValue(VARIANT)).id;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this),1,this.getMetaFromState(world.getBlockState(pos)));
    }
}
