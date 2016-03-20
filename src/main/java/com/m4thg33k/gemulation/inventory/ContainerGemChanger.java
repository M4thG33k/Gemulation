package com.m4thg33k.gemulation.inventory;

import com.m4thg33k.gemulation.tiles.TileGemChanger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerGemChanger extends Container {

    private TileGemChanger te;

    public ContainerGemChanger(InventoryPlayer playerInventory, TileGemChanger tileGemChanger)
    {
        this.te = tileGemChanger;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}
