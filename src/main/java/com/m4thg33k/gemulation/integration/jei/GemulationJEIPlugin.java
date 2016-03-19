package com.m4thg33k.gemulation.integration.jei;

import com.m4thg33k.gemulation.core.util.LogHelper;
import mezz.jei.api.*;

import javax.annotation.Nonnull;

@mezz.jei.api.JEIPlugin
public class GemulationJEIPlugin extends BlankModPlugin{

    public static IJeiHelpers jeiHelpers;

    public GemulationJEIPlugin()
    {

    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();
        LogHelper.info("JEI STUFFZ!!!");
    }
}
