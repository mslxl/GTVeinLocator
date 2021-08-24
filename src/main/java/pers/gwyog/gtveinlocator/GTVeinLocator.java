package pers.gwyog.gtveinlocator;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pers.gwyog.gtveinlocator.api.modhelpers.GT5ModHelper;
import pers.gwyog.gtveinlocator.api.modhelpers.GT6ModHelper;
import pers.gwyog.gtveinlocator.api.modhelpers.IGTModHelper;

@Mod(modid = GTVeinLocator.MODID, name = GTVeinLocator.MODNAME, version = GTVeinLocator.VERSION, dependencies = "required-after:gregtech", acceptableRemoteVersions = "*")
public class GTVeinLocator {
    public static final String MODID = "xray";
    public static final String MODNAME = "GT Vein-Locator Cheat";
    public static final String VERSION = "v1.0.7";
    public static IGTModHelper gtModHelper;

    // default is GT5, it will become "GT6" when GT6 is installed
    public static String GTVersion = "GT5";

    @SidedProxy(clientSide = "pers.gwyog.gtveinlocator.proxies.ClientProxy", serverSide = "pers.gwyog.gtveinlocator.proxies.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance(MODID)
    public static GTVeinLocator instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        initGTHelper();
        this.proxy.preInit(e);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        this.proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        this.proxy.postInit(e);
    }

    @EventHandler
    public void onServerStart(FMLServerStartedEvent e) {
        this.proxy.onServerStart(e);
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent e) {
        this.proxy.onServerStarting(e);
    }

    public void initGTHelper() {
        try {
            Class clazzGT6API = Class.forName("gregapi.GT_API");
            GTVersion = "GT6";
        } catch (ClassNotFoundException e) {
        }
        gtModHelper = GTVersion.equals("GT6") ? new GT6ModHelper() : new GT5ModHelper();
        gtModHelper = new GT5ModHelper();
    }

}