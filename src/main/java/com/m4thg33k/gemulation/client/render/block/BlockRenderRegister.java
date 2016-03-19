package com.m4thg33k.gemulation.client.render.block;

import com.m4thg33k.gemulation.block.ModBlocks;
import net.minecraft.block.Block;

public class BlockRenderRegister {

    public static void registerBlockRenderer()
    {
        reg(ModBlocks.gemFurnaceBlock);
        reg(ModBlocks.improvedFurnaceBlock);
    }

    public static void preInit()
    {
        //ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocks.gemFurnaceBlock),new ResourceLocation("gemulation:GemFurnaceBlock_0"),new ResourceLocation("gemulation:GemFurnaceBlock_1"));
    }

    public static void reg(Block block)
    {
//        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),0,new ModelResourceLocation(Gemulation.MODID + ":" + Names.IMPROVED_FURNACE,"inventory"));
        //Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block),0, new ModelResourceLocation(Gemulation.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
    }
}
