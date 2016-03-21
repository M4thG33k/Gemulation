package com.m4thg33k.gemulation.inventory;

import com.m4thg33k.gemulation.tiles.TileGemChanger;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

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
}
