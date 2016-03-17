package com.m4thg33k.gemulation.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileGemFurnace extends TileEntity{

    private int meta;
    private EnumFacing facing;
    private boolean isOn;

    public TileGemFurnace()
    {
        this(0);
    }

    public TileGemFurnace(int m)
    {
        super();
        meta = m;
        facing = EnumFacing.NORTH;
        System.out.print("Setting to default on");
        if (worldObj.isRemote)
        {
            System.out.print("Client\n");
        }
        else{
            System.out.print("Server\n");
        }
        isOn = false;
    }

    public void setFacing(EnumFacing f)
    {
        facing = f;
    }

    public boolean getOn()
    {
        return isOn;
    }

    public boolean toggleOn()
    {
        worldObj.markBlockForUpdate(pos);
        markDirty();
        isOn = !isOn;
        return isOn;
    }

    public EnumFacing getFacing()
    {
        return facing;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        meta = compound.getInteger("meta");
        facing = EnumFacing.VALUES[compound.getInteger("facing")];
        isOn = compound.getBoolean("on");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("meta",meta);
        compound.setInteger("facing",facing.ordinal());
        compound.setBoolean("on",isOn);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setBoolean("on",isOn);
        return new S35PacketUpdateTileEntity(pos,1,tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tagCompound = pkt.getNbtCompound();
        isOn = tagCompound.getBoolean("on");
    }
}
