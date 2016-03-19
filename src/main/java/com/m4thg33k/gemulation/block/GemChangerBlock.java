package com.m4thg33k.gemulation.block;

import com.m4thg33k.gemulation.lib.Names;
import com.m4thg33k.gemulation.tiles.TileGemChanger;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
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
}
