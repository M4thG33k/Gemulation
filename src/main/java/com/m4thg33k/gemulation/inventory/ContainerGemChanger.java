package com.m4thg33k.gemulation.inventory;

import com.m4thg33k.gemulation.tiles.TileGemChanger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerGemChanger extends Container {

    private TileGemChanger te;

    public ContainerGemChanger(InventoryPlayer playerInventory, TileGemChanger tileGemChanger)
    {
        this.te = tileGemChanger;

        //te inventory
        //input/output; slots 0-23
        for (int x=0;x<2;x++)
        {
            for (int y=0;y<6;y++)
            {
                this.addSlotToContainer(new Slot(te,y*2+x,8+18*x,8+18*y));
                this.addSlotToContainer(new NoInputSlot(te,y*2+x+12,214+18*x,8+18*y));
            }
        }

        //working slot 24
        this.addSlotToContainer(new NoInputSlot(te,24,120,44));

        //target slot 25
        this.addSlotToContainer(new Slot(te,25,120,26));

        //player inventory (slots 26-54; ids 9-35)
        for (int y=0;y<3;y++)
        {
            for (int x=0;x<9;x++)
            {
                addSlotToContainer(new Slot(playerInventory,x+y*9+9,48+x*18,84+y*18));
            }
        }

        //player hotbar (slots 55-64; ids 0-8)
        for (int x=0;x<9;x++)
        {
            addSlotToContainer(new Slot(playerInventory,x,48+x*18,142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.isUseableByPlayer(playerIn);
    }


}
