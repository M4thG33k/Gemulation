package com.m4thg33k.gemulation.tiles;

import com.m4thg33k.gemulation.lib.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.silentchaos512.gems.item.ModItems;

public class TileGemChanger extends TileEntity implements ISidedInventory {

    /**
     * Slots 0-11 will be input
     * Slots 12-23 will be output
     * Slot 24 will be the working slot
     * Slot 25 will be the ghost item slot (the one we are looking for)
     */
    protected ItemStack[] inventory = new ItemStack[26];
    protected String customName;

    public TileGemChanger()
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList list = compound.getTagList("Items",10);
        for (int i=0;i<list.tagCount();i++)
        {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getInteger("Slot");
            setInventorySlotContents(slot,ItemStack.loadItemStackFromNBT(stackTag));
        }

        customName = compound.getString("CustomName");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTTagList list = new NBTTagList();
        for (int i=0;i<getSizeInventory();i++)
        {
            if (inventory[i]!=null)
            {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setInteger("Slot",i);
                inventory[i].writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        compound.setTag("Items",list);

        compound.setString("CustomName",customName);
    }

    @Override
    public int getSizeInventory() {
        return 26;
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
        inventory[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }
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
        return !(index<0 || index >= 12) && (stack.getItem() == ModItems.gem && index==stack.getItemDamage());
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
        return this.hasCustomName() ? this.customName : "container."+ Names.GEM_CHANGER;
    }

    @Override
    public boolean hasCustomName() {
        return customName!=null && !customName.equals("");
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return (index>=0 && index<12 && isItemValidForSlot(index,itemStackIn));
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return (index>=12 && index<24);
    }
}
