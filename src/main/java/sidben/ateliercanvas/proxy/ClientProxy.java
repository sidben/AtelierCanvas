package sidben.ateliercanvas.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import sidben.ateliercanvas.init.MyItems;
import sidben.ateliercanvas.reference.Reference;



public class ClientProxy extends CommonProxy
{


    @Override
    public void pre_initialize()
    {
        // Block and item textures
        MyItems.registerRender();

        // Special renderers

        
        super.pre_initialize();
    }



    @Override
    public void initialize()
    {
    }


    // returns an instance of the GUI
    @Override
    public Object getClientGuiElement(int guiID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }




    private String getResourceName(String name)
    {
        return Reference.ModID + ":" + name;
    }
    
}
