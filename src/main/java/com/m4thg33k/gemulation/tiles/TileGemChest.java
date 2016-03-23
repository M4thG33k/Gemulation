package com.m4thg33k.gemulation.tiles;

import com.m4thg33k.gemulation.block.ModBlocks;
import com.m4thg33k.gemulation.inventory.ContainerGemChest;
import com.m4thg33k.gemulation.lib.GemChestType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import java.util.List;

//Also stolen from cpw's Iron Chest mod

public class TileGemChest extends TileEntityLockable implements ITickable, IInventory{
    private int ticksSinceSync = -1;
    public float prevLidAngle;
    public float lidAngle;
    private int numUsingPlayers;
    private GemChestType type;
    public ItemStack[] chestContents;
//    private ItemStack[] topStacks;
    private byte facing;
    private boolean inventoryTouched;
    private boolean hadStuff;
    private String customName;

    public TileGemChest()
    {
        this(GemChestType.RUBY);
    }

    public TileGemChest(GemChestType type)
    {
        super();
        this.type = type;
        this.chestContents = new ItemStack[getSizeInventory()];
//        this.topStacks = new ItemStack[8];
    }

    public ItemStack[] getContents()
    {
        return chestContents;
    }

    public void setContents(ItemStack[] contents)
    {
        chestContents = new ItemStack[getSizeInventory()];
        for (int i=0;i<contents.length;i++)
        {
            if (i<chestContents.length)
            {
                chestContents[i] = contents[i];
            }
        }
        inventoryTouched = true;
    }

    @Override
    public int getSizeInventory() {
        return type.size;
    }

    public int getFacing()
    {
        return this.facing;
    }

    public GemChestType getType()
    {
        return this.type;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        inventoryTouched = true;
        return chestContents[index];
    }

    @Override
    public void markDirty() {
        super.markDirty();
        sortTopStacks();
    }

    protected void sortTopStacks()
    {}

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (chestContents[index] != null)
        {
            if (chestContents[index].stackSize <= count)
            {
                ItemStack itemStack = chestContents[index];
                chestContents[index] = null;
                markDirty();
                return itemStack;
            }
            ItemStack itemStack =chestContents[index].splitStack(count);
            if (chestContents[index].stackSize==0)
            {
                chestContents[index] = null;
            }
            markDirty();
            return itemStack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        chestContents[index] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : type.getName();
    }

    @Override
    public boolean hasCustomName() {
        return this.customName!=null && !this.customName.equals("");
    }

    public void setCustomName(String name)
    {
        this.customName = name;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList list = compound.getTagList("Items",10);
        this.chestContents = new ItemStack[getSizeInventory()];

        if (compound.hasKey("CustomName"))
        {
            this.customName = compound.getString("CustomName");
        }

        for (int i=0;i<list.tagCount();i++)
        {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 0xff;
            if (slot >= 0 && slot<chestContents.length)
            {
                chestContents[slot] = ItemStack.loadItemStackFromNBT(stackTag);
            }
        }
        facing = compound.getByte("Facing");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList list = new NBTTagList();
        for (int i=0;i<chestContents.length;i++)
        {
            if (chestContents[i]!=null)
            {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot",(byte)i);
                chestContents[i].writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        compound.setTag("Items",list);
        compound.setByte("Facing",facing);

        if (this.hasCustomName())
        {
            compound.setString("CustomName",customName);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (worldObj==null)
        {
            return true;
        }
        if (worldObj.getTileEntity(pos) != this)
        {
            return false;
        }
        return player.getDistanceSq(pos.add(0.5,0.5,0.5))<=64;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        switch (id)
        {
            case 1:
                numUsingPlayers = type;
                break;
            case 2:
                facing = (byte)type;
                break;
            case 3:
                facing = (byte)(type & 0x7);
                numUsingPlayers = (type & 0xF8)>>3;
                break;
            default:
        }
        return true;
    }

    @Override
    public void update() {
        //resync clients with the server state
        if (worldObj!=null && !this.worldObj.isRemote && this.numUsingPlayers!=0 && (this.ticksSinceSync + pos.getX() + pos.getY() + pos.getZ())%200==0) {
            this.numUsingPlayers = 0;
            float var1 = 5.0f;
            List<EntityPlayer> var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.getX() - var1, pos.getY() - var1, pos.getZ() - var1, pos.getX() + 1 + var1, pos.getY() + 1 + var1, pos.getZ() + 1 + var1));

            for (EntityPlayer var4 : var2) {
                if (var4.openContainer instanceof ContainerGemChest) {
                    ++this.numUsingPlayers;
                }
            }
        }

        if (worldObj != null && !worldObj.isRemote && ticksSinceSync<0)
        {
            worldObj.addBlockEvent(pos, ModBlocks.gemChestBlock,3,((numUsingPlayers << 3) & 0xF8 | (facing & 0x7)));
        }
        if (!worldObj.isRemote && inventoryTouched)
        {
            inventoryTouched = false;
        }

        this.ticksSinceSync++;
        prevLidAngle = lidAngle;
        float f = 0.1f;
        if (numUsingPlayers>0 && lidAngle==0f)
        {
            double d = pos.getX() + 0.5;
            double d1 = pos.getZ() + 0.5;
            worldObj.playSoundEffect(d,pos.getY()+0.5,d1,"random.chestopen",0.5f,worldObj.rand.nextFloat()*0.1f+0.9f);
        }
        if (numUsingPlayers == 0 && lidAngle > 0.0f || numUsingPlayers > 0 && lidAngle < 1.0f)
        {
            float f1 = lidAngle;
            if (numUsingPlayers>0)
            {
                lidAngle += f;
            }
            else
            {
                lidAngle -= f;
            }
            if (lidAngle>1.0f)
            {
                lidAngle = 1.0f;
            }
            float f2 = 0.5f;
            if (lidAngle < f2 && f1 >= f2)
            {
                double d2 = pos.getX() + 0.5;
                double d3 = pos.getZ() + 0.5;
                worldObj.playSoundEffect(d2,pos.getY()+0.5,d3,"random.chestclosed",0.5f, worldObj.rand.nextFloat()*0.1f+0.9f);
            }
            if (lidAngle<0.0f)
            {
                lidAngle = 0.0f;
            }
        }

    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (worldObj == null)
        {
            return;
        }
        numUsingPlayers++;
        worldObj.addBlockEvent(pos,ModBlocks.gemChestBlock,1,numUsingPlayers);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (worldObj == null)
        {
            return;
        }
        numUsingPlayers--;
        worldObj.addBlockEvent(pos,ModBlocks.gemChestBlock,1,numUsingPlayers);
    }

    public void setFacing(byte facing)
    {
        this.facing = facing;
    }

    public TileGemChest updateFromMetadata(int l)
    {
        if (worldObj != null && worldObj.isRemote)
        {
            if (l != type.ordinal())
            {
                worldObj.setTileEntity(pos,GemChestType.makeEntity(l));
                return (TileGemChest)worldObj.getTileEntity(pos);
            }
        }
        return this;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("Type",getType().ordinal());
        nbt.setByte("Facing",facing);

        return new S35PacketUpdateTileEntity(pos,0,nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        if (pkt.getTileEntityType() == 0)
        {
            NBTTagCompound compound = pkt.getNbtCompound();
            type = GemChestType.values()[compound.getInteger("Type")];
            facing = compound.getByte("Facing");
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.chestContents[index]!=null)
        {
            ItemStack stack = this.chestContents[index];
            this.chestContents[index] = null;
            return stack;
        }
        return null;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return type.acceptsStack(stack);
    }

    public void rotateAround()
    {
        facing++;
        if (facing > EnumFacing.EAST.ordinal())
        {
            facing = (byte)EnumFacing.NORTH.ordinal();
        }
        setFacing(facing);
        worldObj.addBlockEvent(pos,ModBlocks.gemChestBlock,2,facing);
    }

    public void wasPlaced(EntityLivingBase entityLivingBase, ItemStack itemStack)
    {

    }

    public void removeAdornments()
    {

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
        for (int i=0;i<this.chestContents.length;i++)
        {
            this.chestContents[i] = null;
        }
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return null;
    }

    @Override
    public String getGuiID() {
        return "GemChest:" + type.name(); //// TODO: 3/22/2016 make sure guis work
    }

    @Override
    public boolean canRenderBreaking() {
        return true;
    }


}
