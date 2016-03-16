package com.m4thg33k.gemulation.api;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;

public class GemulationStateProps {

    public static final PropertyEnum<EnumFacing> CARDINALS = PropertyEnum.create("facing",EnumFacing.class,EnumFacing.Plane.HORIZONTAL);
}
