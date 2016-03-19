package com.m4thg33k.gemulation.block.ItemBlocks;

import com.m4thg33k.gemulation.Gemulation;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.silentchaos512.gems.lib.EnumGem;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class GemFurnaceItemBlock extends ItemBlock {

    public GemFurnaceItemBlock(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(Gemulation.tabGemulation);
    }

    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "_" + stack.getItemDamage();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> list, boolean advanced) {

        boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        if (shifted)
        {
            ToolMaterial material = EnumGem.get(stack.getItemDamage()).getToolMaterial(false);

            list.add(EnumChatFormatting.ITALIC + "Properties");
            list.add("----------------");
            list.add(EnumChatFormatting.GOLD + "Time Reduction Multiplier: " + EnumChatFormatting.RESET + (-(1.0/8.0)*material.getEfficiencyOnProperMaterial()+2.0));
            list.add(EnumChatFormatting.GOLD + "Fuel Bonus Multiplier: " + EnumChatFormatting.RESET + ((1.0/8.0)*material.getDamageVsEntity()+3.0/4.0));
            list.add(EnumChatFormatting.GOLD + "Upgrade Slots: " + EnumChatFormatting.RESET + ((material.getEnchantability()-8)/2) + " (WIP)");
            list.add(EnumChatFormatting.GOLD + "Fuel Capacity: " + EnumChatFormatting.RESET + ((material.getMaxUses()/256.0-1)));
        }
        else
        {
            list.add(EnumChatFormatting.ITALIC + "<Press Shift>");
        }
    }
}
