package com.m4thg33k.gemulation.items;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.core.util.ChatHelper;
import com.m4thg33k.gemulation.core.util.LogHelper;
import com.m4thg33k.gemulation.lib.Constants;
import com.m4thg33k.gemulation.lib.Names;
import com.m4thg33k.gemulation.tiles.TileGemFurnace;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;

public class ItemFurnaceUpgrade extends Item {

    public ItemFurnaceUpgrade()
    {
        super();

        this.setCreativeTab(Gemulation.tabGemulation);
        this.setUnlocalizedName(Names.FURNACE_UPGRADE);
        this.setHasSubtypes(true);

        this.setMaxStackSize(64);
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
                stack.stackSize--;
                if (stack.stackSize == 0)
                {
                    stack = null;
                }
                ChatHelper.sayMessage(worldIn,playerIn,"Upgrade installed!");
            }
            else
            {
                ChatHelper.sayMessage(worldIn,playerIn,"No room for upgrades!");
            }
        }
        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName()+"_"+stack.getItemDamage();
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (int i=0;i<3;i++)
        {
            subItems.add(new ItemStack(itemIn,1,i));
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> list, boolean advanced) {
        super.addInformation(stack, playerIn, list, advanced);

        switch (stack.getItemDamage())
        {
            case 0:
                list.add("Speeds up the furnace");
                list.add("by a factor of " + Constants.SPEED_MULT);
                break;
            case 1:
                list.add("Increases the fuel efficiency");
                list.add("of the furnace by a factor of " + Constants.FUEL_MULT);
                break;
            case 2:
                list.add("Increases the storage capacity");
                list.add("of the furnace by a factor of " + Constants.CAP_MULT);
                break;
            default:
        }
        list.add("----------------");
        list.add(EnumChatFormatting.ITALIC + "Sneak-use on a furnace to install");
    }
}
