package com.m4thg33k.gemulation.tiles;

import com.m4thg33k.gemulation.core.util.LogHelper;
import com.m4thg33k.gemulation.lib.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.IChaosEnergyAccepter;

public class TileGemChanger extends TileEntity implements ISidedInventory, ITickable, IChaosEnergyAccepter {

    /**
     * Slots 0-11 will be input
     * Slots 12-23 will be output
     * Slot 24 will be the working slot
     * Slot 25 will be the ghost item slot (the one we are looking for)
     */
    protected ItemStack[] inventory = new ItemStack[26];
    protected String customName;

    ItemStack target;

    public static final int TIMER_DELAY = 1;
    public static final int WORK_TIME = 100;
    public static final int MAX_ENERGY = 1000000;
    public static final int ENERGY_NEEDED_TO_CHANGE = 500;
    public int timer = 0;

    public boolean[] blacklisted = new boolean[12];
    public int numBlacklisted;

    public int workTime;
    public int energyStored;

    public TileGemChanger()
    {
        resetBlacklist();
        customName = "Gem Changer";
        target = null;
    }

    public void resetBlacklist()
    {
//        LogHelper.info("Reset blacklist!");
        for (int i=0;i<12;i++)
        {
            blacklisted[i] = false;
        }
        numBlacklisted = 0;
    }

    public int getBlacklistInt()
    {
        int val = 0;
        for (int i=0;i<12;i++)
        {
            if (blacklisted[i])
            {
                val += (1<<i);
            }
        }
        return val;
    }

    public void getBlacklistArray(int val)
    {
        for (int i=11;i>=0;i--)
        {
            blacklisted[i] = (val&1)==1;
            val = val>>1;
        }
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

        timer = compound.getInteger("Timer");

        getBlacklistArray(compound.getInteger("Blacklisted"));
        numBlacklisted = compound.getInteger("NumBlacklisted");
        workTime = compound.getInteger("WorkTime");
        energyStored = compound.getInteger("StoredEnergy");
        target = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("TargetStack"));
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

        if (customName!=null && !customName.equals("")) {
            compound.setString("CustomName", customName);
        }

        compound.setInteger("Timer",timer);

        compound.setInteger("Blacklisted",getBlacklistInt());
        compound.setInteger("NumBlacklisted",numBlacklisted);
        compound.setInteger("WorkTime",workTime);
        compound.setInteger("StoredEnergy", energyStored);

        NBTTagCompound targetStack = new NBTTagCompound();
        if (target!=null)
        {
            target.writeToNBT(targetStack);
        }
        compound.setTag("TargetStack",targetStack);
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

            if (index==25)
            {
                resetBlacklist();
            }
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

    @Override
    public void update() {
        boolean isDirty = false;
        boolean wasMoved;

        increaseTimer();

        if (!worldObj.isRemote)
        {
            checkIfTargetChanged();

            isDirty = moveToWork();

            if (hasWork() && !needsToStart())
            {
                workTime--;
            }

            if (needsToStart() && hasEnoughEnergy())
            {
                energyStored -= ENERGY_NEEDED_TO_CHANGE;
                workTime--;
            }

//            if (hasWork() && hasEnoughEnergy())
//            {
//                workTime--;
//            }

            if (hasRoomForOutput() && !hasWork())
            {
                randomlyConvert();
                wasMoved = moveToOutput();
                if (!wasMoved)
                {
                    workTime = WORK_TIME;
                }
                isDirty = true;
            }
        }

        if (isDirty)
        {
            markDirty();
        }
    }

    /**
     * attempts to move a gem from the input area into the working area.
     * will not pull from the same slot as the target gem (that would be wasteful)
     * returns true if it succeeds, false if it doesn't
     */
    public boolean moveToWork()
    {
        if (inventory[24]!=null)
        {
            return false;
        }

        int blacklist = 12;
        if (inventory[25]!=null)
        {
            blacklist = inventory[25].getItemDamage();
        }

        for (int i=0;i<12;i++)
        {
            if (i == blacklist)
            {
                continue;
            }
            if (inventory[i]!=null)
            {
                setInventorySlotContents(24,new ItemStack(inventory[i].getItem(),1,inventory[i].getItemDamage()));
                decrStackSize(i,1);
                resetBlacklist();
                workTime = WORK_TIME;
                return true;
            }
        }
        return false;
    }

    /**
     * moves the gem from the working slot to its corresponding output slot
     * if there is a target gem and the gems don't match, this returns false
     * it returns true if a gem is actually moved
     *
     * this should only be called after the appropriate checks have been made
     * to ensure there is room for the gem
     */
    public boolean moveToOutput()
    {
        if(inventory[24]==null)
        {
            return false;
        }

        if (inventory[25]!=null && inventory[24].getItemDamage()!=inventory[25].getItemDamage())
        {
            return false;
        }

        int index = inventory[24].getItemDamage()+12;
        if (inventory[index]==null)
        {
            setInventorySlotContents(index,new ItemStack(inventory[24].getItem(),1,inventory[24].getItemDamage()));
        }
        else
        {
            inventory[index].stackSize++;
        }
        decrStackSize(24,1);
        return true;
    }

    /**
     * checks if there is room to place a converted gem. If there is no target gem, it checks if
     * all output slots have room for a gem (since it's randomized). Otherwise, it only checks the
     * target gem's location. If there is no gem in the working location, this automatically
     * returns false.
     */
    public boolean hasRoomForOutput()
    {
        if (inventory[24]==null)
        {
            return false;
        }

        if (inventory[25]==null)
        {
            //check every location
            for (int i=12;i<24;i++)
            {
                if (inventory[i]!=null && inventory[i].stackSize==getInventoryStackLimit())
                {
                    return false;
                }
            }
            return true;
        }
        //otherwise just check the target's output location
        int targetIndex = 12+inventory[25].getItemDamage();
        return !(inventory[targetIndex]!=null && inventory[targetIndex].stackSize==getInventoryStackLimit());
//        if (inventory[targetIndex]!=null && inventory[targetIndex].stackSize==getInventoryStackLimit())
//        {
//            return false;
//        }
//        return true;
    }

    /**
     * converts a gem in the working area to another gem and leaves it there
     */
    public void randomlyConvert()
    {
        if (inventory[24]==null)
        {
            return;
        }

        if (!blacklisted[inventory[24].getItemDamage()]) {
            blacklisted[inventory[24].getItemDamage()] = true;
            numBlacklisted += 1;
        }
        if (numBlacklisted>=blacklisted.length)
        {
            LogHelper.error("invalid blacklisting has happened! (Gemulation - TileGemChanger)");
            resetBlacklist();
            blacklisted[inventory[24].getItemDamage()] = true;
//            throw new Error();
        }

        int newType = worldObj.rand.nextInt(12-numBlacklisted);
        int i=0;
        while (i<=newType)
        {
            if (blacklisted[i])
            {
                newType++;
            }
            i++;
        }

//        int blacklist = inventory[24].getItemDamage();
//        int newType = worldObj.rand.nextInt(11);
//        if (newType>=blacklist)
//        {
//            newType++;
//        }
        setInventorySlotContents(24,new ItemStack(ModItems.gem,1,newType));
    }

    public void increaseTimer()
    {
        timer = (timer+1)%TIMER_DELAY;
    }

    public boolean hasWork()
    {
        return workTime>0;
    }

    public boolean hasEnoughEnergy()
    {
        return energyStored >= ENERGY_NEEDED_TO_CHANGE;
    }

    @Override
    public int receiveEnergy(int amount) {
        int amountRecieved;
        if (energyStored + amount > getMaxEnergyStored())
        {
            amountRecieved = getMaxEnergyStored() - energyStored;
            energyStored = getMaxEnergyStored();
        }
        else
        {
            amountRecieved = amount;
            energyStored += amount;
        }

        this.worldObj.markBlockForUpdate(pos);

        return amountRecieved;
    }

    @Override
    public boolean canReceiveEnergy() {
        return energyStored < getMaxEnergyStored();
    }

    @Override
    public int getEnergyStored() {
        return energyStored;
    }

    @Override
    public int getMaxEnergyStored() {
        return MAX_ENERGY;
    }

    public boolean needsToStart()
    {
        return workTime == WORK_TIME;
    }

    public void checkIfTargetChanged()
    {
        boolean needBlacklistUpdate = false;
        if (target==null && inventory[25]!=null)
        {
            needBlacklistUpdate = true;
            target = inventory[25].copy();
        }
        else if (target!=null)
        {
            if (inventory[25]==null)
            {
                needBlacklistUpdate = true;
                target = null;
            }
            else
            {
                if (target.getItemDamage()!=inventory[25].getItemDamage())
                {
                    needBlacklistUpdate = true;
                    target = inventory[25].copy();
                }
            }
        }

        if (needBlacklistUpdate)
        {
            resetBlacklist();
        }
    }
}
