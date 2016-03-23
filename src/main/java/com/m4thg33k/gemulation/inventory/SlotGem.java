package com.m4thg33k.gemulation.inventory;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.item.ModItems;

public class SlotGem extends SlotVariableInput{

    private int gemData;

    public SlotGem(IInventory inv, int index, int xpos, int ypos, boolean allowInsertion,boolean allowRemoval, int gemData)
    {
        super(inv, index, xpos, ypos, allowInsertion,allowRemoval);
        this.gemData = gemData;

        this.setBackgroundLocation(new ResourceLocation("silentgems:textures/items/Gem"+gemData));
    }

    //only allow gems with the corresponding data to be placed in this slot


    @Override
    public boolean isItemValid(ItemStack stack) {
        return this.allowInsertion && (stack.getItem() == ModItems.gem && stack.getItemDamage()==gemData);
    }

    @Override
    public ResourceLocation getBackgroundLocation() {
        return new ResourceLocation("silentgems:/textures/items/Gem"+gemData);
    }
}
