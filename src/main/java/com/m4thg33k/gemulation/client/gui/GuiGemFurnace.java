package com.m4thg33k.gemulation.client.gui;

import com.m4thg33k.gemulation.inventory.ContainerGemFurnace;
import com.m4thg33k.gemulation.tiles.TileGemFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class GuiGemFurnace extends GuiContainer {

    public GuiGemFurnace(IInventory playerInventory, TileGemFurnace te)
    {
        super(new ContainerGemFurnace(playerInventory,te));

        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}
