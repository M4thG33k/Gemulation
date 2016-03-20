package com.m4thg33k.gemulation.block;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.gui.GemulationGuiHandler;
import com.m4thg33k.gemulation.lib.Names;
import com.m4thg33k.gemulation.tiles.TileGemChanger;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class GemChangerBlock extends BaseBlock {

    public GemChangerBlock()
    {
        super(Names.GEM_CHANGER, Material.iron,5.0f,10.f);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileGemChanger();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote)
        {
            playerIn.openGui(Gemulation.instance, GemulationGuiHandler.GEM_CHANGER_GUI,worldIn,pos.getX(),pos.getY(),pos.getZ());
        }
        return true;
    }
}
