package com.m4thg33k.gemulation.inventory;

import com.m4thg33k.gemulation.lib.GemChestType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerGemChest extends Container {

    private GemChestType type;
    private EntityPlayer player;
    private IInventory chest;

    public ContainerGemChest(IInventory playerInventory, IInventory chestInventory, GemChestType type, int xSize, int ySize)
    {
        this.chest = chestInventory;
        this.player = ((InventoryPlayer)playerInventory).player;
        this.type = type;
        chestInventory.openInventory(player);
        layoutContainer(playerInventory,chestInventory,type,xSize,ySize);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return chest.isUseableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemStack = null;
        Slot slot = (Slot)inventorySlots.get(index);
        if (slot!=null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();
            if (index<type.size)
            {
                if (!mergeItemStack(stack,type.size,inventorySlots.size(),true))
                {
                    return null;
                }
            }
            else if (!type.acceptsStack(stack))
            {
                return null;
            }
            else if (!mergeItemStack(stack,0,type.size,false))
            {
                return null;
            }
            if (stack.stackSize==0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        return itemStack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        chest.closeInventory(playerIn);
    }

    protected void layoutContainer(IInventory playerInventory, IInventory chestInventory, GemChestType type, int xSize, int ySize)
    {
        for (int chestRow = 0;chestRow<type.getRowCount();chestRow++)
        {
            for (int chestCol = 0; chestCol < type.getRowLength();chestCol++)
            {
                addSlotToContainer(new Slot(chestInventory,chestCol + chestRow * type.getRowLength(),12+chestCol*18,8+chestRow*18));
            }
        }

        int leftCol = (xSize-162)/2+1;
        for (int playerInvRows=0;playerInvRows<3;playerInvRows++)
        {
            for (int playerInvCols=0;playerInvCols<9;playerInvCols++)
            {
                addSlotToContainer(new Slot(playerInventory,playerInvCols+playerInvRows*9+9,leftCol+playerInvCols*18,ySize-(4-playerInvRows)*18-10));
            }
        }

        for (int hotbarSlot=0;hotbarSlot<9;hotbarSlot++)
        {
            addSlotToContainer(new Slot(playerInventory,hotbarSlot,leftCol+hotbarSlot*18,ySize-24));
        }
    }

    public EntityPlayer getPlayer()
    {
        return player;
    }

    public int getNumColumns()
    {
        return type.getRowLength();
    }
}
