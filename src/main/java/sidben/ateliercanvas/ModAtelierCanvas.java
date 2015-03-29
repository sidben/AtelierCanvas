package sidben.ateliercanvas;

import sidben.ateliercanvas.reference.Reference;
import sidben.ateliercanvas.commands.CommandAtelier;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.proxy.IProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;



@Mod(modid = Reference.ModID, name = Reference.ModName, version = Reference.ModVersion)
public class ModAtelierCanvas
{

    
    // The instance of your mod that Forge uses.
    @Mod.Instance(Reference.ModID)
    public static ModAtelierCanvas   instance;

    
    @SidedProxy(clientSide = Reference.ClientProxyClass, serverSide = Reference.ServerProxyClass)
    public static IProxy      proxy;

  
    
    
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LogHelper.info("=================PREINIT========================");
        
        
        // Sided pre-initialization
        proxy.pre_initialize();
    }


    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        LogHelper.info("=================LOAD========================");
        
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
