package com.m4thg33k.gemulation.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

//This type of slot allows no items put into it from a user (the te can still do it though)
public class NoInputSlot extends Slot {

    public NoInputSlot(IInventory inv, int index, int xpos, int ypos)
    {
        super(inv,index,xpos,ypos);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }
}
