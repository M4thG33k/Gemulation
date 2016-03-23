package com.m4thg33k.gemulation.inventory;

import com.m4thg33k.gemulation.tiles.TileGemChanger;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.item.ModItems;

public class SlotGemChangerTarget extends Slot {

    TileGemChanger tile;

    SlotGemChangerTarget(TileGemChanger tileGemChanger, int index, int xpos, int ypos)
    {
        super(tileGemChanger,index,xpos,ypos);
        tile = tileGemChanger;
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
//        tile.resetBlacklist();
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return (stack!=null && stack.getItem() == ModItems.gem && stack.getItemDamage()<12);
    }
}
