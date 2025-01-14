package pers.gwyog.gtveinlocator.api.modhelpers;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.blocks.GT_TileEntity_Ores;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import pers.gwyog.gtveinlocator.config.ModConfig;
import pers.gwyog.gtveinlocator.util.ClientVeinNameHelper;
import pers.gwyog.gtveinlocator.util.GTOreLayerHelper;
import pers.gwyog.gtveinlocator.util.GTVeinNameHelper;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;


public class GT5ModHelper implements IGTModHelper {


    @Override
    public void initClientVeinNameHelper() {
        // in case that the user is using old version of GT
        try {
            Class clazzGTOreLayer = Class.forName("gregtech.common.GT_Worldgen_GT_Ore_Layer");
            Field fieldOverworld = clazzGTOreLayer.getField("mOverworld");
            Field fieldNether = clazzGTOreLayer.getField("mNether");
            Field fieldEnd = clazzGTOreLayer.getField("mEnd");
            Field fieldName = clazzGTOreLayer.getField("mWorldGenName");
            Field fieldList = clazzGTOreLayer.getField("sList");
        } catch (Exception e) {
            ClientVeinNameHelper.basicSupport = false;
        }

        //map initialization start
        if (ClientVeinNameHelper.basicSupport) {
            GTVeinNameHelper.registerVeinName("gtveinlocator.ore.mix.empty");
            GTVeinNameHelper.registerVeinName("gtveinlocator.ore.mix.unknown");
            for (GT_Worldgen_GT_Ore_Layer worldGen : GT_Worldgen_GT_Ore_Layer.sList)
                if (worldGen.mEnabled)
                    GTVeinNameHelper.registerVeinName("gtveinlocator." + worldGen.mWorldGenName);
        }
    }

    @Override
    public void initGTOreLayerHelper() {
        // in case that the user is using old version of GT
        Class clazzGTOreLayer = null;
        try {
            clazzGTOreLayer = Class.forName("gregtech.common.GT_Worldgen_GT_Ore_Layer");
            Field fieldOverworld = clazzGTOreLayer.getField("mOverworld");
            Field fieldNether = clazzGTOreLayer.getField("mNether");
            Field fieldEnd = clazzGTOreLayer.getField("mEnd");
            Field fieldEnabled = clazzGTOreLayer.getField("mEnabled");
            Field fieldPrimaryMeta = clazzGTOreLayer.getField("mPrimaryMeta");
            Field fieldSecondaryMeta = clazzGTOreLayer.getField("mSecondaryMeta");
            Field fieldBetweenMeta = clazzGTOreLayer.getField("mBetweenMeta");
            Field fieldSporadicMeta = clazzGTOreLayer.getField("mSporadicMeta");
            Field fieldName = clazzGTOreLayer.getField("mWorldGenName");
            Field fieldList = clazzGTOreLayer.getField("sList");
        } catch (Exception e) {
            GTOreLayerHelper.basicSupport = false;
        }
        if (clazzGTOreLayer != null && clazzGTOreLayer != null)
            try {
                Field fieldMoon = clazzGTOreLayer.getField("mMoon");
                Field fieldMars = clazzGTOreLayer.getField("mMars");
            } catch (Exception e) {
                GTOreLayerHelper.gcSupport = false;
            }

        // initialization starts
        if (GTOreLayerHelper.basicSupport) {
            GTVeinNameHelper.registerVeinName("gtveinlocator.ore.mix.empty");
            GTVeinNameHelper.registerVeinName("gtveinlocator.ore.mix.unknown");
            for (GT_Worldgen_GT_Ore_Layer worldGen : GT_Worldgen_GT_Ore_Layer.sList) {
                if (worldGen.mEnabled) {
                    GTVeinNameHelper.registerVeinName("gtveinlocator." + worldGen.mWorldGenName);
                    List<Short> componentList = new LinkedList<Short>();
                    componentList.add(worldGen.mPrimaryMeta);
                    componentList.add(worldGen.mSecondaryMeta);
                    componentList.add(worldGen.mBetweenMeta);
                    componentList.add(worldGen.mSporadicMeta);
                    if (worldGen.mOverworld) {
                        GTOreLayerHelper.mapGTOverworldOreLayer.put(componentList, "gtveinlocator." + worldGen.mWorldGenName);
                        GTOreLayerHelper.minLevelOreOverworld = worldGen.mMinY < GTOreLayerHelper.minLevelOreOverworld ? worldGen.mMinY : GTOreLayerHelper.minLevelOreOverworld;
                        GTOreLayerHelper.maxLevelOreOverworld = worldGen.mMaxY > GTOreLayerHelper.maxLevelOreOverworld ? worldGen.mMaxY : GTOreLayerHelper.maxLevelOreOverworld;
                    }
                    if (worldGen.mNether) {
                        GTOreLayerHelper.mapGTNetherOreLayer.put(componentList, "gtveinlocator." + worldGen.mWorldGenName);
                        GTOreLayerHelper.minLevelOreNether = worldGen.mMinY < GTOreLayerHelper.minLevelOreNether ? worldGen.mMinY : GTOreLayerHelper.minLevelOreNether;
                        GTOreLayerHelper.maxLevelOreNether = worldGen.mMaxY > GTOreLayerHelper.maxLevelOreNether ? worldGen.mMaxY : GTOreLayerHelper.maxLevelOreNether;
                    }
                    if (worldGen.mEnd) {
                        GTOreLayerHelper.mapGTEndOreLayer.put(componentList, "gtveinlocator." + worldGen.mWorldGenName);
                        GTOreLayerHelper.minLevelOreEnd = worldGen.mMinY < GTOreLayerHelper.minLevelOreEnd ? worldGen.mMinY : GTOreLayerHelper.minLevelOreEnd;
                        GTOreLayerHelper.maxLevelOreEnd = worldGen.mMaxY > GTOreLayerHelper.maxLevelOreEnd ? worldGen.mMaxY : GTOreLayerHelper.maxLevelOreEnd;
                    }
                    if (GTOreLayerHelper.gcSupport) {
                        if (worldGen.mMoon) {
                            GTOreLayerHelper.mapGTMoonOreLayer.put(componentList, "gtveinlocator." + worldGen.mWorldGenName);
                            GTOreLayerHelper.minLevelOreMoon = worldGen.mMinY < GTOreLayerHelper.minLevelOreMoon ? worldGen.mMinY : GTOreLayerHelper.minLevelOreMoon;
                            GTOreLayerHelper.maxLevelOreMoon = worldGen.mMaxY > GTOreLayerHelper.maxLevelOreMoon ? worldGen.mMaxY : GTOreLayerHelper.maxLevelOreMoon;
                        }
                        if (worldGen.mMars) {
                            GTOreLayerHelper.mapGTMarsOreLayer.put(componentList, "gtveinlocator." + worldGen.mWorldGenName);
                            GTOreLayerHelper.minLevelOreMars = worldGen.mMinY < GTOreLayerHelper.minLevelOreMars ? worldGen.mMinY : GTOreLayerHelper.minLevelOreMars;
                            GTOreLayerHelper.maxLevelOreMars = worldGen.mMaxY > GTOreLayerHelper.maxLevelOreMars ? worldGen.mMaxY : GTOreLayerHelper.maxLevelOreMars;
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getClosestIndex(double var1) {
        return (int) (var1 < 8 && ModConfig.matchMisplacement ? Math.round((var1 - 40) / 48) : Math.round((var1 - 24) / 48));
    }

    @Override
    public int getCoordinateFromIndex(int index) {
        return index < 0 && ModConfig.matchMisplacement ? (40 + 48 * index) : (24 + 48 * index);
    }

    @Override
    public boolean isGTBlockOre(World world, int posX, int posY, int posZ) {
        return world.getBlock(posX, posY, posZ).getUnlocalizedName().startsWith("gt.blockores");
    }

    @Override
    public short getGTOreMeta(World world, int posX, int posY, int posZ) {
        return ((GT_TileEntity_Ores) world.getTileEntity(posX, posY, posZ)).mMetaData;
    }

    @Override
    public short getValidMeta(short meta) {
        return (short) (meta >= 16000 ? -1 : meta % 1000);
    }

}
