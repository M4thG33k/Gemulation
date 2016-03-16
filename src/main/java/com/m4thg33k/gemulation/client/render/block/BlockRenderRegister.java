package com.m4thg33k.gemulation.client.render.block;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class BlockRenderRegister {

    public static void registerBlockRenderer()
    {
        reg(ModBlocks.gemFurnaceBlock);
    }

    public static void reg(Block block)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block),0, new ModelResourceLocation(Gemulation.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
    }
}
