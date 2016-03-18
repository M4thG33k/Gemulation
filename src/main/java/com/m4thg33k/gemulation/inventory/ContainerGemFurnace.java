package com.m4thg33k.gemulation.inventory;

import com.m4thg33k.gemulation.tiles.TileGemFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ContainerGemFurnace extends Container {

    private TileGemFurnace te;

    public ContainerGemFurnace(IInventory playerInventory, TileGemFurnace tileGemFurnace)
    {
        te = tileGemFurnace;

        //te inventory
        this.addSlotToContainer(new Slot(te,0,62,17));
        this.addSlotToContainer(new Slot(te,1,80,17));
        this.addSlotToContainer(new NoInputSlot(te,2,98,17));
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
}
