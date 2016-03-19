package com.m4thg33k.gemulation.client.render.item;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.block.ModBlocks;
import com.m4thg33k.gemulation.lib.Names;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.silentchaos512.gems.lib.EnumGem;

public class ItemRenderRegister {


    public static void registerItemRenderer()
    {
        for (int i=0;i<12;i++)
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.gemFurnaceBlock),i,new ModelResourceLocation("gemulation:"+Names.GEM_FURNACE,"facing=north,on=false,variant="+ EnumGem.values()[i].getName().toLowerCase()));
        }
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.improvedFurnaceBlock),0,new ModelResourceLocation(Gemulation.MODID+":"+Names.IMPROVED_FURNACE,"facing=north,on=false"));
    }
}
