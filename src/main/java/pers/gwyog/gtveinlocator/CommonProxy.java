package pers.gwyog.gtveinlocator;

import cpw.mods.fml.common.event.*;
import net.minecraftforge.common.MinecraftForge;
import pers.gwyog.gtveinlocator.cmd.CommandCheat;
import pers.gwyog.gtveinlocator.compat.LoadedModHelper;
import pers.gwyog.gtveinlocator.config.ModConfig;
import pers.gwyog.gtveinlocator.network.GTVLNetwork;
import pers.gwyog.gtveinlocator.util.ClientVeinNameHelper;
import pers.gwyog.gtveinlocator.util.GTOreLayerHelper;

public class CommonProxy {
    
    public void preInit(FMLPreInitializationEvent e) {
        new ModConfig(e);
        new GTVLNetwork();
    }
    
    public void init(FMLInitializationEvent e) {
    	// we put it here just in case gt6's hv sensor has not been set in the preInit period
    }

    public void postInit(FMLPostInitializationEvent e) {
        
    }
    
    public void onServerStart(FMLServerStartedEvent e) {
        GTOreLayerHelper.init();
    }
    public void onServerStarting(FMLServerStartingEvent event)
    {
        CommandCheat.init(event);
    }
}

