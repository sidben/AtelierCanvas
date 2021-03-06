package sidben.ateliercanvas.client.gui;

import static sidben.ateliercanvas.reference.TextFormatTable.GLYPH_SORT;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.handler.CustomPaintingConfigItemComparator;
import sidben.ateliercanvas.handler.CustomPaintingConfigItemComparator.SortingType;
import sidben.ateliercanvas.helper.LocalizationHelper;
import sidben.ateliercanvas.helper.LocalizationHelper.Category;
import sidben.ateliercanvas.helper.MouseHelper;
import sidben.ateliercanvas.reference.ColorTable;
import sidben.ateliercanvas.reference.TextFormatTable;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
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
public class GuiScreenCustomPaintingsManage extends GuiScreen
{

    private static final int                  BT_ID_DONE            = 1;
    private static final int                  BT_ID_CHANGE          = 3;
    private static final int                  BT_ID_REMOVE          = 4;
    private static final int                  BT_ID_ENABLE          = 5;
    private static final int                  BT_ID_SORT            = 6;

    private static final int                  GUI_REMOVE_RETURNCODE = -2;              // Must be negative value
    private static final int                  GUI_EDIT_RETURNCODE   = -7;              // Must be negative value


    public final GuiConfig                    parentScreen;
    public final boolean                      isWorldRunning;

    private List<GuiElementPaintingListEntry> paintingList;
    private GuiElementPaintingList            guiPaintingList;
    private GuiElementPaintingDetails         guiElementPaintingDetails;
    private GuiScreenConfirm                  guiConfirmed;
    private GuiScreenCustomPaintingsEditor    guiEditor;
    private GuiButton                         btEdit;
    private GuiButton                         btRemove;
    private GuiButton                         btEnable;
    private GuiButton[]                       btsEditor;
    private GuiButton                         btSorting;
    private int                               selectedIndex         = -1;
    private SortingType                       listSorting           = SortingType.SIZE;



    public GuiScreenCustomPaintingsManage(GuiConfig parentScreen) {
        this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.isWorldRunning = mc.theWorld != null;
        this.paintingList = null;
    }



    @Override
    @SuppressWarnings({ "unchecked" })
    public void initGui()
    {


        // Buttons
        final int buttonWidth = 66;
        final int secondColumnX = this.width / 2 + 4;
        final int buttonStartY = 100;
        final int buttonMargin = 1;

        btEdit = new GuiButton(BT_ID_CHANGE, secondColumnX, buttonStartY, LocalizationHelper.translate(Category.CONFIG, "edit"));
        btRemove = new GuiButton(BT_ID_REMOVE, secondColumnX, buttonStartY, LocalizationHelper.translate(Category.CONFIG, "remove"));
        btEnable = new GuiButton(BT_ID_ENABLE, secondColumnX, buttonStartY, "---");

        btsEditor = new GuiButton[] { btEdit, btRemove, btEnable };


        btSorting = new GuiUnicodeGlyphButton(BT_ID_SORT, this.width / 2 - 4 - 200, this.height - 48, 70, 20, " " + LocalizationHelper.translate(Category.CONFIG, "sort"), GLYPH_SORT, 2.0F);

        this.buttonList.add(new GuiButton(BT_ID_DONE, this.width / 2 - 100, this.height - 48, StatCollector.translateToLocal("gui.done")));
        this.buttonList.add(btSorting);


        // Resize and repositions the editor buttons
        for (int i = 0; i < btsEditor.length; i++) {
            this.buttonList.add(btsEditor[i]);
            btsEditor[i].xPosition = secondColumnX + (buttonWidth * (i)) + (buttonMargin * (i));
            btsEditor[i].width = buttonWidth;
        }

        this.refreshListbox();
        this.displayDetailsButtons(false);


        // Paintings details screen
        this.guiElementPaintingDetails = new GuiElementPaintingDetails(this, null);
        this.displayDetails(this.selectedIndex);

        // Confirmation screen (removal)
        this.guiConfirmed = new GuiScreenConfirm(this, "Are you sure?", -2);

        // Editor
        this.guiEditor = null;
    }



    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // Clear the background
        this.drawBackground(0);

        // If the GUI is large, allows more info and a bigger picture
        if (this.height > 320) {
            for (final GuiButton button : this.btsEditor) {
                button.yPosition = 164;
            }
        }


        // Selected painting extra info
        if (this.selectedIndex > -1) {
            this.guiElementPaintingDetails.drawScreen(mouseX, mouseY, partialTicks);
        }

        // Draws the listbox
        this.guiPaintingList.drawScreen(mouseX, mouseY, partialTicks);

        // Texts - Title, Total paintings installed
        this.drawCenteredString(this.fontRendererObj, LocalizationHelper.translate(Category.CONFIG, "title"), this.width / 2, 8, ColorTable.WHITE);
        this.drawCenteredString(this.fontRendererObj, LocalizationHelper.translate(Category.CONFIG, "manage_paintings"), this.width / 2, 18, ColorTable.WHITE);
        this.drawCenteredString(this.fontRendererObj, LocalizationHelper.translateFormatted(Category.CONFIG, "installed_counter", this.paintingList.size()), this.width / 2, this.height - 20,
                ColorTable.GRAY);


        // Parent call (draws buttons)
        super.drawScreen(mouseX, mouseY, partialTicks);


        // Tooltips (OBS: this must come after [super.drawScreen], or else the buttons will get a weird gray overlay
        if (MouseHelper.isMouseInside(mouseX, mouseY, btSorting)) {
            // Sorting button tooltip
            if (this.listSorting == SortingType.AUTHOR) {
                this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(LocalizationHelper.translate(Category.CONFIG, "sort_by_author"), 300), mouseX, mouseY);

            } else if (this.listSorting == SortingType.TITLE) {
                this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(LocalizationHelper.translate(Category.CONFIG, "sort_by_title"), 300), mouseX, mouseY);

            } else if (this.listSorting == SortingType.SIZE) {
                this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(LocalizationHelper.translate(Category.CONFIG, "sort_by_size"), 300), mouseX, mouseY);

            }

        } else if (!this.guiPaintingList.getTooltip().isEmpty()) {
            this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(this.guiPaintingList.getTooltip(), 300), mouseX, mouseY);

        } else if (!this.guiElementPaintingDetails.getTooltip().isEmpty()) {
            this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(this.guiElementPaintingDetails.getTooltip(), 300), mouseX, mouseY);

        }

    }



    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled) {

            if (button.id == BT_ID_DONE) {
                // Saves config (if needed) and return



                // Identifies and update every element that changed
                boolean elementChanged = false;
                for (final GuiElementPaintingListEntry item : this.paintingList) {
                    if (item.changed()) {
                        // An item was changed, update the config
                        elementChanged = true;
                        ConfigurationHandler.addOrUpdateEntry(item.getConfigItem());
                    } else if (item.removed()) {
                        // An item was removed, update the config
                        elementChanged = true;
                        ConfigurationHandler.removeEntry(item.getConfigItem().getUUID());
                    }
                }


                // Save the changes to the config file, if anything changed
                if (elementChanged) {
                    ConfigurationHandler.updateAndSaveConfig();
                }



                // "Resets" the item list and selected item
                this.selectedIndex = -1;
                this.paintingList = null;


                // Returns to the parent screen
                this.mc.displayGuiScreen(this.parentScreen);


            }



            else if (button.id == BT_ID_CHANGE) {
                // Open editor for current painting
                if (this.selectedIndex >= 0 && this.selectedIndex < this.paintingList.size()) {
                    final GuiElementPaintingListEntry entry = this.paintingList.get(this.selectedIndex);
                    if (entry != null) {
                        this.guiEditor = new GuiScreenCustomPaintingsEditor(this, entry.getConfigItem(), GUI_EDIT_RETURNCODE);
                        this.mc.displayGuiScreen(guiEditor);
                    }
                }

            }



            else if (button.id == BT_ID_ENABLE) {
                // Enable / Disable painting
                if (this.selectedIndex >= 0 && this.selectedIndex < this.paintingList.size()) {
                    final GuiElementPaintingListEntry entry = this.paintingList.get(this.selectedIndex);
                    if (entry != null) {
                        // Changes the config
                        entry.swapIsEnabled();

                        // Update the button display
                        btEnable.displayString = LocalizationHelper.translate(Category.CONFIG, (entry.getConfigItem().getIsEnabled() ? "enabled" : "disabled"));
                        if (!entry.getConfigItem().getIsEnabled()) {
                            btEnable.displayString = TextFormatTable.COLOR_GRAY + btEnable.displayString;
                        }


                    }

                }

            }



            else if (button.id == BT_ID_REMOVE) {
                // Removes the selected painting from the config
                this.mc.displayGuiScreen(guiConfirmed);

            }



            else if (button.id == BT_ID_SORT) {
                // Changes the list sorting and refresh the GUI
                if (this.listSorting == SortingType.AUTHOR) {
                    this.listSorting = SortingType.TITLE;
                } else if (this.listSorting == SortingType.TITLE) {
                    this.listSorting = SortingType.SIZE;
                } else {
                    this.listSorting = SortingType.AUTHOR;
                }

                this.selectedIndex = -1;
                this.paintingList = null;
                this.displayDetails(this.selectedIndex);
                this.refreshListbox();

            }

        }

    }



    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
    public void onGuiClosed()
    {
        /*
         * this.entryList.onGuiClosed();
         * 
         * if (this.configID != null && this.parentScreen instanceof GuiConfig)
         * {
         * GuiConfig parentGuiConfig = (GuiConfig) this.parentScreen;
         * parentGuiConfig.needsRefresh = true;
         * parentGuiConfig.initGui();
         * }
         * 
         * if (!(this.parentScreen instanceof GuiConfig))
         * Keyboard.enableRepeatEvents(false);
         */
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



    @Override
    public void confirmClicked(boolean result, int index)
    {
        // OBS: this event is fired by the GuiElementPaintingList when an item is selected
        // OBS 2: this event is also fired by the confirmation screen (remove painting). For that reason,
        // the confirmation screen pass a unique, negative index.
        if (index == GuiScreenCustomPaintingsManage.GUI_REMOVE_RETURNCODE) {

            // removes the selected index
            if (result && this.selectedIndex >= 0 && this.selectedIndex < this.paintingList.size()) {
                final GuiElementPaintingListEntry entry = this.paintingList.get(this.selectedIndex);

                // Marks the element as removed
                entry.setRemoved();
                this.selectedIndex = -1;
                this.displayDetailsButtons(false);

            }

            this.mc.displayGuiScreen(this);

        }

        // Return from the editor, updates the entry
        else if (index == GuiScreenCustomPaintingsManage.GUI_EDIT_RETURNCODE) {

            if (result && this.selectedIndex >= 0 && this.selectedIndex < this.paintingList.size()) {
                final GuiElementPaintingListEntry entry = this.paintingList.get(this.selectedIndex);

                // Updates the entry
                entry.setPaintingInfo(this.guiEditor.getPaintingName(), this.guiEditor.getPaintingAuthor());
            }

            this.mc.displayGuiScreen(this);

        }

        else {
            // Item selection
            if (result) {
                this.selectedIndex = -1;
                this.displayDetailsButtons(false);
                this.displayDetails(index);
                this.selectedIndex = index;
            }

        }
    }


    protected void displayDetailsButtons(boolean visible)
    {
        for (final GuiButton button : this.btsEditor) {
            button.visible = visible;
        }
    }


    /**
     * Displays the GUI element with the image details and thumbnail.
     * 
     * @param index
     *            Index of the element on the listbox.
     */
    public void displayDetails(int index)
    {
        if (index >= 0 && index < this.paintingList.size()) {
            final GuiElementPaintingListEntry entry = this.paintingList.get(index);
            this.guiElementPaintingDetails.updateConfigItem(entry.getConfigItem());

            btEnable.displayString = LocalizationHelper.translate(Category.CONFIG, (entry.getConfigItem().getIsEnabled() ? "enabled" : "disabled"));
            if (!entry.getConfigItem().getIsEnabled()) {
                btEnable.displayString = TextFormatTable.COLOR_GRAY + btEnable.displayString;
            }

            this.displayDetailsButtons(true);
        }
    }



    private void refreshListbox()
    {
        if (this.paintingList == null) {
            this.loadConfigValues();
        }

        // Paintings listbox
        this.guiPaintingList = new GuiElementPaintingList(this.mc, 200, this.height, this.paintingList, this);
        this.guiPaintingList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.guiPaintingList.registerScrollButtons(7, 8);
    }



    /**
     * Loads the config entries on the listbox array.
     */
    private void loadConfigValues()
    {

        // Sorting
        final List<CustomPaintingConfigItem> configList = ConfigurationHandler.getAllMahGoodPaintings();
        Collections.sort(configList, new CustomPaintingConfigItemComparator(listSorting));

        // Listbox data (loads from config)
        this.paintingList = new ArrayList<GuiElementPaintingListEntry>();
        for (final CustomPaintingConfigItem item : configList) {
            this.paintingList.add(new GuiElementPaintingListEntry(this, item));
        }

    }

}
