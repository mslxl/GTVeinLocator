package pers.gwyog.gtveinlocator.cmd;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import pers.gwyog.gtveinlocator.GTVeinLocator;
import pers.gwyog.gtveinlocator.config.ModConfig;
import pers.gwyog.gtveinlocator.network.ClientInfoMessageTranslationPacket;
import pers.gwyog.gtveinlocator.network.ClientVeinNameTranslationPacket;
import pers.gwyog.gtveinlocator.network.ClientWaypointPacket;
import pers.gwyog.gtveinlocator.network.GTVLNetwork;
import pers.gwyog.gtveinlocator.util.GTOreLayerHelper;

import java.util.*;

public class CommandCheat extends CommandBase {

    public static void init(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandCheat());
    }

    @Override
    public String getCommandName() {
        return "gtore";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "command.gtore.usage";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        EntityPlayerMP player = strings.length > 0 ? CommandBase.getPlayer(iCommandSender, strings[0])
                : CommandBase.getCommandSenderAsPlayer(iCommandSender);
        World world = player.worldObj;


        int searchRange = 5;

        if (!world.isRemote) {
            int indexX = getClosestIndex(player.posX);
            int indexZ = getClosestIndex(player.posZ);
            int veinCount = 0;
            int dimId = player.dimension;
            WorldProvider worldProvider = world.provider;
            int targetX, targetY, targetZ;
            String foundVeinNames = "", nameUnlocalitzed;
            for (int i = (1 - searchRange) / 2; i < (1 + searchRange) / 2; i++)
                for (int j = (1 - searchRange) / 2; j < (1 + searchRange) / 2; j++) {
                    targetX = getCoordinateFromIndex(indexX + i);
                    targetZ = getCoordinateFromIndex(indexZ + j);
                    targetY = getVeinYLevel(world, targetX, targetZ);
                    nameUnlocalitzed = GTOreLayerHelper.judgeOreLayerName(judgeVeinComponent(world, targetX, targetY, targetZ), getWorldNameEnum(worldProvider));
                    GTVLNetwork.INSTANCE.sendTo(new ClientWaypointPacket(nameUnlocalitzed, targetX, targetY, targetZ, dimId), (EntityPlayerMP) player);
                    if (!nameUnlocalitzed.equals("gtveinlocator.ore.mix.empty") && !nameUnlocalitzed.equals("gtveinlocator.ore.mix.unknown")) {
                        veinCount++;
                        if (foundVeinNames.isEmpty()) {
                            foundVeinNames = nameUnlocalitzed;
                        } else
                            foundVeinNames += "," + nameUnlocalitzed;
                    }
                }
            GTVLNetwork.INSTANCE.sendTo(new ClientInfoMessageTranslationPacket(2, new int[]{veinCount, searchRange, searchRange}), (EntityPlayerMP) player);
            if (!foundVeinNames.isEmpty())
                GTVLNetwork.INSTANCE.sendTo(new ClientVeinNameTranslationPacket(foundVeinNames), (EntityPlayerMP) player);
        }
    }


    public GTOreLayerHelper.WorldNameEnum getWorldNameEnum(WorldProvider provider) {
        if (ModConfig.overworldLikeDimensions.contains(provider.dimensionId))
            return GTOreLayerHelper.WorldNameEnum.overworld;
        else if (ModConfig.netherLikeDimensions.contains(provider.dimensionId))
            return GTOreLayerHelper.WorldNameEnum.nether;
        else if (ModConfig.endLikeDimensions.contains(provider.dimensionId))
            return GTOreLayerHelper.WorldNameEnum.end;
        else if (provider.getDimensionName().equals(StatCollector.translateToLocal("dimension.Moon.name")))
            return GTOreLayerHelper.WorldNameEnum.moon;
        else if (provider.getDimensionName().equals(StatCollector.translateToLocal("dimension.Mars.name")))
            return GTOreLayerHelper.WorldNameEnum.mars;
        else
            //other worlds where GT veins don't generate
            return GTOreLayerHelper.WorldNameEnum.unknown;
    }

    public int getVeinYLevel(World world, int x, int z) {
        GTOreLayerHelper.WorldNameEnum worldName = getWorldNameEnum(world.provider);
        for (int y = GTOreLayerHelper.getMinOreLevel(worldName) - 2; y <= GTOreLayerHelper.getMaxOreLevel(worldName); y++) {
            for (int dx = -2; dx < 3; dx++)
                for (int dz = -2; dz < 3; dz++) {
                    if (GTVeinLocator.gtModHelper.isGTBlockOre(world, x + dx, y, z + dz)) {
                        short meta = GTVeinLocator.gtModHelper.getGTOreMeta(world, x + dx, y, z + dz);
                        meta = GTVeinLocator.gtModHelper.getValidMeta(meta);
                        if (meta == -1)
                            continue;
                        return y;
                    }
                }
            if (world.canBlockSeeTheSky(x, y - 5, z))
                break;
        }
        return -1;
    }

    public List<Short> judgeVeinComponent(World world, int x, int y, int z) {
        Map<Short, Integer> map = new HashMap<Short, Integer>();
        if (y != -1)
            return getSortedListFromMap(scanCarefully(map, world, x, y, z));
        else
            return null;
    }

    public Map<Short, Integer> scanCarefully(Map<Short, Integer> map, World world, int x, int y, int z) {
        //the max range of dy comes from GT's oregen algorithm
        for (int dy = -2; dy < 7; dy++)
            for (int dx = -4; dx < 5; dx++)
                for (int dz = -4; dz < 5; dz++) {
                    if (GTVeinLocator.gtModHelper.isGTBlockOre(world, x + dx, y, z + dz)) {
                        short meta = GTVeinLocator.gtModHelper.getGTOreMeta(world, x + dx, y, z + dz);
                        meta = GTVeinLocator.gtModHelper.getValidMeta(meta);
                        if (meta == -1)
                            continue;
                        if (map.containsKey(meta))
                            map.put(meta, map.get(meta) + 1);
                        else
                            map.put(meta, 1);
                    }
                }
        return map;
    }

    public List<Short> getSortedListFromMap(Map<Short, Integer> map) {
        List<Map.Entry<Short, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Short, Integer>>() {
            @Override
            public int compare(Map.Entry<Short, Integer> o1, Map.Entry<Short, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });


        List<Short> ret = new LinkedList<Short>();
        for (short meta : map.keySet()) {
            if (meta == 0)
                System.out.println(map.get(0));
            else
                ret.add(meta);
        }
        return ret;
    }

    protected int getClosestIndex(double var1) {
        return GTVeinLocator.gtModHelper.getClosestIndex(var1);
    }

    protected int getCoordinateFromIndex(int index) {
        return GTVeinLocator.gtModHelper.getCoordinateFromIndex(index);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 1;
    }
}
