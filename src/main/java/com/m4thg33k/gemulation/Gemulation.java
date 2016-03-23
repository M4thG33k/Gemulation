package com.m4thg33k.gemulation;

import com.m4thg33k.gemulation.block.ModBlocks;
import com.m4thg33k.gemulation.core.proxies.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Gemulation.MODID,name = Gemulation.MODNAME, version = Gemulation.VERSION, dependencies = "required-after:SilentGems@[1.3.12,)")
public class Gemulation {

    public static final String MODID = "gemulation";
    public static final String MODNAME = "Gemulation";
    public static final String VERSION = "@VERSION@";

    @Mod.Instance
    public static Gemulation instance = new Gemulation();

    @SidedProxy(clientSide = "com.m4thg33k.gemulation.core.proxies.ClientProxy",serverSide = "com.m4thg33k.gemulation.core.proxies.ServerProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e)
    {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
        proxy.postInit(e);
    }

    public static CreativeTabs tabGemulation = new CreativeTabs("tabGemulation") {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ModBlocks.gemFurnaceBlock);
        }
    };
}
