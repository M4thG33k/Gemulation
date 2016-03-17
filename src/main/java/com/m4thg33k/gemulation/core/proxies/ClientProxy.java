package com.m4thg33k.gemulation.core.proxies;

import com.m4thg33k.gemulation.client.render.block.BlockRenderRegister;
import com.m4thg33k.gemulation.client.render.item.ItemRenderRegister;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy  extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        BlockRenderRegister.preInit();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);

        BlockRenderRegister.registerBlockRenderer();
        ItemRenderRegister.registerItemRenderer();

    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }
}
