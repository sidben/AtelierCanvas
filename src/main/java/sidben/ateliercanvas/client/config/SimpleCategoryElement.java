package sidben.ateliercanvas.client.config;

import java.util.ArrayList;
import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;


/**
 * Simple class for a category button that uses a custom CategoryEntry, 
 * when the config requires some custom behavior. 
 * 
 * @author sidben
 */
public class SimpleCategoryElement extends DummyConfigElement<Object>
{
    
    @SuppressWarnings("rawtypes")
    public SimpleCategoryElement(String name, String langKey, Class<? extends IConfigEntry> customListEntryClass)
    {
        super(name, null, ConfigGuiType.CONFIG_CATEGORY, langKey);
        super.childElements = new ArrayList<IConfigElement>();
        super.configEntryClass = customListEntryClass;
        super.isProperty = false;
    }

}
