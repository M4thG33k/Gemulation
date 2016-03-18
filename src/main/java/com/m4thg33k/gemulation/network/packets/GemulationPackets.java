package com.m4thg33k.gemulation.network.packets;

import com.m4thg33k.gemulation.Gemulation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class GemulationPackets {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Gemulation.MODID);

    public static void init()
    {
        INSTANCE.registerMessage(PacketHandlerNBT.class,PacketNBT.class,0, Side.CLIENT);
    }
}
