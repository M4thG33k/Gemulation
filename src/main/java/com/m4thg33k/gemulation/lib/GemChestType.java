package com.m4thg33k.gemulation.lib;

import com.m4thg33k.gemulation.block.BlockGemChest;
import com.m4thg33k.gemulation.tiles.TileGemChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.item.ModItems;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

//Template stolen with love from cpw's Iron Chest mod

public enum GemChestType implements IStringSerializable {
    RUBY(54,9,true,"Ruby Chest","GemChest0.png",0, 0,TileGemChest.class,"mmmmCmmmm"),
    GARNET(54,9,true,"Garnet Chest","GemChest1.png",0, 1,TileGemChest.class,"mmmmCmmmm"),
    TOPAZ(54,9,true,"Topaz Chest","GemChest2.png",0, 2,TileGemChest.class,"mmmmCmmmm"),
    HELIODOR(54,9,true,"Heliodor Chest","GemChest3.png",0, 3,TileGemChest.class,"mmmmCmmmm"),
    PERIDOT(54,9,true,"Peridot Chest","GemChest4.png",0,4,TileGemChest.class,"mmmmCmmmm"),
    EMERALD(54,9,true,"Emerald Chest","GemChest5.png",0,5,TileGemChest.class,"mmmmCmmmm"),
    AQUAMARINE(54,9,true,"Aquamarine Chest","GemChest6.png",0, 6,TileGemChest.class,"mmmmCmmmm"),
    SAPPHIRE(54,9,true,"Sapphire Chest","GemChest7.png",0, 7,TileGemChest.class,"mmmmCmmmm"),
    IOLITE(54,9,true,"Iolite Chest","GemChest8.png",0, 8,TileGemChest.class,"mmmmCmmmm"),
    AMETHYST(54,9,true,"Amethyst Chest","GemChest9.png",0, 9,TileGemChest.class,"mmmmCmmmm"),
    MORGANITE(54,9,true,"Morganite Chest","GemChest10.png",0, 10,TileGemChest.class,"mmmmCmmmm"),
    ONYX(54,9,true,"Onyx Chest","GemChest11.png",0, 11,TileGemChest.class,"mmmmCmmmm")
    ;


    public int size;
    private int rowLength;
    public String friendlyName;
    private boolean  tieredChest;
    private String modelTexture;
    private int textureRow;
    public Class<? extends TileGemChest> clazz;
    private String[] recipes;
    private int material;
//    private ArrayList<String> matList;
    private Item itemFilter;

    GemChestType(int size, int rowLength, boolean tieredChest, String friendlyName, String modelTexture, int textureRow, int material, Class<? extends TileGemChest> clazz, String... recipes)
    {
        this(size, rowLength, tieredChest, friendlyName, modelTexture, textureRow, material, clazz, (Item)null, recipes);
    }
    GemChestType(int size, int rowLength, boolean tieredChest, String friendlyName, String modelTexture, int textureRow, int material, Class<? extends TileGemChest> clazz,Item itemFilter, String... recipes)
    {
        this.size = size;
        this.rowLength = rowLength;
        this.tieredChest = tieredChest;
        this.friendlyName = friendlyName;
        this.modelTexture = modelTexture;
        this.textureRow = textureRow;
        this.clazz = clazz;
        this.itemFilter = itemFilter;
        this.recipes = recipes;
        this.material = material;
//        this.matList = new ArrayList<>();
//        matList.addAll(mats);
    }


    @Override
    public String getName() {
        return name().toLowerCase();
    }

    public String getModelTexture()
    {
        return modelTexture;
    }

    public int getTextureRow()
    {
        return textureRow;
    }

    public static TileGemChest makeEntity(int metadata)
    {
        //compat
        int chesttype = validateMeta(metadata);
        if (chesttype == metadata)
        {
            try
            {
                TileGemChest te = values()[chesttype].clazz.newInstance();

                return te;
            }
            catch (InstantiationException e)
            {
                //impossible
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                //impossible
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void registerBlocksAndRecipes(BlockGemChest blockResult)
    {
        Object previous = "chestWood";
        for (GemChestType typ : values())
        {
            generateRecipesForType(blockResult,previous,typ);
            ItemStack chest = new ItemStack(blockResult,1,typ.ordinal());
            if (typ.tieredChest)
            {
                previous = chest;
            }
        }
    }

    public static void generateRecipesForType(BlockGemChest blockResult, Object previousTier, GemChestType type)
    {
        for (String recipe : type.recipes)
        {
            String[] recipeSplit = new String[]{recipe.substring(0,3),recipe.substring(3,6),recipe.substring(6,9)};
            Object mainMaterial = translateOreName(type.material);
            addRecipe(new ItemStack(blockResult,1,type.ordinal()),recipeSplit,'m',mainMaterial,'P', previousTier, 'G', "blockGlass", 'C', "chestWood");
//            for (String mat : type.matList)
//            {
//                mainMaterial = translateOreName(mat);
//                addRecipe(new ItemStack(blockResult,1,type.ordinal()), recipeSplit, 'm', mainMaterial, 'P', previousTier, 'G', "blockGlass", 'C', "chestWood"); //// TODO: 3/22/2016 add recipes handles for the subtypes
//            }
        }
    }

    public static Object translateOreName(int material)
    {
        if (material>=0 && material<12)
        {
            return new ItemStack(ModItems.gem,1,material);
        }
        else
        {
            return new ItemStack(Items.nether_star,1);
        }
    }

    public static Object translateOreName(String mat)
    {
        if (mat.equals("obsidian"))
        {
            return Blocks.obsidian;
        }
        else if (mat.equals("dirt"))
        {
            return Blocks.dirt;
        }
        else if (mat.equals("0"))
        {
            return new ItemStack(ModItems.gem,1,0);
        }
        else if (mat.equals("1")) {
            return new ItemStack(ModItems.gem, 1, 1);
        }
        return mat;
    }

    public static  void addRecipe(ItemStack is, Object... parts)
    {
        ShapedOreRecipe oreRecipe = new ShapedOreRecipe(is, parts);
        GameRegistry.addRecipe(oreRecipe);
    }

    public int getRowCount()
    {
        return size / rowLength;
    }

    public int getRowLength()
    {
        return rowLength;
    }

//    public List<String> getMatList()
//    {
//        return matList;
//    }

    public static int validateMeta(int i)
    {
        if (i< values().length && values()[i].size > 0)
        {
            return i;
        }
        return 0;
    }

    public boolean isValidForCreativeMode()
    {
        return validateMeta(ordinal()) == ordinal();
    }

    public boolean isExplosionResistant()
    {
        return false; //// TODO: 3/22/2016 make the supercharged chests resistant
    }


    public boolean acceptsStack(ItemStack itemStack)
    {
        return itemFilter == null || itemStack == null || itemStack.getItem() == itemFilter;
    }
}
