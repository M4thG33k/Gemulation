package com.m4thg33k.gemulation.core.proxies;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.block.ModBlocks;
import com.m4thg33k.gemulation.client.render.item.ItemRenderRegister;
import com.m4thg33k.gemulation.core.init.ModRecipes;
import com.m4thg33k.gemulation.gui.GemulationGuiHandler;
import com.m4thg33k.gemulation.network.packets.GemulationPackets;
import com.m4thg33k.gemulation.network.packets.PacketNBT;
import com.m4thg33k.gemulation.tiles.ModTiles;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e)
    {
        GemulationPackets.init();
        ModBlocks.createBlocks();
    }

    public void init(FMLInitializationEvent e)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(Gemulation.instance,new GemulationGuiHandler());
        ModTiles.init();
        ModRecipes.init();
    }

    public void postInit(FMLPostInitializationEvent e)
    {

    }

    public void handleNBTPacket(PacketNBT message)
    {

    }
}
