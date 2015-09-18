package sidben.ateliercanvas.client.config;

import sidben.ateliercanvas.client.gui.GuiScreenCustomPaintings;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.config.ConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class CustomPaintingCategoryEntry extends CategoryEntry
{

    @SuppressWarnings("rawtypes")
    public CustomPaintingCategoryEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
    {
        super(owningScreen, owningEntryList, prop);
    }

    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected GuiScreen buildChildScreen()
    {
        
        // TODO: make it GuiConfig
        return new GuiScreenCustomPaintings();
        
        /*
        // This GuiConfig object specifies the configID of the object and as such will force-save when it is closed. The parent
        // GuiConfig object's entryList will also be refreshed to reflect the changes.
        return new GuiConfig(this.owningScreen, 
                (new ConfigElement(ForgeModContainer.getConfig().getCategory(ConfigurationHandler.CATEGORY_PAINTINGS))).getChildElements(), 
                this.owningScreen.modID, ConfigurationHandler.CATEGORY_PAINTINGS, this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart, 
                this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                GuiConfig.getAbridgedConfigPath(ForgeModContainer.getConfig().toString()));
        */
    }
    
}
