package com.m4thg33k.gemulation.core.proxies;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.block.ModBlocks;
import com.m4thg33k.gemulation.client.render.item.ItemRenderRegister;
import com.m4thg33k.gemulation.gui.GemulationGuiHandler;
import com.m4thg33k.gemulation.tiles.ModTiles;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e)
    {
        ModBlocks.createBlocks();
    }

    public void init(FMLInitializationEvent e)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(Gemulation.instance,new GemulationGuiHandler());
        ModTiles.init();
    }

    public void postInit(FMLPostInitializationEvent e)
    {

    }
}
