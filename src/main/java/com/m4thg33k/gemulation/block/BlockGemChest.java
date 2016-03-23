package com.m4thg33k.gemulation.block;

import com.google.common.collect.Lists;
import com.m4thg33k.gemulation.Gemulation;
import com.m4thg33k.gemulation.lib.GemChestType;
import com.m4thg33k.gemulation.lib.Names;
import com.m4thg33k.gemulation.tiles.TileGemChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockGemChest extends BlockContainer {

    public static final PropertyEnum<GemChestType> VARIANT_PROP = PropertyEnum.create("variant", GemChestType.class);

    public BlockGemChest()
    {
        super(Material.iron);

        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP,GemChestType.RUBY));

        this.setBlockBounds(0.0625f,0f,0.0625f,0.9375f,0.875f,0.9375f);
        this.setHardness(3.0f);
        this.setUnlocalizedName(Names.GEM_CHEST);
        this.setCreativeTab(Gemulation.tabGemulation);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);

        if (te==null || !(te instanceof TileGemChest))
        {
            return true;
        }

        if (worldIn.isSideSolid(pos.add(0,1,0),EnumFacing.DOWN))
        {
            return true;
        }
        if (worldIn.isRemote)
        {
            return true;
        }

        playerIn.openGui(Gemulation.instance,((TileGemChest)te).getType().ordinal()+2,worldIn,pos.getX(),pos.getY(),pos.getZ());
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return GemChestType.makeEntity(meta);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (GemChestType type : GemChestType.values())
        {
            if (type.isValidForCreativeMode())
            {
                list.add(new ItemStack(itemIn,1,type.ordinal()));
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT_PROP,GemChestType.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((GemChestType)state.getValue(VARIANT_PROP)).ordinal();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this,VARIANT_PROP);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> items = Lists.newArrayList();
        ItemStack stack = new ItemStack(this,1,getMetaFromState(state));
        items.add(stack);
        return items;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.markBlockForUpdate(pos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        byte chestFacing = 0;
        int facing = MathHelper.floor_double((placer.rotationYaw*4f)/360f + 0.5d)&3;
        switch (facing)
        {
            case 0:
                chestFacing = 2;
                break;
            case 1:
                chestFacing = 5;
                break;
            case 2:
                chestFacing = 3;
                break;
            case 3:
                chestFacing = 4;
                break;
            default:
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null && te instanceof TileGemChest)
        {
            TileGemChest gemChest = (TileGemChest)te;
            gemChest.wasPlaced(placer,stack);
            gemChest.setFacing(chestFacing);
            worldIn.markBlockForUpdate(pos);
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return GemChestType.validateMeta((state.getValue(VARIANT_PROP)).ordinal());
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileGemChest gemChest = (TileGemChest)worldIn.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(worldIn,pos,gemChest);
        super.breakBlock(worldIn,pos,state);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileGemChest && ((TileGemChest) te).getType().isExplosionResistant())
        {
            return 10000f;
        }
        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof IInventory)
        {
            return Container.calcRedstoneFromInventory((IInventory)te);
        }
        return 0;
    }

    private static final EnumFacing[] validRotationAxes = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};

    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return validRotationAxes;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if (world.isRemote)
        {
            return false;
        }

        if (axis== EnumFacing.UP || axis== EnumFacing.DOWN)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileGemChest)
            {
                ((TileGemChest) tileEntity).rotateAround();
            }
            return true;
        }
        return false;
    }

    @Override
    public int getRenderType() {
        return 2;
    }
}
