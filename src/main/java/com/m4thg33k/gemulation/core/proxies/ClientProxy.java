package com.m4thg33k.gemulation.core.proxies;

import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.client.render.block.BlockRenderRegister;
import com.m4thg33k.gemulation.client.render.handlers.TextureHandler;
import com.m4thg33k.gemulation.client.render.item.ItemRenderRegister;
import com.m4thg33k.gemulation.client.render.tiles.TileGemChestRenderer;
import com.m4thg33k.gemulation.network.packets.PacketNBT;
import com.m4thg33k.gemulation.tiles.TileGemChest;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy  extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        ItemRenderRegister.registerItemRenderer();
        OBJLoader.instance.addDomain(Gemulation.MODID);
        MinecraftForge.EVENT_BUS.register(new TextureHandler());
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);

        BlockRenderRegister.registerBlockRenderer();

        //register chest TESRS
        ClientRegistry.bindTileEntitySpecialRenderer(TileGemChest.class,new TileGemChestRenderer()); //re-work tesr a bit
//        for (GemChestType type : GemChestType.values())
//        {
//            GameRegistry.registerTileEntityWithAlternatives(type.clazz, "GemChest." + type.name(),type.name());
//            ClientRegistry.bindTileEntitySpecialRenderer(type.clazz,new TileGemChestRenderer<>(type.clazz));
//            this.registerTileEntitySpecialRenderer(type.clazz);
//        }

    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @Override
    public void handleNBTPacket(PacketNBT message) {
        Minecraft.getMinecraft().theWorld.getTileEntity(message.pos).readFromNBT(message.compound);
        Minecraft.getMinecraft().theWorld.markBlockForUpdate(message.pos);
    }

//    @Override
//    public void registerRenderInformation() {
//        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().registerBuiltInBlocks(ModBlocks.gemChestBlock);
//        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
//
//        for (GemChestType type : GemChestType.values())
//        {
//            Item chestItem = Item.getItemFromBlock(ModBlocks.gemChestBlock);
//            ModelLoader.setCustomModelResourceLocation(chestItem,type.ordinal(),new ModelResourceLocation(Gemulation.MODID+":"+ Names.GEM_CHEST,"inventory"));
////            mesher.register(chestItem,type.ordinal(),new ModelResourceLocation("gemulation:chest_" + type.getName().toLowerCase(),"inventory"));
////            ModelBakery.addVariantName(chestItem,"gemulation:chest_" + type.getName().toLowerCase());
//        }
//    }


//    @Override
//    public <T extends TileGemChest> void registerTileEntitySpecialRenderer(Class<T> type) {
//        ClientRegistry.bindTileEntitySpecialRenderer(type,new TileGemChestRenderer<>(type));
//    }
}
