package pers.gwyog.gtveinlocator.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ModConfig {
     private static Configuration config;
     private static Logger logger;
     public static int waypointYLevelAdvancedLocator;
     public static int waypointYLevelEliteLocator;
     public static int waypointColorJourneyMap;
     public static int waypointColorXaeroMinimap;
     public static String waypointSymbolXaeroMinimap;
     public static List<Integer> overworldLikeDimensions;
     public static List<Integer> netherLikeDimensions;
     public static List<Integer> endLikeDimensions;
     public static boolean matchMisplacement;
     
     public ModConfig(FMLPreInitializationEvent event) {
         logger = event.getModLog();
         config = new Configuration(event.getSuggestedConfigurationFile());
         load();
     }

     public static void load()
     {        
         logger.info("Started loading config.");           
         config.load();
         
         config.addCustomCategoryComment("Locator Enabled", "You can set if specific locator is enabled or whether it consumes energy when using.");
         config.addCustomCategoryComment("Locator Use Energy", "You can configure a setting here to make the mod in an esay mode.");
         config.addCustomCategoryComment("Locator Data", "You can set the basic data of the vein locators of different levels to balance them.");
         config.addCustomCategoryComment("Compatibility", "Things that are related with other mods can be configured here.");
         config.addCustomCategoryComment("Loot Tweaks", "You can set if the locators would generate in the loot chests and the possibility to find them.\nTips: The basic name of the chests are: \n    mineshaftCorridor, pyramidDesertyChest, pyramidJungleChest, pyramidJungleDispenser \n    strongholdCorridor, strongholdLibrary, strongholdCrossing, villageBlacksmith \n    bonusChest, dungeonChest \nNote: If you want to make specific locator appear in multiple kinds of loot-chests, you should use MineTweaker.");
         config.addCustomCategoryComment("Recipe Disabled", "You can disable the recipe of specific locator here.");
         config.addCustomCategoryComment("Creative Tab Icon", "For those whose client crashes everytime switching to GTVL's creative tab, you can now change the icon to minecraft's compass to avoid crashing.");
         config.addCustomCategoryComment("Dimension White List", "Elite locator will only function in these dimensions and GalactiCraft's planets. This catagory aims mainly at compatibility for bukkit plugins like Multiverse-Core.");
         

         
         //vein locator data
         String matchMisplacementDes = "Set this to true to match the vein location misplacement issue before GT5.09.29";
         matchMisplacement = config.get("Locator Data", "matchMisplacement", false, matchMisplacementDes).getBoolean();
    
         
         //compatibility
         String yLevelAdvancedLocatorDes = "The Y level of the auto-generated waypoints from the Advanced Vein-Locator.";   
         String yLevelEliteLocatorDes = "The Y level of the auto-generated empty or unknown waypoints from the Elite Vein-Locator.";   
         String waypointColorJourneyMapDes = "The color of the waypoints on the JourneyMap added by advanced and elite locators. Please use hexadecimal. For example, 0xFFFFFF means white. Set to -1 will use the random color.";
         String waypointColorXaeroMinimapDes = "The color of the waypoints on the XaeroMinimap added by advanced and elite locators. The range is 0-15, which corresponds with the 16 colors of XaeroMinimap's waypoints. Set to -1 will use the random color.";    
         String waypointSymbolXaeroMinimapDes = "The symbol of the waypoints on the XaeroMinimap. That is to say, this is the string symbol shown above the waypoints in the minimap. The default value is 'X'.";
         Property propertyYLevelAdvancedLocator = config.get("Compatibility", "waypointYLevelAdvancedLocator", 70, yLevelAdvancedLocatorDes);
         waypointYLevelAdvancedLocator = getSafeIntFromProperty(propertyYLevelAdvancedLocator, 0, 255);
         Property propertyYLevelEliteLocator = config.get("Compatibility", "waypointYLevelEliteLocator", 70, yLevelEliteLocatorDes);
         waypointYLevelEliteLocator = getSafeIntFromProperty(propertyYLevelEliteLocator, 0, 255);               
         Property propertyWaypointColorJourneyMap = config.get("Compatibility", "waypointColorJourneyMap", "-1", waypointColorJourneyMapDes);
         waypointColorJourneyMap = getSafeColorFromProperty(propertyWaypointColorJourneyMap, -1, Integer.MAX_VALUE);
         Property propertyWaypointColorXaeroMinimap = config.get("Compatibility", "waypointColorXaeroMinimap", -1, waypointColorXaeroMinimapDes);
         waypointColorXaeroMinimap = getSafeIntFromProperty(propertyWaypointColorXaeroMinimap, -1, 15);
         waypointSymbolXaeroMinimap = config.get("Compatibility", "waypointSymbolXaeroMinimap", "X", waypointSymbolXaeroMinimapDes).getString();
        
         //dimension white list
         String overworldLikeDimensionsDes = "This list of dimension ids for overworld-like dimensions which elite vein locator should work in.";
         String netherLikeDimensionsDes = "This list of dimension ids for nether-like dimensions which elite vein locator should work in.";
         String endLikeDimensionsDes = "This list of dimension ids for end-like dimensions which elite vein locator should work in.";         
         overworldLikeDimensions = Arrays.asList(ArrayUtils.toObject(config.get("Dimension White List", "overworldLikeDimensionWhitelist", new int[]{0}, overworldLikeDimensionsDes).getIntList()));
         netherLikeDimensions = Arrays.asList(ArrayUtils.toObject(config.get("Dimension White List", "netherLikeDimensionWhitelist", new int[]{-1}, netherLikeDimensionsDes).getIntList()));
         endLikeDimensions = Arrays.asList(ArrayUtils.toObject(config.get("Dimension White List", "endLikeDimensionWhitelist", new int[]{1}, endLikeDimensionsDes).getIntList()));
         
         config.save();        
         logger.info("Finished loading config.");
     }
    
     public static Logger getLogger() {  
         return logger;
     }
     
     public static double getSafeDoubleFromProperty(Property property, double min, double max) {
         double temp = property.getDouble();
         if (temp<min || temp>max) {
             property.setToDefault();
             temp = property.getDouble();
         }
         return temp;
     }
     
     public static int getSafeIntFromProperty(Property property, int min, int max) {
         int temp = property.getInt();
         if (temp<min || temp>max) {
             property.setToDefault();
             temp = property.getInt();
         }
         return temp;
     }
     
     public static int getSafeColorFromProperty(Property property, int min, int max) {
    	 String colorString = property.getString();
    	 if (colorString.startsWith("0x"))
    		 colorString = colorString.replace("0x", "");
    	 try {
    		 int color = Integer.parseInt(colorString, 16);
    		 if (color<min || color>max)
    			 return -1;
    		 else
    			 return color;
    	 }
    	 catch (NumberFormatException e) {
    		 property.setToDefault();
    		 return -1;
    	 }
     }
     
}
