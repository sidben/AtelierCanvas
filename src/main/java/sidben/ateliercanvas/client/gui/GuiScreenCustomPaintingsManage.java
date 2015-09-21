package sidben.ateliercanvas.client.gui;

import static sidben.ateliercanvas.reference.TextFormatTable.GLYPH_BACK;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Property;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.reference.ColorTable;
import sidben.ateliercanvas.reference.Reference;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * <p>
 * Screen where the player can manage all custom paintings installed.
 * </p>
 * 
 * <p>
 * A custom painting is considered 'installed' when the entry is added to the mod config file, so it's not enough to just add PNG files to the mod config folder. A player will need to use this GUI to notify the mod of the images and also provide more info, like painting name or author.
 * </p>
 * 
 * <p>
 * This GUI was created using the ResourcePack selector GUI as reference.
 * </p>
 * 
 * 
 * @see sidben.ateliercanvas.client.gui.GuiElementPaintingList
 * @see sidben.ateliercanvas.client.gui.GuiElementPaintingListEntry
 * @see net.minecraft.client.gui.GuiScreenResourcePacks
 * @author sidben
 * 
 */
@SideOnly(Side.CLIENT)
public class GuiScreenCustomPaintingsManage extends GuiScreen implements IListContainer
{

    private static final int                  BT_ID_BACK    = 1;
    private static final int                  BT_ID_ADDNEW  = 2;
    private static final int                  BT_ID_CHANGE  = 3;
    private static final int                  BT_ID_REMOVE  = 4;
    private static final int                  BT_ID_ENABLE  = 5;


    public final GuiConfig                    parentScreen;
    public final boolean                      isWorldRunning;

    private List<GuiElementPaintingListEntry> paintingList;
    private GuiElementPaintingList            guiPaintingList;
    private GuiElementPaintingDetails         guiElementPaintingDetails;
    private int                               selectedIndex = -1;



    public GuiScreenCustomPaintingsManage(GuiConfig parentScreen) {
        this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.isWorldRunning = mc.theWorld != null;
    }



    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void initGui()
    {
        // Buttons
        final int buttonWidth = 66;
        final int secondColumnX = this.width / 2 + 4;
        final int buttonStartY = 100;
        final int buttonMargin = 1;

        this.buttonList.add(new GuiOptionButton(BT_ID_ADDNEW, this.width / 2 - 154, this.height - 48, StatCollector.translateToLocal(getLanguageKey("add_new"))));
        this.buttonList.add(new GuiUnicodeGlyphButton(BT_ID_BACK, secondColumnX, this.height - 48, 150, 20, " " + StatCollector.translateToLocal("gui.back"), GLYPH_BACK, 2.0F));

        this.buttonList.add(new GuiButton(BT_ID_CHANGE, secondColumnX, buttonStartY, StatCollector.translateToLocal(getLanguageKey("edit"))));
        this.buttonList.add(new GuiButton(BT_ID_REMOVE, secondColumnX, buttonStartY, StatCollector.translateToLocal(getLanguageKey("remove"))));
        this.buttonList.add(new GuiButton(BT_ID_ENABLE, secondColumnX, buttonStartY, "---"));

        for (int i = 2; i < 5; i++) {
            ((GuiButton) this.buttonList.get(i)).xPosition = secondColumnX + (buttonWidth * (i - 2)) + (buttonMargin * (i - 2));
            ((GuiButton) this.buttonList.get(i)).width = buttonWidth;
        }

        this.displayDetailsButtons(false);

        // TODO: remove when they are implemented
        ((GuiButton) this.buttonList.get(0)).enabled = false;
        ((GuiButton) this.buttonList.get(3)).enabled = false;
        ((GuiButton) this.buttonList.get(4)).enabled = false;



        // Listbox data (loads from config)
        this.paintingList = new ArrayList();
        for (final CustomPaintingConfigItem item : ConfigurationHandler.mahPaintings) {
            this.paintingList.add(new GuiElementPaintingListEntry(this, item));
        }

        // Paintings listbox
        this.guiPaintingList = new GuiElementPaintingList(this.mc, 200, this.height, this.paintingList, this);
        this.guiPaintingList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.guiPaintingList.registerScrollButtons(7, 8);

        // Paintings details screen
        this.guiElementPaintingDetails = new GuiElementPaintingDetails(this, null);
    }



    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // Clear the background
        this.drawBackground(0);

        // If the GUI is large, allows more info and a bigger picture
        if (this.height > 320) {
            for (int i = 2; i < 5; i++) {
                ((GuiButton) this.buttonList.get(i)).yPosition = 164;
            }
        }


        // Selected painting extra info
        if (this.selectedIndex > -1) {
            this.guiElementPaintingDetails.drawScreen(mouseX, mouseY, partialTicks);
        }

        // Draws the listbox
        this.guiPaintingList.drawScreen(mouseX, mouseY, partialTicks);

        // Texts - Title, Total paintings installed
        this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal(getLanguageKey("title")), this.width / 2, 8, ColorTable.WHITE);
        this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal(getLanguageKey("manage_paintings")), this.width / 2, 18, ColorTable.WHITE);
        this.drawCenteredString(this.fontRendererObj, String.format(StatCollector.translateToLocal(getLanguageKey("installed_counter")), this.paintingList.size()), this.width / 2, this.height - 20,
                ColorTable.GRAY);


        // Parent call (draws buttons)
        super.drawScreen(mouseX, mouseY, partialTicks);


        // Tooltips (OBS: this must come after [super.drawScreen], or else the buttons will get a weird gray overlay
        if (!this.guiPaintingList.getTooltip().isEmpty()) {
            this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(this.guiPaintingList.getTooltip(), 300), mouseX, mouseY);
        } else if (!this.guiElementPaintingDetails.getTooltip().isEmpty()) {
            this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(this.guiElementPaintingDetails.getTooltip(), 300), mouseX, mouseY);
        }

    }



    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled) {

            if (button.id == BT_ID_BACK) {
                final String configID = ConfigurationHandler.CATEGORY_PAINTINGS;
                final boolean requiresMcRestart = false;

                // Fires the related event. I don't use this, but I'm following GuiConfig code. Maybe Forge needs this info)
                final ConfigChangedEvent event = new OnConfigChangedEvent(Reference.ModID, configID, isWorldRunning, requiresMcRestart);
                FMLCommonHandler.instance().bus().post(event);
                if (!event.getResult().equals(Result.DENY)) {
                    FMLCommonHandler.instance().bus().post(new PostConfigChangedEvent(Reference.ModID, configID, isWorldRunning, requiresMcRestart));
                }



                // Clear all content of the category
                ConfigurationHandler.config.getCategory(configID).clear();

                // Re-adds all the valid entries
                Property configProp;
                int c = 0;

                for (final GuiElementPaintingListEntry item : this.paintingList) {
                    final String configKey = String.format("%s_%03d", ConfigurationHandler.PAINTINGS_ARRAY_KEY, c);

                    configProp = ConfigurationHandler.config.get(configID, configKey, new String[] {});
                    configProp.set(item._entryData.ToStringArray());

                    c++;
                }

                // Saves the config file
                ConfigurationHandler.config.save();     // TODO: Only save if there were changes (?)



                // Returns to the parent screen
                this.mc.displayGuiScreen(this.parentScreen);


            } else if (button.id == BT_ID_ADDNEW) {
                this.mc.displayGuiScreen(new GuiScreenCustomPaintingsAdd(this));

            }

        }
    }



    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int clickType)
    {
        super.mouseClicked(mouseX, mouseY, clickType);
        this.guiPaintingList.func_148179_a(mouseX, mouseY, clickType);
    }


    /**
     * Called when the mouse is moved or a mouse button is released. Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int which)
    {
        super.mouseMovedOrUp(mouseX, mouseY, which);
    }



    @SuppressWarnings("rawtypes")
    public void drawToolTip(List stringList, int x, int y)
    {
        this.parentScreen.drawToolTip(stringList, x, y);
    }



    protected void displayDetailsButtons(boolean visible)
    {
        for (int i = 2; i < 5; i++) {
            ((GuiButton) this.buttonList.get(i)).visible = visible;
        }
    }




    /**
     * Returns the full language key for elements of this GUI.
     */
    protected String getLanguageKey(String name)
    {
        return "sidben.ateliercanvas.config." + name;
    }



    @Override
    public void onItemSelected(GuiElementPaintingList list, int index)
    {
        this.selectedIndex = -1;
        this.displayDetailsButtons(false);

        if (index >= 0 && index < this.paintingList.size()) {
            final GuiElementPaintingListEntry entry = this.paintingList.get(index);
            this.guiElementPaintingDetails.updateConfigItem(entry._entryData);
            this.selectedIndex = index;

            ((GuiButton) this.buttonList.get(4)).displayString = entry._entryData.getIsEnabled() ? StatCollector.translateToLocal(getLanguageKey("enabled")) : StatCollector
                    .translateToLocal(getLanguageKey("disabled"));

            this.displayDetailsButtons(true);
        }
    }

}
