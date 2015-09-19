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


public class ModGuiConfig extends GuiConfig
{


    public ModGuiConfig(GuiScreen guiScreen) {
        super(guiScreen, getConfigElements(), Reference.ModID, false, false, GuiConfig.getAbridgedConfigPath(ConfigurationHandler.config.toString()));
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List<IConfigElement> getConfigElements()
    {
        final List<IConfigElement> list = new ArrayList<IConfigElement>();
        List<IConfigElement> paintingsConfigs = new ArrayList<IConfigElement>();


        // Paintings installed
        paintingsConfigs = new ConfigElement(ConfigurationHandler.config.getCategory(ConfigurationHandler.CATEGORY_PAINTINGS)).getChildElements();
        list.add(new DummyConfigElement.DummyCategoryElement(ConfigurationHandler.CATEGORY_PAINTINGS, "sidben.ateliercanvas.config.category.paintings", paintingsConfigs, CustomPaintingCategoryEntry.class));


        return list;
    }
    
}
