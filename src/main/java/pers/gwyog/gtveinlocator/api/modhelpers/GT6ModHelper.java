package pers.gwyog.gtveinlocator.api.modhelpers;

import gregapi.block.prefixblock.PrefixBlockTileEntity;
import gregapi.data.CS;
import gregapi.worldgen.GT6WorldGenerator;
import gregapi.worldgen.WorldgenOresLarge;
import net.minecraft.world.World;
import pers.gwyog.gtveinlocator.util.ClientVeinNameHelper;
import pers.gwyog.gtveinlocator.util.GTOreLayerHelper;
import pers.gwyog.gtveinlocator.util.GTVeinNameHelper;

import java.util.LinkedList;
import java.util.List;

public class GT6ModHelper implements IGTModHelper {

	@Override
	public void initClientVeinNameHelper() {      
        // map initialization start
        if (ClientVeinNameHelper.basicSupport) {
            GTVeinNameHelper.registerVeinName("gtveinlocator.ore.mix.empty");
            GTVeinNameHelper.registerVeinName("gtveinlocator.ore.mix.unknown");
            for (WorldgenOresLarge worldGen : WorldgenOresLarge.sList)
                if (worldGen.mEnabled) 
                    GTVeinNameHelper.registerVeinName("gtveinlocator." + worldGen.mName.replace("large", "mix"));
        }
	}

	@Override
	public void initGTOreLayerHelper() {
		// gt6 does not support galacticraft
    	GTOreLayerHelper.gcSupport = false; 
    	
        // initialization starts
        if (GTOreLayerHelper.basicSupport) {
            GTVeinNameHelper.registerVeinName("gtveinlocator.ore.mix.empty");
            GTVeinNameHelper.registerVeinName("gtveinlocator.ore.mix.unknown");
            
            List<WorldgenOresLarge> listOverworld =  GT6WorldGenerator.PFAA ? CS.ORE_PFAA : CS.ORE_OVERWORLD;
            List<WorldgenOresLarge> listNether = CS.ORE_NETHER;
            List<WorldgenOresLarge> listEnd = CS.ORE_END;
            
            for (WorldgenOresLarge worldGen : listOverworld) {
                if (worldGen.mEnabled) {
                    GTVeinNameHelper.registerVeinName("gtveinlocator." + worldGen.mName.replace("large", "mix"));
                    List<Short> componentList = new LinkedList<Short>();
                    componentList.add(worldGen.mTop.mID);
                    componentList.add(worldGen.mBottom.mID);
                    componentList.add(worldGen.mBetween.mID);
                    componentList.add(worldGen.mSpread.mID);
                    GTOreLayerHelper.mapGTOverworldOreLayer.put(componentList, "gtveinlocator." + worldGen.mName.replace("large", "mix"));
                    GTOreLayerHelper.minLevelOreOverworld = worldGen.mMinY<GTOreLayerHelper.minLevelOreOverworld? worldGen.mMinY: GTOreLayerHelper.minLevelOreOverworld;
                    GTOreLayerHelper.maxLevelOreOverworld = worldGen.mMaxY>GTOreLayerHelper.maxLevelOreOverworld? worldGen.mMaxY: GTOreLayerHelper.maxLevelOreOverworld;
                }
            }
                
            for (WorldgenOresLarge worldGen : listNether) {
                if (worldGen.mEnabled) {
                    GTVeinNameHelper.registerVeinName("gtveinlocator." + worldGen.mName.replace("large", "mix"));
                    List<Short> componentList = new LinkedList<Short>();
                    componentList.add(worldGen.mTop.mID);
                    componentList.add(worldGen.mBottom.mID);
                    componentList.add(worldGen.mBetween.mID);
                    componentList.add(worldGen.mSpread.mID);
                    GTOreLayerHelper.mapGTNetherOreLayer.put(componentList, "gtveinlocator." + worldGen.mName.replace("large", "mix"));
                    GTOreLayerHelper.minLevelOreNether = worldGen.mMinY<GTOreLayerHelper.minLevelOreNether? worldGen.mMinY: GTOreLayerHelper.minLevelOreNether;
                    GTOreLayerHelper.maxLevelOreNether = worldGen.mMaxY>GTOreLayerHelper.maxLevelOreNether? worldGen.mMaxY: GTOreLayerHelper.maxLevelOreNether;
                }
            }
            
            for (WorldgenOresLarge worldGen : listEnd) {
                if (worldGen.mEnabled) {
                    GTVeinNameHelper.registerVeinName("gtveinlocator." + worldGen.mName.replace("large", "mix"));
                    List<Short> componentList = new LinkedList<Short>();
                    componentList.add(worldGen.mTop.mID);
                    componentList.add(worldGen.mBottom.mID);
                    componentList.add(worldGen.mBetween.mID);
                    componentList.add(worldGen.mSpread.mID);
                    GTOreLayerHelper.mapGTEndOreLayer.put(componentList, "gtveinlocator." + worldGen.mName.replace("large", "mix"));
                    GTOreLayerHelper.minLevelOreEnd = worldGen.mMinY<GTOreLayerHelper.minLevelOreEnd? worldGen.mMinY: GTOreLayerHelper.minLevelOreEnd;
                    GTOreLayerHelper.maxLevelOreEnd = worldGen.mMaxY>GTOreLayerHelper.maxLevelOreEnd? worldGen.mMaxY: GTOreLayerHelper.maxLevelOreEnd;
              }
            }
        }
		
	}

	@Override
	public int getClosestIndex(double var1) {
        return (int)(Math.round((var1-24)/48));
	}

	@Override
	public int getCoordinateFromIndex(int index) {
        return 24+48*index;
	}
	
	@Override
	public boolean isGTBlockOre(World world, int posX, int posY, int posZ) {
		String unlocalizedName = world.getBlock(posX, posY, posZ).getUnlocalizedName();
		return unlocalizedName.startsWith("gt.meta.ore.normal") && !unlocalizedName.startsWith("gt.meta.ore.normal.bedrock");
	}
	
	@Override
	public short getGTOreMeta(World world, int posX, int posY, int posZ) {
		return ((PrefixBlockTileEntity)(world.getTileEntity(posX, posY, posZ))).mMetaData;
	}

	@Override
	public short getValidMeta(short meta) {
		return meta;
	}

}
