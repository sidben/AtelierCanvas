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

    // Icons
    public static String                   customPaintingIcon;
    public static String                   customPaintingShinyIcon;
    public static String                   randomPaintingIcon;



    public static void register()
    {
        GameRegistry.registerItem(customPainting, "custom_painting");
        GameRegistry.registerItem(randomPainting, "random_painting");
    }



    @SideOnly(Side.CLIENT)
    public static void registerRender()
    {
        MyItems.customPaintingIcon = "ateliercanvas:painting_custom";
        MyItems.customPaintingShinyIcon = "ateliercanvas:painting_custom_shiny";
        MyItems.randomPaintingIcon = "ateliercanvas:painting_random";
    }


}
