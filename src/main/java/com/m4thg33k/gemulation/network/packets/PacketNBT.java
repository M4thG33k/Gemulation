package com.m4thg33k.gemulation.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketNBT implements IMessage {

    public BlockPos pos;
    public NBTTagCompound compound;

    public PacketNBT()
    {

    }

    public PacketNBT(BlockPos pos,NBTTagCompound tagCompound)
    {
        this.pos = pos;
        compound = tagCompound;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(ByteBufUtils.readVarInt(buf,5),ByteBufUtils.readVarInt(buf,5),ByteBufUtils.readVarInt(buf,5));
        compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf,pos.getX(),5);
        ByteBufUtils.writeVarInt(buf,pos.getY(),5);
        ByteBufUtils.writeVarInt(buf,pos.getZ(),5);
        ByteBufUtils.writeTag(buf,compound);
    }
}
