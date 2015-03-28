package sidben.ateliercanvas.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import sidben.ateliercanvas.client.renderer.RenderCustomPainting;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.init.MyItems;




public class ClientProxy extends CommonProxy
{


    @Override
    public void pre_initialize()
    {
        LogHelper.info("=================PREINIT (client proxy)========================");
        
        super.pre_initialize();
        

        // Block and item textures
        MyItems.registerRender();

        // Special renderers
    }



    @Override
    public void initialize()
    {
        LogHelper.info("=================LOAD (client proxy)========================");
        
        
        super.initialize();
        
        
        // Entity renderer
        RenderingRegistry.registerEntityRenderingHandler(EntityCustomPainting.class, new RenderCustomPainting());

        
    }


    // returns an instance of the GUI
    @Override
    public Object getClientGuiElement(int guiID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }



/*
    private String getResourceName(String name)
    {
        return Reference.ModID + ":" + name;
    }
  */
    
}
