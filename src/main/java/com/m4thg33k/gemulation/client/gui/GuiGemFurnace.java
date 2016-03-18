package com.m4thg33k.gemulation.client.gui;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.core.util.LogHelper;
import com.m4thg33k.gemulation.inventory.ContainerGemFurnace;
import com.m4thg33k.gemulation.lib.Names;
import com.m4thg33k.gemulation.tiles.TileGemFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiGemFurnace extends GuiContainer {

    private TileGemFurnace tileGemFurnace;

    public GuiGemFurnace(IInventory playerInventory, TileGemFurnace te)
    {
        super(new ContainerGemFurnace(playerInventory,te));
        tileGemFurnace = te;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
        mc.getTextureManager().bindTexture(new ResourceLocation(Gemulation.MODID+":textures/gui/"+ Names.GEM_FURNACE + ".png"));

        int k = (width - xSize)/2;
        int l = (height - ySize)/2;

        zLevel = 0;
        drawTexturedModalRect(k,l,0,0,xSize,ySize);
        int L;
        if (TileGemFurnace.isBurning(tileGemFurnace))
        {
            L = getBurnLeftScaled(13);
            drawTexturedModalRect(k+53,l+31+12-L,176,12-L,14,L+1);
        }

        zLevel = 1;
        L = getCookProgressScaled(24);
        this.drawTexturedModalRect(k+76,l+30,176,14,L+1,16);

    }

    private int getCookProgressScaled(int pixels)
    {
        int i =((ContainerGemFurnace)this.inventorySlots).cookTime;
        int j = ((ContainerGemFurnace)this.inventorySlots).totalItemCookTime;
        return j!=0 && i!=0 ? i*pixels/j:0;
    }

    private int getBurnLeftScaled(int pixels)
    {
        int i = ((ContainerGemFurnace)this.inventorySlots).totalBurnTime;
        int j = ((ContainerGemFurnace)this.inventorySlots).burnTime;
        return j!=0 && i!=0 ? j*pixels/i : 0;
    }
}
