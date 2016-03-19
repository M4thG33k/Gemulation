package com.m4thg33k.gemulation.items;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.core.util.LogHelper;
import com.m4thg33k.gemulation.lib.Names;
import com.m4thg33k.gemulation.tiles.TileGemFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemFurnaceUpgrade extends Item {

    public ItemFurnaceUpgrade()
    {
        super();

        this.setCreativeTab(Gemulation.tabGemulation);
        this.setUnlocalizedName(Names.FURNACE_UPGRADE);

        this.setMaxStackSize(1);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity!=null && tileEntity instanceof TileGemFurnace)
        {
            if (((TileGemFurnace) tileEntity).canReceiveUpgrade())
            {
//                LogHelper.info("Ready to receive upgrade, sir!");
                ((TileGemFurnace) tileEntity).installUpgrade(stack);
            }
            else
            {
                LogHelper.info("Too full!");
            }
        }
        return true;
    }
}
