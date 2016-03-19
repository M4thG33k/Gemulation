package com.m4thg33k.gemulation.client.gui;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.inventory.ContainerGemFurnace;
import com.m4thg33k.gemulation.lib.Names;
import com.m4thg33k.gemulation.tiles.TileGemFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiGemFurnace extends GuiContainer {

    private TileGemFurnace tileGemFurnace;
    private InventoryPlayer playerInventory;

    public GuiGemFurnace(InventoryPlayer playerInventory, TileGemFurnace te)
    {
        super(new ContainerGemFurnace(playerInventory,te));
        tileGemFurnace = te;
        this.xSize = 176;
        this.ySize = 166;
        this.playerInventory = playerInventory;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int w = (width-xSize)/2;
        int h = (height-ySize)/2;

        String name = tileGemFurnace.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(name,xSize/2-this.fontRendererObj.getStringWidth(name)/2,6,0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(),8,ySize-96+2,0x404040);

        int mX = mouseX - w;
        int mY = mouseY - h;

        if (mX<43 || mX >= 50 || mY<15 || mY >= 69)
        {
            return;
        }
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);

        int stored = ((ContainerGemFurnace)this.inventorySlots).storedFuel;
        int max = ((ContainerGemFurnace)this.inventorySlots).maxFuel;

//        this.fontRendererObj.drawString(stored + "," + max,mX+10,mY+10,0x404040);
        double perc = Math.floor(((double)stored/((double)max))*10000)/100;

        String smelt = "Able to smelt %d items.";
        String percent = "Fuel Level At: " + perc + "%.";
        smelt = String.format(smelt,((ContainerGemFurnace)this.inventorySlots).getNumSmeltable);

        int textWidth = this.fontRendererObj.getStringWidth(smelt);

        mc.getTextureManager().bindTexture(new ResourceLocation(Gemulation.MODID+":textures/gui/"+ Names.GEM_FURNACE + ".png"));
        this.drawTexturedModalRect(mX-textWidth-2,mY-12,0,180,textWidth+4,18);

        this.fontRendererObj.drawString(percent,mX-textWidth,mY-10,0x404040);
        this.fontRendererObj.drawString(smelt,mX-textWidth,mY-2,0x404040);
//        this.fontRendererObj.drawString("Able to smelt " + ((ContainerGemFurnace)this.inventorySlots).getNumSmeltable + " items.",mouseX-w,mouseY-h,0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
        mc.getTextureManager().bindTexture(new ResourceLocation(Gemulation.MODID+":textures/gui/"+ Names.GEM_FURNACE + ".png"));

        int k = (width - xSize)/2;
        int l = (height - ySize)/2;

//        zLevel = 0;
        drawTexturedModalRect(k,l,0,0,xSize,ySize);
        int L;
//        if (TileGemFurnace.isBurning(tileGemFurnace))
//        {
            L = getBurnLeftScaled(13);
            drawTexturedModalRect(k+53,l+48-L,176,13-L,14,L+1);
//        }

//        zLevel = 1;
        L = getCookProgressScaled(24);
        this.drawTexturedModalRect(k+76,l+34,176,14,L+1,16);

//        zLevel = 2; //7x54
        L = getStoredFuelScaled(54);
        this.drawTexturedModalRect(k+43,l+68-L,176,84-L,7,L);

    }

    private int getStoredFuelScaled(int pixels)
    {
        int i = ((ContainerGemFurnace)this.inventorySlots).storedFuel;
        int j = ((ContainerGemFurnace)this.inventorySlots).maxFuel;
//        LogHelper.info("i,j: " + i + "," + j);
        return (j!=0 && i!=0) ? i*pixels/j : 0;
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
