package sidben.ateliercanvas.client.config;

import net.minecraft.client.gui.GuiScreen;
import sidben.ateliercanvas.client.gui.GuiScreenCustomPaintings;
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
    public CustomPaintingCategoryEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
        super(owningScreen, owningEntryList, prop);
    }


    @Override
    protected GuiScreen buildChildScreen()
    {
        return new GuiScreenCustomPaintings(this.owningScreen);
    }

}
