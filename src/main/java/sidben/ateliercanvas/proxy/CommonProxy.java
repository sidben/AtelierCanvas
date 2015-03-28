package sidben.ateliercanvas.proxy;

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
        // Recipes

        
        // Achievements

        
        // Event Handlers
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
