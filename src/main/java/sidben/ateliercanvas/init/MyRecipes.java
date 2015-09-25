package sidben.ateliercanvas.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class MyRecipes
{

    
    static final String OREDIC_PAINTING = "materialPainting";
    
    
    
    
    
    
    public static void registerRecipes() 
    {
        
        // Register required ore dictionary entries
        registerOreDictionary();
        
        

        final ItemStack enderPearlStack = new ItemStack(Items.ender_pearl, 1);

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
            GameRegistry.addRecipe(new ShapelessOreRecipe(randomPaintingStack, OREDIC_PAINTING));
        } else {
            GameRegistry.addRecipe(new ShapedOreRecipe(randomPaintingStack, "ppp", "pep", "ppp", 'e', enderPearlStack, 'p', OREDIC_PAINTING));
        }
    
    }

    
    
    
    /**
     * Add required entries to Forge OreDictionary.
     */
    private static void registerOreDictionary() {
        OreDictionary.registerOre(OREDIC_PAINTING, Items.painting);
        OreDictionary.registerOre(OREDIC_PAINTING, MyItems.customPainting);
    }

    
}
