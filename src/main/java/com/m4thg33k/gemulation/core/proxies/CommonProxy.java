package com.m4thg33k.gemulation.core.proxies;

import com.m4thg33k.gemulation.block.ModBlocks;
import com.m4thg33k.gemulation.client.render.item.ItemRenderRegister;
import com.m4thg33k.gemulation.tiles.ModTiles;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e)
    {
        ModBlocks.createBlocks();
    }

    public void init(FMLInitializationEvent e)
    {
        ModTiles.init();
    }

    public void postInit(FMLPostInitializationEvent e)
    {

    }
}
