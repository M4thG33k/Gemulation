package com.m4thg33k.gemulation.inventory;

import com.m4thg33k.gemulation.core.util.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

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

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        super.onPickupFromSlot(playerIn, stack);
        playerIn.addExperience((int)FurnaceRecipes.instance().getSmeltingExperience(stack)*stack.stackSize);
        LogHelper.info(FurnaceRecipes.instance().getSmeltingExperience(new ItemStack(Items.iron_ingot,64)));
    }
}
