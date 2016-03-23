package com.m4thg33k.gemulation.block.ItemBlocks;

import com.m4thg33k.gemulation.lib.GemChestType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class GemChestItemBlock extends ItemBlock{

    public GemChestItemBlock(Block block)
    {
        super(block);

        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return GemChestType.validateMeta(damage);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "tile.gemulation:"+GemChestType.values()[stack.getMetadata()].name().toLowerCase()+"_chest";
    }
}
