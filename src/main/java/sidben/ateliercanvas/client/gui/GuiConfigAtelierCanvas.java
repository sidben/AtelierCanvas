package sidben.ateliercanvas.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import sidben.ateliercanvas.client.config.CustomPaintingCategoryEntry;
import sidben.ateliercanvas.client.config.ImportPaintingCategoryEntry;
import sidben.ateliercanvas.client.config.SimpleCategoryElement;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.helper.LocalizationHelper;
import sidben.ateliercanvas.helper.LocalizationHelper.Category;
import sidben.ateliercanvas.reference.Reference;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class GuiConfigAtelierCanvas extends GuiConfig
{


    public GuiConfigAtelierCanvas(GuiScreen guiScreen) {
        super(guiScreen, getConfigElements(), Reference.ModID, false, false, Reference.ModName);
    }


    @SuppressWarnings({ "rawtypes" })
    private static List<IConfigElement> getConfigElements()
    {
        final List<IConfigElement> list = new ArrayList<IConfigElement>();


        // TODO: possible refactor - pass the painting config here, instead of making direct calls to ConfigurationHandler in the GuiScreenCustomPaintingsManage (initGui)
        // Paintings installed
        list.add(new SimpleCategoryElement(ConfigurationHandler.CATEGORY_PAINTINGS, LocalizationHelper.getLanguageKey(Category.CONFIG, "manage_paintings"), CustomPaintingCategoryEntry.class));

        // Paintings import / export
        list.add(new SimpleCategoryElement(ConfigurationHandler.CATEGORY_IMPORT, LocalizationHelper.getLanguageKey(Category.CONFIG, "import_painting"), ImportPaintingCategoryEntry.class));
        list.add(new SimpleCategoryElement(ConfigurationHandler.CATEGORY_EXPORT, LocalizationHelper.getLanguageKey(Category.CONFIG, "export_painting"), null));


        // General config
        final List<IConfigElement> generalConfigs = new ArrayList<IConfigElement>();
        final ConfigCategory generalCat = ConfigurationHandler.config.getCategory(Configuration.CATEGORY_GENERAL);

        generalConfigs.add(new ConfigElement(generalCat.get("max_filesize_kb").setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class)));
        generalConfigs.add(new ConfigElement(generalCat.get("max_image_size").setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class)));
        generalConfigs.add(new ConfigElement(generalCat.get("simple_recipes")));
        generalConfigs.add(new ConfigElement(generalCat.get("date_format")));

        list.addAll(generalConfigs);



        return list;
    }



}
