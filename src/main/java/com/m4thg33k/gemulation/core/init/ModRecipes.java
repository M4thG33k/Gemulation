package com.m4thg33k.gemulation.core.init;

import com.m4thg33k.gemulation.block.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;

public class ModRecipes {

    public static void init()
    {
        //furnaces
        for (int i=0;i< EnumGem.values().length;i++)
        {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.gemFurnaceBlock,1,i)," g "," f "," g ",'f',new ItemStack(Blocks.furnace,1),'g',new ItemStack(ModItems.gem,1,i));
        }
    }
}
