package sidben.ateliercanvas.client.gui;

import java.util.ArrayList;
import java.util.List;
import sidben.ateliercanvas.client.config.CustomPaintingCategoryEntry;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.reference.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;


public class GuiConfigAtelierCanvas extends GuiConfig
{


    public GuiConfigAtelierCanvas(GuiScreen guiScreen) {
        super(guiScreen, getConfigElements(), Reference.ModID, false, false, GuiConfig.getAbridgedConfigPath(ConfigurationHandler.config.toString()));
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List<IConfigElement> getConfigElements()
    {
        final List<IConfigElement> list = new ArrayList<IConfigElement>();
        List<IConfigElement> paintingsConfigs = new ArrayList<IConfigElement>();


        // Paintings installed
        paintingsConfigs = new ConfigElement(ConfigurationHandler.config.getCategory(ConfigurationHandler.CATEGORY_PAINTINGS)).getChildElements();
        list.add(new DummyConfigElement.DummyCategoryElement(ConfigurationHandler.CATEGORY_PAINTINGS, getLanguageKey("paintings"), paintingsConfigs, CustomPaintingCategoryEntry.class));


        return list;
    }
    
    
    
    /**
     * Returns the full language key for elements of this GUI. 
     */
    protected static String getLanguageKey(String name) {
        return "sidben.ateliercanvas.config.category." + name;
    }


}
