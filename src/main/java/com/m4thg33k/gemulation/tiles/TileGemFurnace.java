package com.m4thg33k.gemulation.tiles;

import com.m4thg33k.gemulation.block.GemFurnaceBlock;
import com.m4thg33k.gemulation.core.util.LogHelper;
import com.m4thg33k.gemulation.lib.Names;
import com.m4thg33k.gemulation.network.packets.GemulationPackets;
import com.m4thg33k.gemulation.network.packets.PacketNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.material.ModMaterials;

public class TileGemFurnace extends TileEntity implements IInventory, ITickable{

    //slot 0 is for fuel, 1 for input, 2 for output (more to come)
    private ItemStack[] inventory;
    private String customName;
    private int meta;
    private EnumFacing facing;
    private boolean isOn;

    public int burnTime;
    public int currentItemBurnTime;
    public int cookTime;
    public int totalCookTime;

    private double cookTimeFactor = 1.0;
    private int upgradeCount = 0;
    private double fuelBooster = 1.0;


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

        Item.ToolMaterial material = EnumGem.get(meta).getToolMaterial(false);
        cookTimeFactor = -(1.0/8.0)*material.getEfficiencyOnProperMaterial()+2.0;
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

        burnTime = compound.getInteger("burnTime");
        currentItemBurnTime = compound.getInteger("currentItemBurnTime");
        cookTime = compound.getInteger("cookTime");
        totalCookTime = compound.getInteger("totalCookTime");

        cookTimeFactor = compound.getDouble("cookTimeFactor");
        upgradeCount = compound.getInteger("upgradeCount");
        fuelBooster = compound.getDouble("fuelBooster");
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

        compound.setInteger("burnTime",burnTime);
        compound.setInteger("currentItemBurnTime",currentItemBurnTime);
        compound.setInteger("cookTime",cookTime);
        compound.setInteger("totalCookTime",totalCookTime);

        compound.setDouble("cookTimeFactor",cookTimeFactor);
        compound.setInteger("upgradeCount",upgradeCount);
        compound.setDouble("fuelBooster",fuelBooster);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
//        tagCompound.setBoolean("on",isOn);
//        tagCompound.setInteger("facing",facing.ordinal());
        return new S35PacketUpdateTileEntity(pos,1,tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tagCompound = pkt.getNbtCompound();
//        isOn = tagCompound.getBoolean("on");
//        facing = EnumFacing.VALUES[tagCompound.getInteger("facing")];
        readFromNBT(tagCompound);
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
        return 3;
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
        boolean flag = stack!=null && stack.isItemEqual(inventory[index]) && ItemStack.areItemStackTagsEqual(stack,inventory[index]);
        inventory[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }

        if (index==1 && !flag)
        {
            totalCookTime = getCookTime(stack);
            cookTime = 0;
            markDirty();
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
        switch (index)
        {
            case 0: //fuel
                return isItemFuel(stack);
            case 1: //has a furnace recipe
                return hasFurnaceRecipe(stack);
            default:
                return false;
        }
    }

    @Override
    public int getField(int id) {
        switch (id)
        {
            case 0:
                return burnTime;
            case 1:
                return currentItemBurnTime;
            case 2:
                LogHelper.info("Cook time: " + cookTime);
                return cookTime;
            case 3:
                return totalCookTime;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id)
        {
            case 0:
                burnTime = value;
                break;
            case 1:
                currentItemBurnTime = value;
                break;
            case 2:
                cookTime = value;
                break;
            case 3:
                totalCookTime = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
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

    public boolean isBurning()
    {
        return burnTime>0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inv)
    {
        return inv.getField(0)>0;
    }

    @Override
    public void update() {
        boolean on = isBurning();
        boolean isDirty = false;

        if (isBurning())
        {
            --burnTime;
        }



        if (!worldObj.isRemote)
        {
            if (isBurning() || inventory[0] != null && inventory[1] !=null)
            {
                if (!isBurning() && canSmelt())
                {
                    currentItemBurnTime = burnTime = getItemBurnTime(inventory[0]);

                    if (isBurning())
                    {
                        isDirty = true;

                        if (inventory[0] != null)
                        {
                            inventory[0].stackSize--;
                            if (inventory[0].stackSize==0)
                            {
                                inventory[0] = inventory[0].getItem().getContainerItem(inventory[0]);
                            }
                        }
                    }
                }

                if (isBurning() && canSmelt())
                {
                    cookTime++;

                    if (cookTime == totalCookTime)
                    {
                        cookTime = 0;
                        totalCookTime = getCookTime(inventory[1]);
                        smeltItem();
                        isDirty = true;
                    }
                }
                else
                {
                    cookTime = 0;
                }
            }
            else if (!isBurning() && cookTime>0)
            {
                cookTime = MathHelper.clamp_int(cookTime-2,0,totalCookTime);
            }

            if (on != isBurning())
            {
                isDirty = true;
                isOn = isBurning();
//                NBTTagCompound tag = new NBTTagCompound();
//                writeToNBT(tag);
//                GemulationPackets.INSTANCE.sendToAllAround(new PacketNBT(pos,tag),new NetworkRegistry.TargetPoint(worldObj.provider.getDimensionId(),pos.getX(),pos.getY(),pos.getZ(),32));
            }
        }

        if (isDirty)
        {
            markDirty();
            NBTTagCompound tag = new NBTTagCompound();
            writeToNBT(tag);
            GemulationPackets.INSTANCE.sendToAllAround(new PacketNBT(pos,tag),new NetworkRegistry.TargetPoint(worldObj.provider.getDimensionId(),pos.getX(),pos.getY(),pos.getZ(),32));
        }
    }

    public int getCookTime(ItemStack stack)
    {
        return (int)(Math.floor(200*cookTimeFactor));
    }

    /**
     * Returns the number of ticks that the supplied fuel will keep the furnace burning (including fuelBoost) or 0 if
     * the item is not fuel
     */
    public int getItemBurnTime(ItemStack stack)
    {
        if (stack==null)
        {
            return 0;
        }

        return (int)Math.ceil(TileEntityFurnace.getItemBurnTime(stack)*fuelBooster);
    }

    public boolean isItemFuel(ItemStack stack)
    {
        return getItemBurnTime(stack)>0;
    }

    public boolean hasFurnaceRecipe(ItemStack stack)
    {
        return FurnaceRecipes.instance().getSmeltingResult(stack)!=null;
    }

    private boolean canSmelt()
    {
        if (inventory[1]==null || !hasFurnaceRecipe(inventory[1])) //anything in input that can be smelted?
        {
            return false;
        }
        if (inventory[2]==null)
        {
            return true;
        }
        ItemStack output = FurnaceRecipes.instance().getSmeltingResult(inventory[1]);
        if (!inventory[2].isItemEqual(output))
        {
            return false;
        }
        int result = inventory[2].stackSize+output.stackSize;
        return result<=getInventoryStackLimit() && result<=inventory[2].getMaxStackSize();
    }

    public void smeltItem()
    {
        if (canSmelt())
        {
            ItemStack stack = FurnaceRecipes.instance().getSmeltingResult(inventory[1]);

            if (inventory[2] == null)
            {
                inventory[2] = stack.copy();
            }
            else if (inventory[2].getItem() == stack.getItem())
            {
                inventory[2].stackSize += stack.stackSize;
            }

            if (inventory[1].getItem() == Item.getItemFromBlock(Blocks.sponge) && inventory[1].getMetadata()==1 && inventory[0] != null && inventory[0].getItem() == Items.bucket)
            {
                inventory[0] = new ItemStack(Items.water_bucket);
            }

            inventory[1].stackSize--;

            if (inventory[1].stackSize<=0)
            {
                inventory[1] = null;
            }
        }
    }


}
