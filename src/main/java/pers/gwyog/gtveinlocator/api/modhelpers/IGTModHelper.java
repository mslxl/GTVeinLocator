package pers.gwyog.gtveinlocator.api.modhelpers;

import net.minecraft.world.World;

public interface IGTModHelper {


	void initClientVeinNameHelper();
	
	void initGTOreLayerHelper();
	
	int getClosestIndex(double var1);
	
	int getCoordinateFromIndex(int index);
	
	short getGTOreMeta(World world, int posX, int posY, int posZ);

	boolean isGTBlockOre(World world, int posX, int posY, int posZ);

    short getValidMeta(short meta);
}
