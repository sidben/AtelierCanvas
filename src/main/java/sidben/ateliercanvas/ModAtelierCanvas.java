package sidben.ateliercanvas;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import sidben.ateliercanvas.command.CommandAtelier;
import sidben.ateliercanvas.creativetab.AtelierCanvasCreativeTabs;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.PlayerEventHandler;
import sidben.ateliercanvas.helper.AtelierHelper;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.proxy.IProxy;
import sidben.ateliercanvas.reference.Reference;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * Atelier Canvas mod main class.
 * 
 * @author sidben
 */
@Mod(modid = Reference.ModID, name = Reference.ModName, version = Reference.ModVersion, guiFactory = Reference.GuiFactoryClass)
public class ModAtelierCanvas
{


    // The instance of your mod that Forge uses.
    @Mod.Instance(Reference.ModID)
    public static ModAtelierCanvas     instance;


    @SidedProxy(clientSide = Reference.ClientProxyClass, serverSide = Reference.ServerProxyClass)
    public static IProxy               proxy;


    // Used to send information between client / server
    public static SimpleNetworkWrapper NetworkWrapper;

    
    // Creative Tabs
    public static CreativeTabs tabMod = new AtelierCanvasCreativeTabs();

    

    // Helpers
    @SideOnly(Side.CLIENT)
    private AtelierHelper              paintingsFileHelper;



    /**
     * Returns a singleton instance of the music helper class.
     */
    @SideOnly(Side.CLIENT)
    public AtelierHelper getAtelierHelper()
    {
        return paintingsFileHelper;
    }

    @SideOnly(Side.CLIENT)
    public void setAtelierHelper(AtelierHelper helper)
    {
        paintingsFileHelper = helper;
    }



    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // Loads config
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());


        // Sided pre-initialization
        proxy.pre_initialize();
    }


    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        // Event Handlers
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());


        // Sided initializations
        proxy.initialize();
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        // Sided post-initialization
        proxy.post_initialize();
    }



    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        // register custom commands
        event.registerServerCommand(new CommandAtelier());
    }


}
