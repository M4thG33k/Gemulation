package com.m4thg33k.gemulation.core.init;

import com.m4thg33k.gemulation.block.ModBlocks;
import com.m4thg33k.gemulation.lib.GemChestType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;

public class ModRecipes {

    public static void init()
    {
        //gem furnaces
        for (int i=0;i< EnumGem.values().length;i++)
        {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.gemFurnaceBlock,1,i)," g "," f "," g ",'f',new ItemStack(Blocks.furnace,1),'g',new ItemStack(ModItems.gem,1,i));
            GameRegistry.addRecipe(new ItemStack(ModBlocks.gemFurnaceBlock,1,i)," g "," f "," g ",'f',new ItemStack(ModBlocks.improvedFurnaceBlock,1),'g',new ItemStack(ModItems.gem,1,i));
        }

        //improved furnace
        GameRegistry.addRecipe(new ItemStack(ModBlocks.improvedFurnaceBlock,1)," g "," f "," g ",'f',new ItemStack(Blocks.furnace,1),'g',new ItemStack(Items.flint));

        //furnace upgrades
        GameRegistry.addRecipe(new ItemStack(com.m4thg33k.gemulation.items.ModItems.itemFurnaceUpgrade,1,0),"grb","rcr","blg",'g',new ItemStack(ModItems.gem,1,1),'r',new ItemStack(Items.redstone,1),'b',new ItemStack(ModItems.gem,1,5),'c',new ItemStack(ModItems.craftingMaterial,1,16),'l',new ItemStack(Items.dye,1,4));
        GameRegistry.addRecipe(new ItemStack(com.m4thg33k.gemulation.items.ModItems.itemFurnaceUpgrade,2,1),"tzi","zcz","ilt",'t',new ItemStack(ModItems.gem,1,2),'z',new ItemStack(Items.blaze_powder,1),'i',new ItemStack(ModItems.gem,1,8),'c',new ItemStack(ModItems.craftingMaterial,1,16),'l',new ItemStack(Items.dye,1,4));
        GameRegistry.addRecipe(new ItemStack(com.m4thg33k.gemulation.items.ModItems.itemFurnaceUpgrade,2,2),"mzo","zcz","olm",'m',new ItemStack(ModItems.gem,1,10),'z',new ItemStack(ModBlocks.improvedFurnaceBlock,1),'o',new ItemStack(ModItems.gem,1,11),'c',new ItemStack(ModItems.craftingMaterial,1,16),'l',new ItemStack(Items.dye,1,4));

        //chests
        GemChestType.registerBlocksAndRecipes(ModBlocks.gemChestBlock);

        //gem changer
        GameRegistry.addRecipe(new ItemStack(ModBlocks.gemChangerBlock,1,0),"www","coc","ooo",'w', Blocks.wool,'c',new ItemStack(ModItems.craftingMaterial,1,16),'o',Blocks.obsidian);
    }
}
