package com.m4thg33k.gemulation.client.gui;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.inventory.ContainerGemChanger;
import com.m4thg33k.gemulation.lib.Names;
import com.m4thg33k.gemulation.tiles.TileGemChanger;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiGemChanger extends GuiContainer{

    private TileGemChanger tileGemChanger;
    private InventoryPlayer playerInventory;

    public GuiGemChanger(InventoryPlayer playerInventory, TileGemChanger te)
    {
        super(new ContainerGemChanger(playerInventory,te));

        this.tileGemChanger = te;
        this.playerInventory = playerInventory;

        this.xSize = 256;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
        int k = (width-xSize)/2;
        int l = (height-ySize)/2;

        mc.getTextureManager().bindTexture(new ResourceLocation(Gemulation.MODID+":textures/gui/"+ Names.GEM_CHANGER + ".png"));
        this.drawTexturedModalRect(k,l,0,0,xSize,ySize);
    }
}
