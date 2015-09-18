package sidben.ateliercanvas;

import net.minecraftforge.common.MinecraftForge;
import sidben.ateliercanvas.reference.Reference;
import sidben.ateliercanvas.command.CommandAtelier;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.proxy.IProxy;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.PlayerEventHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;



@Mod(modid = Reference.ModID, name = Reference.ModName, version = Reference.ModVersion, guiFactory = Reference.GuiFactoryClass)
public class ModAtelierCanvas
{

    
    // The instance of your mod that Forge uses.
    @Mod.Instance(Reference.ModID)
    public static ModAtelierCanvas   instance;

    
    @SidedProxy(clientSide = Reference.ClientProxyClass, serverSide = Reference.ServerProxyClass)
    public static IProxy      proxy;


    // Used to send information between client / server
    public static SimpleNetworkWrapper NetworkWrapper;

    
    
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LogHelper.info("=================PREINIT========================");
        
        // Loads config
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());

        
        // Sided pre-initialization
        proxy.pre_initialize();
    }


    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        LogHelper.info("=================LOAD========================");
        
        // Event Handlers
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());

        
        // Sided initializations
        proxy.initialize();
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        LogHelper.info("=================POSTINIT========================");
        
        
        
        
        // Sided post-initialization
        proxy.post_initialize();
    }

    
    
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        // register custom commands
        event.registerServerCommand(new CommandAtelier());
    }

    
}
