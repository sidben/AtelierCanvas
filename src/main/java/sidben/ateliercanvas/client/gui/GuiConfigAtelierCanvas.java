package sidben.ateliercanvas.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import sidben.ateliercanvas.client.config.CustomPaintingCategoryEntry;
import sidben.ateliercanvas.client.config.ImportPaintingCategoryEntry;
import sidben.ateliercanvas.client.config.SimpleCategoryElement;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.reference.Reference;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class GuiConfigAtelierCanvas extends GuiConfig
{


    public GuiConfigAtelierCanvas(GuiScreen guiScreen) {
        super(guiScreen, getConfigElements(), Reference.ModID, false, false, Reference.ModName);
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List<IConfigElement> getConfigElements()
    {
        final List<IConfigElement> list = new ArrayList<IConfigElement>();
        List<IConfigElement> generalConfigs = new ArrayList<IConfigElement>();


        // TODO: possible refactor - pass the painting config here, instead of making direct calls to ConfigurationHandler in the GuiScreenCustomPaintingsManage (initGui)
        // Paintings installed
        list.add(new SimpleCategoryElement(ConfigurationHandler.CATEGORY_PAINTINGS, getLanguageKey("manage_paintings"), CustomPaintingCategoryEntry.class));

        // Paintings import / export
        list.add(new SimpleCategoryElement(ConfigurationHandler.CATEGORY_IMPORT, getLanguageKey("import_painting"), ImportPaintingCategoryEntry.class));
        list.add(new SimpleCategoryElement(ConfigurationHandler.CATEGORY_EXPORT, getLanguageKey("export_painting"), null));

        // General config
        generalConfigs = new ConfigElement(ConfigurationHandler.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements();
        list.addAll(generalConfigs);



        return list;
    }



    /**
     * Returns the full language key for elements of this GUI.
     */
    protected static String getLanguageKey(String name)
    {
        return "sidben.ateliercanvas.config." + name;
    }


}
