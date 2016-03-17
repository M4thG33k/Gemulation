package com.m4thg33k.gemulation.tiles;

import com.m4thg33k.gemulation.lib.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

public class TileGemFurnace extends TileEntity implements IInventory{

    private ItemStack[] inventory;
    private String customName;
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
        isOn = false;
        inventory = new ItemStack[this.getSizeInventory()];
    }

    public void setFacing(EnumFacing f)
    {
        facing = f;
        markDirty();
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

        NBTTagList list = compound.getTagList("Items",10);
        for (int i=0;i<list.tagCount();i++)
        {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot")&255;
            setInventorySlotContents(slot,ItemStack.loadItemStackFromNBT(stackTag));
        }

        if (compound.hasKey("CustomName"))
        {
            setCustomName(compound.getString("CustomName"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("meta",meta);
        compound.setInteger("facing",facing.ordinal());
        compound.setBoolean("on",isOn);

        NBTTagList list = new NBTTagList();
        for (int i=0;i<getSizeInventory();i++)
        {
            if (getStackInSlot(i)!=null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        compound.setTag("Items",list);

        if (hasCustomName())
        {
            compound.setString("CustomName",getCustomName());
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setBoolean("on",isOn);
        tagCompound.setInteger("facing",facing.ordinal());
        return new S35PacketUpdateTileEntity(pos,1,tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tagCompound = pkt.getNbtCompound();
        isOn = tagCompound.getBoolean("on");
        facing = EnumFacing.VALUES[tagCompound.getInteger("facing")];
    }

    public String getCustomName()
    {
        return this.customName;
    }

    public void setCustomName(String name)
    {
        this.customName = name;
    }

    //IInventory

    @Override
    public int getSizeInventory() {
        return 3; //// TODO: 3/17/2016 make dependent on variant
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index<0 || index>=this.getSizeInventory())
        {
            return null;
        }
        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.getStackInSlot(index)!=null)
        {
            ItemStack itemstack;
            if (getStackInSlot(index).stackSize <= count)
            {
                itemstack = getStackInSlot(index);
                setInventorySlotContents(index,null);
                markDirty();
                return itemstack;
            }
            itemstack = this.getStackInSlot(index).splitStack(count);

            if (getStackInSlot(index).stackSize<=0)
            {
                setInventorySlotContents(index,null);
            }
            else
            {
                //just to show that changes happened
                setInventorySlotContents(index,getStackInSlot(index));
            }

            markDirty();
            return itemstack;
        }

        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index<0 || index>=getSizeInventory())
        {
            return null;
        }

        ItemStack stack = inventory[index];
        inventory[index] = null;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index<0 || index>=getSizeInventory())
        {
            return;
        }

        if (stack != null && stack.stackSize>getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
        if (stack != null && stack.stackSize==0)
        {
            stack = null;
        }

        inventory[index] = stack;
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(pos)==this && player.getDistanceSq(pos.add(0.5,0.5,0.5))<=64;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i=0;i<getSizeInventory();i++)
        {
            setInventorySlotContents(i,null);
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container."+ Names.GEM_FURNACE;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.equals("");
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
    }
}
