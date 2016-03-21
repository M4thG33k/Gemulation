package com.m4thg33k.gemulation.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.item.ModItems;

public class SlotGem extends SlotVariableInput{

    private int gemData;

    public SlotGem(IInventory inv, int index, int xpos, int ypos, boolean allowInsertion,boolean allowRemoval, int gemData)
    {
        super(inv, index, xpos, ypos, allowInsertion,allowRemoval);
        this.gemData = gemData;
    }

    //only allow gems with the corresponding data to be placed in this slot


    @Override
    public boolean isItemValid(ItemStack stack) {
        return (stack.getItem() == ModItems.gem && stack.getItemDamage()==gemData);
    }
}
