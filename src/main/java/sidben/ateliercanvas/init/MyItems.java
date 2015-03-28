package sidben.ateliercanvas.init;

import sidben.ateliercanvas.item.ItemCustomPainting;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class MyItems
{

    
    public static final ItemCustomPainting customPainting = new ItemCustomPainting();
    

    
    public static void register()
    {
        GameRegistry.registerItem(customPainting, "custom_painting");
    }

    
    @SideOnly(Side.CLIENT)
    public static void registerRender()
    {
    }    
    
    
}
