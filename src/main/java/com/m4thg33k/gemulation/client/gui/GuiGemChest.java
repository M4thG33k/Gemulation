package com.m4thg33k.gemulation.client.gui;

import com.m4thg33k.gemulation.inventory.ContainerGemChest;
import com.m4thg33k.gemulation.lib.GemChestType;
import com.m4thg33k.gemulation.tiles.TileGemChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiGemChest extends GuiContainer{

    public enum ResourceList {
        BASIC(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png"));
//        RUBY(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png")),
//        GARNET(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png")),
//        TOPAZ(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png")),
//        HELIODOR(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png")),
//        PERIDOT(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png")),
//        EMERALD(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png")),
//        AQUAMARINE(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png")),
//        SAPPHIRE(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png")),
//        IOLITE(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png")),
//        AMETHYST(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png")),
//        MORGANITE(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png")),
//        ONYX(new ResourceLocation("gemulation","textures/gui/BasicGemChest.png"));
        public final ResourceLocation location;
        private ResourceList(ResourceLocation loc)
        {
            this.location = loc;
        }
    }

    public enum GUI{
        RUBY(184,202,ResourceList.BASIC, GemChestType.RUBY),
        GARNET(184,202,ResourceList.BASIC, GemChestType.GARNET),
        TOPAZ(184,202,ResourceList.BASIC, GemChestType.TOPAZ),
        HELIODOR(184,202,ResourceList.BASIC, GemChestType.HELIODOR),
        PERIDOT(184,202,ResourceList.BASIC, GemChestType.PERIDOT),
        EMERALD(184,202,ResourceList.BASIC, GemChestType.EMERALD),
        AQUAMARINE(184,202,ResourceList.BASIC, GemChestType.AQUAMARINE),
        SAPPHIRE(184,202,ResourceList.BASIC, GemChestType.SAPPHIRE),
        IOLITE(184,202,ResourceList.BASIC, GemChestType.IOLITE),
        AMETHYST(184,202,ResourceList.BASIC, GemChestType.AMETHYST),
        MORGANITE(184,202,ResourceList.BASIC, GemChestType.MORGANITE),
        ONYX(184,202,ResourceList.BASIC, GemChestType.ONYX);
        private int xSize;
        private int ySize;
        private ResourceList guiResourceList;
        private GemChestType mainType;
        GUI(int xSize,int ySize, ResourceList guiResourceList, GemChestType mainType)
        {
            this.xSize = xSize;
            this.ySize = ySize;
            this.guiResourceList = guiResourceList;
            this.mainType = mainType;
        }

        protected Container makeContainer(IInventory player, IInventory chest)
        {
            return new ContainerGemChest(player,chest,mainType,xSize,ySize);
        }

        public static GuiGemChest buildGUI(GemChestType type, IInventory playerInventory, TileGemChest chestInventory)
        {
            return new GuiGemChest(values()[chestInventory.getType().ordinal()],playerInventory,chestInventory);
        }
    }

    public int getRowLength()
    {
        return type.mainType.getRowLength();
    }

    private GUI type;

    private GuiGemChest(GUI type, IInventory player, IInventory chest)
    {
        super(type.makeContainer(player,chest));
        this.type = type;
        this.xSize = type.xSize;
        this.ySize = type.ySize;
        this.allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
        this.mc.getTextureManager().bindTexture(type.guiResourceList.location);
        int x = (width-xSize)/2;
        int y = (height-ySize)/2;
        drawTexturedModalRect(x,y,0,0,xSize,ySize);
    }
}
