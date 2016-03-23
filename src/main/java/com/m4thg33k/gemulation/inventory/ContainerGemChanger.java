package com.m4thg33k.gemulation.inventory;

import com.m4thg33k.gemulation.tiles.TileGemChanger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerGemChanger extends Container {

    private TileGemChanger te;

    public int workValue = 0;
    public int storedEnergy = 0;

    public ContainerGemChanger(InventoryPlayer playerInventory, TileGemChanger tileGemChanger)
    {
        this.te = tileGemChanger;

        //te inventory
        //input/output; slots 0-23
        for (int x=0;x<2;x++)
        {
            for (int y=0;y<6;y++)
            {
                //input slots
                this.addSlotToContainer(new SlotGem(te,y*2+x,8+18*x,8+18*y,true,true,x+2*y));
            }
        }
        for (int x=0;x<2;x++)
        {
            for (int y=0;y<6;y++)
            {
                //output slots
                this.addSlotToContainer(new SlotGem(te,y*2+x+12,214+18*x,8+18*y,false,true,x+2*y));
            }
        }

        //working slot 24
        this.addSlotToContainer(new SlotVariableInput(te,24,120,44,false,false));

        //target slot 25
        this.addSlotToContainer(new SlotGemChangerTarget(te,25,120,26));

        //player inventory (slots 26-52; ids 9-35)
        for (int y=0;y<3;y++)
        {
            for (int x=0;x<9;x++)
            {
                addSlotToContainer(new Slot(playerInventory,x+y*9+9,48+x*18,84+y*18));
            }
        }

        //player hotbar (slots 53-61; ids 0-8)
        for (int x=0;x<9;x++)
        {
            addSlotToContainer(new Slot(playerInventory,x,48+x*18,142));
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

            //insert custom behaviour
            if (index<24 || index == 25)
            {
                //moving from te input/output and target slots
                if(!mergeItemStack(current,26,62,true))
                {
                    return null;
                }

            }
            else if (index>25)
            {
                //moving from player inventory
                if (!mergeItemStack(current,0,12,false))
                {
                    return null;
                }
            }
            //end custom behaviour

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

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i=0;i<this.crafters.size();i++)
        {
            ICrafting iCrafting = this.crafters.get(i);

            if (this.workValue != this.te.workTime)
            {
                iCrafting.sendProgressBarUpdate(this,0,this.te.workTime);
            }

            if (this.storedEnergy != this.te.getEnergyStored())
            {
                iCrafting.sendProgressBarUpdate(this,1,this.te.getEnergyStored()%1000);
                iCrafting.sendProgressBarUpdate(this,2,this.te.getEnergyStored()/1000);
            }
        }

        this.workValue = this.te.workTime;
        this.storedEnergy = this.te.getEnergyStored();
    }

    @Override
    public void onCraftGuiOpened(ICrafting iCrafting) {
        super.onCraftGuiOpened(iCrafting);

        iCrafting.sendProgressBarUpdate(this,0,workValue);
        iCrafting.sendProgressBarUpdate(this,1,this.te.getEnergyStored()%1000);
        iCrafting.sendProgressBarUpdate(this,2,this.te.getEnergyStored()/1000);
    }

    @Override
    public void updateProgressBar(int id, int data) {
        switch (id)
        {
            case 0:
                workValue = data;
                break;
            case 1:
                storedEnergy = data;
                break;
            case 2:
                storedEnergy += data*1000;
                break;
            default:
        }
    }
}
