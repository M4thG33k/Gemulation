package com.m4thg33k.gemulation.network.packets;

import com.m4thg33k.gemulation.Gemulation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerNBT implements IMessageHandler<PacketNBT,IMessage>{

    @Override
    public IMessage onMessage(PacketNBT message, MessageContext ctx) {
        Gemulation.proxy.handleNBTPacket(message);

        return null;
    }
}
