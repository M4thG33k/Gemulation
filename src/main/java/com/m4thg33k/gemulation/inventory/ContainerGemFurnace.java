package com.m4thg33k.gemulation.inventory;

import com.m4thg33k.gemulation.tiles.TileGemFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerGemFurnace extends Container{

    private TileGemFurnace te;
    public int burnTime;
    public int totalBurnTime;
    public int cookTime;
    public int totalItemCookTime;
    public int storedFuel;
    public int maxFuel;
    public int getNumSmeltable;
    public ItemStack outputStack;

    public ContainerGemFurnace(InventoryPlayer playerInventory, TileGemFurnace tileGemFurnace)
    {
        te = tileGemFurnace;

        //te inventory
        this.addSlotToContainer(new Slot(te,0,53,52));
        this.addSlotToContainer(new Slot(te,1,53,16));
        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player,te,2,107,34));
//        for (int y=0;y<te.getSizeInventory();y++)
//        {
//            this.addSlotToContainer(new Slot(te,y,62,17+y*18));
//        }

        //player inventory slot 9-35 (ids 9-35)
        for (int y=0;y<3;y++)
        {
            for (int x=0;x<9;x++)
            {
                addSlotToContainer(new Slot(playerInventory, x+y*9+9,8+x*18,84+y*18));
            }
        }

        //player hotbar slot 0-8 (ids 36-44)
        for (int x=0;x<9;x++)
        {
            addSlotToContainer(new Slot(playerInventory,x,8+x*18,142));
        }
    }


    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.isUseableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack previous = null;
        Slot slot = inventorySlots.get(index);

        if (slot!=null && slot.getHasStack())
        {
            ItemStack current = slot.getStack();
            previous = current.copy();

            // [...] custom behaviour
            if (index < te.getSizeInventory())
            {
                //from the te
                if (!mergeItemStack(current,te.getSizeInventory(),te.getSizeInventory()+36,true))
                {
                    return null;
                }
            }
            else
            {
                //from player inventory
                boolean canBurn = TileEntityFurnace.getItemBurnTime(current)>0;
                boolean canSmelt = FurnaceRecipes.instance().getSmeltingResult(current)!=null;

                if (canBurn && !canSmelt) //if it can be burned but not smelted
                {
                    if (!mergeItemStack(current,0,1,false))
                    {
                        return null;
                    }
                }
                else if (!canBurn && canSmelt) //if it can be smelted but not burned
                {
                    if (!mergeItemStack(current,1,2,false))
                    {
                        return null;
                    }
                }
                else if (canBurn) //can burn and smelt, place first in input then burn
                {
                    if (!mergeItemStack(current,0,2,true)){
                        return null;
                    }
                }
                else //can neither burn nor smelt (so don't move it
                {
                    return null;
                }
//                if (!mergeItemStack(current,0,te.getSizeInventory(),false))
//                {
//                    return null;
//                }
            }

            if (current.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (current.stackSize == previous.stackSize)
            {
                return null;
            }
            slot.onPickupFromSlot(playerIn,current);
        }
        return previous;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i=0;i<this.crafters.size();i++)
        {
            ICrafting iCrafting = this.crafters.get(i);

            if ((this.te.cookTime != this.cookTime) || (ItemStack.areItemStacksEqual(outputStack,this.te.getStackInSlot(2))))
            {
                iCrafting.sendProgressBarUpdate(this,0,this.te.cookTime);
            }

            if (this.te.totalCookTime != this.totalItemCookTime)
            {
                iCrafting.sendProgressBarUpdate(this,1,this.te.totalCookTime);
            }

            if (this.te.burnTime != this.burnTime)
            {
                iCrafting.sendProgressBarUpdate(this,2,this.te.burnTime);
            }

            if (this.te.currentItemBurnTime != this.totalBurnTime)
            {
                iCrafting.sendProgressBarUpdate(this,3,this.te.currentItemBurnTime);
            }

            if (this.te.storedFuel != this.storedFuel)
            {
                iCrafting.sendProgressBarUpdate(this,4,this.te.storedFuel);
            }

            if (this.te.getMaxFuel() != this.maxFuel)
            {
                iCrafting.sendProgressBarUpdate(this,5,this.te.getMaxFuel());
            }

            if (this.te.getNumSmeltable() != getNumSmeltable)
            {
                iCrafting.sendProgressBarUpdate(this,6,this.te.getNumSmeltable());
            }
        }

        this.cookTime = te.cookTime;
        this.totalItemCookTime = te.totalCookTime;
        this.burnTime = te.burnTime;
        this.totalBurnTime = te.currentItemBurnTime;
        this.storedFuel = te.storedFuel;
        this.maxFuel = te.getMaxFuel();
        this.getNumSmeltable = te.getNumSmeltable();
        if (te.getStackInSlot(2)==null)
        {
            this.outputStack = null;
        }
        else{
            this.outputStack = te.getStackInSlot(2).copy();
        }
    }

    @Override
    public void onCraftGuiOpened(ICrafting iCrafting) {
        super.onCraftGuiOpened(iCrafting);

        iCrafting.sendProgressBarUpdate(this,0,cookTime);
        iCrafting.sendProgressBarUpdate(this,1,totalItemCookTime);
        iCrafting.sendProgressBarUpdate(this,2,burnTime);
        iCrafting.sendProgressBarUpdate(this,3,totalBurnTime);
        iCrafting.sendProgressBarUpdate(this,4,storedFuel);
        iCrafting.sendProgressBarUpdate(this,5,maxFuel);
        iCrafting.sendProgressBarUpdate(this,6,getNumSmeltable);
    }

    @Override
    public void updateProgressBar(int id, int data) {
        switch(id)
        {
            case 0:
                cookTime = data;
                break;
            case 1:
                totalItemCookTime = data;
                break;
            case 2:
                burnTime = data;
                break;
            case 3:
                totalBurnTime = data;
                break;
            case 4:
                storedFuel = data;
                break;
            case 5:
                maxFuel = data;
                break;
            case 6:
                getNumSmeltable = data;
                break;
            default:
        }
    }

}
