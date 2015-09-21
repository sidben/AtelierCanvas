package sidben.ateliercanvas.init;

import sidben.ateliercanvas.item.ItemCustomPainting;
import sidben.ateliercanvas.item.ItemRandomPainting;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class MyItems
{

    
    public static final ItemCustomPainting customPainting = new ItemCustomPainting();
    public static final ItemRandomPainting randomPainting = new ItemRandomPainting();
    

    
    public static void register()
    {
        GameRegistry.registerItem(customPainting, "custom_painting");
        GameRegistry.registerItem(randomPainting, "random_painting");
    }

    
    @SideOnly(Side.CLIENT)
    public static void registerRender()
    {
    }    
    
    
}
