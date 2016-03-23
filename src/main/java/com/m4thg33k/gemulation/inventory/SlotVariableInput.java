package com.m4thg33k.gemulation.inventory;

import com.m4thg33k.gemulation.core.util.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

//This type of slot allows no items put into it from a user (the te can still do it though)
public class SlotVariableInput extends Slot {

    protected boolean allowInsertion;
    protected boolean allowRemoval;

    public SlotVariableInput(IInventory inv, int index, int xpos, int ypos, boolean allowInsertion,boolean allowRemoval)
    {
        super(inv,index,xpos,ypos);
        this.allowInsertion = allowInsertion;
        this.allowRemoval = allowRemoval;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return allowInsertion;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        super.onPickupFromSlot(playerIn, stack);
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return allowRemoval;
    }
}
