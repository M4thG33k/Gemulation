package com.m4thg33k.gemulation.gui;

import com.m4thg33k.gemulation.client.gui.GuiGemFurnace;
import com.m4thg33k.gemulation.inventory.ContainerGemFurnace;
import com.m4thg33k.gemulation.tiles.TileGemFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GemulationGuiHandler implements IGuiHandler {

    public static final int GEM_FURNACE_GUI = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GEM_FURNACE_GUI)
        {
            return new ContainerGemFurnace(player.inventory,(TileGemFurnace)world.getTileEntity(new BlockPos(x,y,z)));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GEM_FURNACE_GUI)
        {
            return new GuiGemFurnace(player.inventory,(TileGemFurnace)world.getTileEntity(new BlockPos(x,y,z)));
        }
        return null;
    }
}
