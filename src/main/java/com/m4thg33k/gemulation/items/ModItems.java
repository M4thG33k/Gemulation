package com.m4thg33k.gemulation.items;

import com.m4thg33k.gemulation.lib.Names;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static Item itemFurnaceUpgrade;

    public static void createItems()
    {
        GameRegistry.registerItem(itemFurnaceUpgrade = new ItemFurnaceUpgrade(), Names.FURNACE_UPGRADE);
    }
}
