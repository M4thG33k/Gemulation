package com.m4thg33k.gemulation.client.render.handlers;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureHandler {

    @SubscribeEvent
    public void stitchTexture(TextureStitchEvent.Pre pre)
    {
//        LogHelper.info(("Stitching Gemulation Textures"));
//        pre.map.registerSprite(new ResourceLocation(Gemulation.MODID,"gui/"+ Names.GEM_FURNACE));
    }
}
