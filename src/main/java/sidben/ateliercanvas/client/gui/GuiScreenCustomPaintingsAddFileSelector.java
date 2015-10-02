package sidben.ateliercanvas.client.gui;

import static sidben.ateliercanvas.reference.TextFormatTable.GLYPH_BACK;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Util;
import org.lwjgl.Sys;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.helper.ImageFilenameFilter;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.helper.MouseHelper;
import sidben.ateliercanvas.reference.ColorTable;
import sidben.ateliercanvas.reference.TextFormatTable;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * <p>
 * This is the GUI where the player can select which image file they want to import to the mod.
 * </p>
 * <p>
 * A listbox on the left will display every image file in the mod paintings folder that is NOT already added to the config file. The mod only compare file names.
 * </p>
 * 
 * @author sidben
 */
@SideOnly(Side.CLIENT)
public class GuiScreenCustomPaintingsAddFileSelector extends GuiScreen // implements IListContainer
{

    private static final int                  BT_ID_BACK       = 1;
    private static final int                  BT_ID_OPENFOLDER = 2;
    private static final int                  BT_ID_SELECT     = 3;

    private static final int                  GUI_ADDNEW_RETURNCODE  = -3;      // Must be negative value

    
    public final GuiScreen                    parentScreen;
    public final boolean                      isWorldRunning;

    private List<GuiElementPaintingListEntry> paintingList;
    private GuiElementPaintingList            guiPaintingList;
    private GuiElementPaintingDetails         guiElementPaintingDetails;
    private GuiScreenCustomPaintingsEditor  guiEditor;
    private GuiButton                         btSelect;
    private int                               selectedIndex    = -1;



    public GuiScreenCustomPaintingsAddFileSelector(GuiScreen parentScreen) {
        this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.isWorldRunning = mc.theWorld != null;
    }



    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void initGui()
    {
        // Buttons
        final int secondColumnX = this.width / 2 + 4;
        final int buttonStartY = 100;

        btSelect = new GuiButton(BT_ID_SELECT, secondColumnX, buttonStartY, StatCollector.translateToLocal(getLanguageKey("select")));
        btSelect.width = 200;

        this.buttonList.add(new GuiOptionButton(BT_ID_OPENFOLDER, this.width / 2 - 154, this.height - 48, StatCollector.translateToLocal(getLanguageKey("open_folder"))));
        this.buttonList.add(new GuiUnicodeGlyphButton(BT_ID_BACK, secondColumnX, this.height - 48, 150, 20, " " + StatCollector.translateToLocal("gui.back"), GLYPH_BACK, 2.0F));
        this.buttonList.add(btSelect);

        this.displayDetailsButtons(false);



        // Listbox data (loads the image files that are not already in the mod config)
        final List<CustomPaintingConfigItem> filesNotInstalled = ConfigurationHandler.getPaintingsInConfigFolderNotInstalled();
        this.paintingList = new ArrayList();

        for (final CustomPaintingConfigItem configItem : filesNotInstalled) {
            this.paintingList.add(new GuiElementPaintingListEntry(this, configItem));
        }


        // Paintings listbox
        this.guiPaintingList = new GuiElementPaintingList(this.mc, 200, this.height, this.paintingList, this);
        this.guiPaintingList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.guiPaintingList.registerScrollButtons(7, 8);

        // Paintings details screen
        this.guiElementPaintingDetails = new GuiElementPaintingDetails(this, null);
        this.displayDetails(this.selectedIndex);
        
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
            btSelect.yPosition = 164;
        }


        // Selected painting extra info
        if (this.selectedIndex > -1) {
            this.guiElementPaintingDetails.drawScreen(mouseX, mouseY, partialTicks);
        }

        // Draws the listbox
        this.guiPaintingList.drawScreen(mouseX, mouseY, partialTicks);

        // Texts - Title, folder info
        this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal(getLanguageKey("title")), this.width / 2, 8, ColorTable.WHITE);
        this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal(getLanguageKey("add_new_file")), this.width / 2, 18, ColorTable.WHITE);
        this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal(getLanguageKey("folder_info")), this.width / 2 - 77, this.height - 26, ColorTable.GRAY);


        // Parent call (draws buttons)
        super.drawScreen(mouseX, mouseY, partialTicks);


        // Tooltips (OBS: this must come after [super.drawScreen], or else the buttons will get a weird gray overlay
        if (!StringUtils.isNullOrEmpty(this.guiPaintingList.getTooltip())) {
            this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(this.guiPaintingList.getTooltip(), 300), mouseX, mouseY);
        } else if (!StringUtils.isNullOrEmpty(this.guiElementPaintingDetails.getTooltip())) {
            this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(this.guiElementPaintingDetails.getTooltip(), 300), mouseX, mouseY);
        } else {
            final GuiButton bt = (GuiButton) this.buttonList.get(0);
            final boolean isMouseOverFolderButton = MouseHelper.isMouseInside(mouseX, mouseY, bt);

            if (isMouseOverFolderButton) {
                String info = "";

                info += String.format("%s%s\n", TextFormatTable.COLOR_GRAY, StatCollector.translateToLocal(getLanguageKey("prop.valid_extensions")));
                info += String.format("  %s%s\n", TextFormatTable.COLOR_YELLOW, ImageFilenameFilter.acceptedExtensions());
                info += String.format("%s%s\n", TextFormatTable.COLOR_GRAY, StatCollector.translateToLocal(getLanguageKey("prop.max_image_size")));
                info += String.format("  %1$s%2$dx%2$d\n", TextFormatTable.COLOR_YELLOW, ConfigurationHandler.maxPaintingSize);
                info += String.format("%s%s\n", TextFormatTable.COLOR_GRAY, StatCollector.translateToLocal(getLanguageKey("prop.max_filesize_kb")));
                info += String.format("  %s%.1f KB", TextFormatTable.COLOR_YELLOW, (ConfigurationHandler.maxFileSize / 1024F));

                this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(info, 300), mouseX, mouseY);
            }
        }


    }



    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled) {

            if (button.id == BT_ID_BACK) {
                // Returns to the parent screen
                this.mc.displayGuiScreen(this.parentScreen);
            }

            else if (button.id == BT_ID_SELECT) {
                if (button.enabled && this.selectedIndex >= 0 && this.selectedIndex < this.paintingList.size()) {
                    final GuiElementPaintingListEntry entry = this.paintingList.get(this.selectedIndex);
                    if (entry != null) {
                        this.guiEditor = new GuiScreenCustomPaintingsEditor(this, entry.getConfigItem(), GUI_ADDNEW_RETURNCODE);
                        this.mc.displayGuiScreen(guiEditor);
                    }
                }
            }

            else if (button.id == BT_ID_OPENFOLDER) {

                /*
                 * Ref: GuiScreenResourcePacks.actionPerformed(2)
                 */

                final File folder = new File(this.mc.mcDataDir, ConfigurationHandler.IMAGES_BASE_PATH);
                final String s = folder.getAbsolutePath();
                LogHelper.info("Opening custom paintings folder");

                if (Util.getOSType() == Util.EnumOS.OSX) {
                    try {
                        Runtime.getRuntime().exec(new String[] { "/usr/bin/open", s });
                        return;
                    } catch (final IOException ex) {
                        LogHelper.error("Couldn\'t open file");
                        LogHelper.error(ex.getMessage());
                    }
                } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                    final String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] { s });

                    try {
                        Runtime.getRuntime().exec(s1);
                        return;
                    } catch (final IOException ex) {
                        LogHelper.error("Couldn\'t open file");
                        LogHelper.error(ex.getMessage());
                    }
                }

                boolean flag = false;

                try {
                    final Class oclass = Class.forName("java.awt.Desktop");
                    final Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
                    oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { folder.toURI() });
                } catch (final Throwable throwable) {
                    LogHelper.error("Couldn\'t open link");
                    LogHelper.error(throwable.getMessage());
                    flag = true;
                }

                if (flag) {
                    LogHelper.info("Opening via system class!");
                    Sys.openURL("file://" + s);
                }
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



    /**
     * Called when the child window (painting editor) confirms the changes.
     */
    @Override
    public void confirmClicked(boolean result, int id)
    {
        if (id == GUI_ADDNEW_RETURNCODE) {
            if (result && this.selectedIndex >= 0 && this.selectedIndex < this.paintingList.size()) {
                final GuiElementPaintingListEntry entry = this.paintingList.get(this.selectedIndex);

                // Updates the entry
                entry.setPaintingInfo(this.guiEditor.getPaintingName(), this.guiEditor.getPaintingAuthor());

                // Updates and save the config file
                ConfigurationHandler.addOrUpdateEntry(entry.getConfigItem());
                ConfigurationHandler.updateAndSaveConfig();
                
                this.selectedIndex = -1;
                this.mc.displayGuiScreen(this);
            }
        }
        else 
        {
            // Item selection
            if (result) {
                this.selectedIndex = -1;
                this.displayDetailsButtons(false);
                this.displayDetails(id);
                this.selectedIndex = id;
            }
        }

    }






    @SuppressWarnings("rawtypes")
    public void drawToolTip(List stringList, int x, int y)
    {
        super.func_146283_a(stringList, x, y);
    }



    /**
     * Returns the full language key for elements of this GUI.
     */
    protected String getLanguageKey(String name)
    {
        return "sidben.ateliercanvas.config." + name;
    }



    protected void displayDetailsButtons(boolean visible)
    {
        displayDetailsButtons(visible, visible);
    }

    protected void displayDetailsButtons(boolean visible, boolean enabled)
    {
        btSelect.visible = visible;
        btSelect.enabled = enabled;
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
            this.selectedIndex = index;

            this.displayDetailsButtons(true, StringUtils.isNullOrEmpty(entry.getWarningMessage()));
        }
    }

}
