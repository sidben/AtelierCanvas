package sidben.ateliercanvas.proxy;

import cpw.mods.fml.common.registry.EntityRegistry;
import sidben.ateliercanvas.ModAtelierCanvas;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.init.MyItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;


/*
 * Base proxy class, here I initialize everything that must happen on both, server and client.
 * 
 */
public abstract class CommonProxy implements IProxy
{

    

    @Override
    public void pre_initialize()
    {
        // Register network messages

        // Register blocks
        
        // Register items
        MyItems.register();
    }


    @Override
    public void initialize()
    {
        LogHelper.info("=================LOAD (common proxy)========================");
        
        
        // Recipes

        
        // Achievements

        
        // Event Handlers
        
        
        // Entities
        LogHelper.info("-- Registering custom painting entity");
        EntityRegistry.registerModEntity(EntityCustomPainting.class, "CustomPainting", 0, ModAtelierCanvas.instance, 10, 3, false);
    }


    @Override
    public void post_initialize()
    {
    }


    @Override
    public Object getServerGuiElement(int guiID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    
    @Override
    public Object getClientGuiElement(int guiID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }


}
