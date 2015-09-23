package sidben.ateliercanvas.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import sidben.ateliercanvas.handler.ConfigurationHandler;
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
    public static String customPaintingIcon;
    public static String randomPaintingIcon;



    
    public static void register()
    {
        GameRegistry.registerItem(customPainting, "custom_painting");
        GameRegistry.registerItem(randomPainting, "random_painting");
    }
    
    
    
    public static void registerRecipes() 
    {

        final ItemStack enderPearlStack = new ItemStack(Items.ender_pearl, 1);
        final ItemStack paintingStack = new ItemStack(Items.painting, 1);
        
        final ItemStack randomPaintingStack = new ItemStack(MyItems.randomPainting, 8);

        
        /*
         * NOTE: 
         * I feel that 1 ender pearl is a good price to pay for the mysterious painting, 
         * that can each give 1 custom painting of any size.
         * 
         * I don't want to make the custom painting too easily available on the first night, 
         * but I also don't want to hide it behind a grind wall. 
         * 
         * For players that play on peaceful mode or simply want to get the custom paintings
         * without hassle, there is a config call "Simple Crafting" that activates a basic
         * 1 painting = 1 mysterious painting recipe.
         */
        
        // Mysterious Painting
        if (ConfigurationHandler.simpleRecipes) {
            GameRegistry.addShapelessRecipe(randomPaintingStack, paintingStack);
        } else {
            GameRegistry.addRecipe(randomPaintingStack, "ppp", "pep", "ppp", 'e', enderPearlStack, 'p', paintingStack);
        }
    
    }

    
    
    @SideOnly(Side.CLIENT)
    public static void registerRender()
    {
        MyItems.customPaintingIcon =  "ateliercanvas:" + "painting_custom";  // TODO: create helper class to encapsulate Reference.ResourcesNamespace
        MyItems.randomPaintingIcon =  "ateliercanvas:" + "painting_random";
    }    
    
    
}
