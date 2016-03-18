package com.m4thg33k.gemulation.core.proxies;

import com.m4thg33k.gemulation.client.render.block.BlockRenderRegister;
import com.m4thg33k.gemulation.client.render.handlers.TextureHandler;
import com.m4thg33k.gemulation.client.render.item.ItemRenderRegister;
import com.m4thg33k.gemulation.network.packets.PacketNBT;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy  extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        ItemRenderRegister.registerItemRenderer();

        MinecraftForge.EVENT_BUS.register(new TextureHandler());
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);

        BlockRenderRegister.registerBlockRenderer();

    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @Override
    public void handleNBTPacket(PacketNBT message) {
        Minecraft.getMinecraft().theWorld.getTileEntity(message.pos).readFromNBT(message.compound);
        Minecraft.getMinecraft().theWorld.markBlockForUpdate(message.pos);
    }
}
