package sidben.ateliercanvas.client.gui;

import java.util.ArrayList;
import java.util.List;
import sidben.ateliercanvas.reference.ColorTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class GuiScreenCustomPaintingsAdd extends GuiScreen
{

    private static final int                BT_ID_DONE          = 1;
    private static final int                BT_ID_NEWFILE       = 2;
    private static final int                BT_ID_NEWURL        = 3;
    private static final int                BT_ID_NEWBASE64     = 4;         // OBS: under evaluation

    
    public final GuiScreen                  parentScreen;
    public final boolean                    isWorldRunning;
    
    private GuiListExtended                 guiList;



    
    public GuiScreenCustomPaintingsAdd(GuiScreen parentScreen) {
        this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.isWorldRunning = mc.theWorld != null;
    }

    
    
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        // Buttons
        int buttonX = this.parentScreen.width / 2 - 100;
        int buttonY = 64;
        
        this.buttonList.add(new GuiButton(BT_ID_DONE, buttonX, this.height - 29, StatCollector.translateToLocal("gui.done")));
        this.buttonList.add(new GuiButton(BT_ID_NEWFILE, buttonX, buttonY, StatCollector.translateToLocal(getLanguageKey("add_new_file"))));
        this.buttonList.add(new GuiButton(BT_ID_NEWURL, buttonX, buttonY + 22, StatCollector.translateToLocal(getLanguageKey("add_new_url"))));
        this.buttonList.add(new GuiButton(BT_ID_NEWBASE64, buttonX, buttonY + 44, StatCollector.translateToLocal(getLanguageKey("add_new_base64"))));

        ((GuiButton) this.buttonList.get(3)).enabled = false;
        /*
        for (int i = 0; i <= 3; i++) {
            ((GuiButton) this.buttonList.get(i)).enabled = false;
        }
        */

        // Fake listbox, used only for visual effects
        this.guiList = new GuiElementPaintingList(this.mc, this.width, this.height, new ArrayList<Object>());
    }
    
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // Clear the background
        this.drawDefaultBackground();

        // Draws the listbox
        this.guiList.drawScreen(mouseX, mouseY, partialTicks);


        // Texts - Title, Total paintings installed
        this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal(getLanguageKey("title")), this.width / 2, 8, ColorTable.WHITE);
        this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal(getLanguageKey("title_new")), this.width / 2, 18, ColorTable.WHITE);


        // Parent call (draws buttons)
        super.drawScreen(mouseX, mouseY, partialTicks);


        // Tooltips (OBS: this must come after [super.drawScreen], or else the buttons will get a weird gray overlay

    }



    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled) {

            if (button.id == BT_ID_DONE) {

                // Returns to the parent screen
                this.mc.displayGuiScreen(this.parentScreen);

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
        super.func_146283_a(stringList, x, y);
    }
    

    
    
    /**
     * Returns the full language key for elements of this GUI.
     */
    protected String getLanguageKey(String name)
    {
        return "sidben.ateliercanvas.config." + name;
    }

}
