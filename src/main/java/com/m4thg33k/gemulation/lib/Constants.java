package com.m4thg33k.gemulation.lib;

import net.minecraft.item.Item;

public class Constants {

    //Furnace Upgrades
    public static final double SPEED_MULT = 0.25;
    public static final double FUEL_MULT = 1.25;
    public static final double CAP_MULT = 1.25;



    //material functions
    public static double baseCookTime(Item.ToolMaterial material)
    {
        return -(1.0/8.0)*material.getEfficiencyOnProperMaterial()+2.0;
    }
    public static double baseFuelBoost(Item.ToolMaterial material)
    {
        return ((1.0/8.0)*material.getDamageVsEntity()+3.0/4.0);
    }
    public static int upgradeCount(Item.ToolMaterial material)
    {
        return (material.getEnchantability()-8)/2;
    }
    public static int baseMaxFuel(Item.ToolMaterial material,double factor)
    {
        return (int)Math.ceil((400*(material.getMaxUses())-102400)*factor);
    }
}
