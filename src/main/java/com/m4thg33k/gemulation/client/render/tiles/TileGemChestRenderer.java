package com.m4thg33k.gemulation.client.render.tiles;

import com.google.common.collect.ImmutableMap;
import com.m4thg33k.gemulation.block.BlockGemChest;
import com.m4thg33k.gemulation.block.ModBlocks;
import com.m4thg33k.gemulation.lib.GemChestType;
import com.m4thg33k.gemulation.tiles.TileGemChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class TileGemChestRenderer extends TileEntitySpecialRenderer{

    private static Map<GemChestType, ResourceLocation> locations;

    static {
        ImmutableMap.Builder<GemChestType, ResourceLocation> builder = ImmutableMap.<GemChestType,ResourceLocation>builder();
        for (GemChestType typ : GemChestType.values())
        {
            builder.put(typ,new ResourceLocation("gemulation","textures/model/"+typ.getModelTexture()));
        }
        locations = builder.build();
    }

    private ModelChest model;

    public TileGemChestRenderer()
    {
        model = new ModelChest();
    }

    public void render(TileGemChest tile, double x, double y, double z, float partialTick, int breakStage)
    {
        if (tile==null)
        {
            return;
        }

        int facing = 3;
        GemChestType type = tile.getType();

        if (tile.hasWorldObj() && tile.getWorld().getBlockState(tile.getPos()).getBlock() == ModBlocks.gemChestBlock)
        {
            facing = tile.getFacing();
            type = tile.getType();
            IBlockState state = tile.getWorld().getBlockState(tile.getPos());
            type = (GemChestType)state.getValue(BlockGemChest.VARIANT_PROP);
        }

        if (breakStage >= 0)
        {
            bindTexture(DESTROY_STAGES[breakStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            bindTexture(locations.get(type));
        }
        GlStateManager.pushMatrix();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scale(1.0F, -1F, -1F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        int k = 0;
        if (facing == 2) {
            k = 180;
        }
        if (facing == 3) {
            k = 0;
        }
        if (facing == 4) {
            k = 90;
        }
        if (facing == 5) {
            k = -90;
        }
        GlStateManager.rotate(k, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float lidangle = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * partialTick;
        lidangle = 1.0F - lidangle;
        lidangle = 1.0F - lidangle * lidangle * lidangle;
        model.chestLid.rotateAngleX = -((lidangle * 3.141593F) / 2.0F);
        // Render the chest itself
        model.renderAll();
        if (breakStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }

        GlStateManager.popMatrix();
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        render((TileGemChest)te,x,y,z,partialTicks,destroyStage);
    }
}
