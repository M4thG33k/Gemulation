package com.m4thg33k.gemulation.gui;

import com.m4thg33k.gemulation.client.gui.GuiGemChanger;
import com.m4thg33k.gemulation.client.gui.GuiGemChest;
import com.m4thg33k.gemulation.client.gui.GuiGemFurnace;
import com.m4thg33k.gemulation.inventory.ContainerGemChanger;
import com.m4thg33k.gemulation.inventory.ContainerGemChest;
import com.m4thg33k.gemulation.inventory.ContainerGemFurnace;
import com.m4thg33k.gemulation.lib.GemChestType;
import com.m4thg33k.gemulation.tiles.TileGemChanger;
import com.m4thg33k.gemulation.tiles.TileGemChest;
import com.m4thg33k.gemulation.tiles.TileGemFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GemulationGuiHandler implements IGuiHandler {

    public static final int GEM_FURNACE_GUI = 0;
    public static final int GEM_CHANGER_GUI = 1;
    public static final int[] GEM_CHEST_GUIS = new int[]{2,3,4,5,6,7,8,9,10,11,12,13};

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID)
        {
            case GEM_FURNACE_GUI:
                return new ContainerGemFurnace(player.inventory,(TileGemFurnace)world.getTileEntity(new BlockPos(x,y,z)));
            case GEM_CHANGER_GUI:
                return new ContainerGemChanger(player.inventory,(TileGemChanger)world.getTileEntity(new BlockPos(x,y,z)));
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                return new ContainerGemChest(player.inventory,(TileGemChest)world.getTileEntity(new BlockPos(x,y,z)), GemChestType.values()[ID-2],0,0);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID)
        {
            case GEM_FURNACE_GUI:
                return new GuiGemFurnace(player.inventory,(TileGemFurnace)world.getTileEntity(new BlockPos(x,y,z)));
            case GEM_CHANGER_GUI:
                return new GuiGemChanger(player.inventory,(TileGemChanger) world.getTileEntity(new BlockPos(x,y,z)));
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                return GuiGemChest.GUI.buildGUI(GemChestType.values()[ID-2],player.inventory,(TileGemChest)world.getTileEntity(new BlockPos(x,y,z)));
            default:
                return null;
        }
    }
}
