package com.m4thg33k.gemulation.client.gui;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.client.render.helpers.StackRenderingHelper;
import com.m4thg33k.gemulation.inventory.ContainerGemChanger;
import com.m4thg33k.gemulation.lib.Names;
import com.m4thg33k.gemulation.tiles.TileGemChanger;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.item.ModItems;
import org.lwjgl.opengl.GL11;

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
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int w = (width-xSize)/2;
        int h = (height-ySize)/2;

        String name = tileGemChanger.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(name,xSize/2-this.fontRendererObj.getStringWidth(name)/2,6,0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(),47,ySize-96+2,0x404040);
    }



    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
        int k = (width-xSize)/2;
        int l = (height-ySize)/2;



        mc.getTextureManager().bindTexture(new ResourceLocation(Gemulation.MODID+":textures/gui/"+ Names.GEM_CHANGER + ".png"));
        this.drawTexturedModalRect(k,l,0,0,xSize,ySize);

        drawGemBackgrounds(k,l);
//        zLevel = 2;
//        mc.getTextureManager().bindTexture(new ResourceLocation(Gemulation.MODID+":textures/gui/"+ Names.GEM_CHANGER + "Foreground.png"));
//        this.drawTexturedModalRect(k,l,0,0,xSize,ySize);
    }

    private void drawGemBackgrounds(int k, int l)
    {
//        for (int x=0;x<2;x++)
//        {
//            for (int y=0;y<6;y++)
//            {
//                StackRenderingHelper.renderItemStack(itemRender.getItemModelMesher(),mc.renderEngine,k+8+18*x,l+8+18*y,new ItemStack(ModItems.gem,1,x+2*y),0xFFFFFF,true);
//            }
//        }

        for (int x=0;x<2;x++)
        {
            for (int y=0;y<6;y++)
            {
                itemRender.renderItemIntoGUI(new ItemStack(ModItems.gem,1,x+2*y),k+8+18*x,l+8+18*y);
                itemRender.renderItemIntoGUI(new ItemStack(ModItems.gem,1,x+2*y),k+214+18*x,l+8+18*y);
            }
        }
//        GlStateManager.disableBlend();
//        GlStateManager.disableAlpha();
    }
}
