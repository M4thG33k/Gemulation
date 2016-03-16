package com.m4thg33k.gemulation.block;

import com.m4thg33k.gemulation.block.ItemBlocks.GemFurnaceItemBlock;
import com.m4thg33k.gemulation.lib.Names;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static Block gemFurnaceBlock;

    public static void createBlocks()
    {
        GameRegistry.registerBlock(gemFurnaceBlock = new GemFurnaceBlock(), GemFurnaceItemBlock.class, Names.GEM_FURNACE);
    }
}
