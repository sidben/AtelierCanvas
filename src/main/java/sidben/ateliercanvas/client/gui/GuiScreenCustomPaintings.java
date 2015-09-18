package sidben.ateliercanvas.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import sidben.ateliercanvas.client.config.PaintingSelectorListEntry;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * Screen where the player can see all custom paintings installed, see details of each
 * painting and open the GUI to add a new painting. 
 * 
 * A custom painting is considered 'installed' when the entry is added to the mod config 
 * file, so it's not enough to just add PNG files to the mod config folder. A player will
 * need to use this GUI to notify the mod of the images and also provide more info, like
 * painting name or author.
 * 
 * This GUI was created using the ResourcePack selector GUI as reference.
 * 
 * 
 * @see sidben.ateliercanvas.client.gui.GuiCustomPaintingList
 * @see sidben.ateliercanvas.client.config.PaintingSelectorListEntry
 * @see net.minecraft.client.gui.GuiScreenResourcePacks
 * @author sidben
 *
 */
@SideOnly(Side.CLIENT)
public class GuiScreenCustomPaintings extends GuiScreen
{

    public final GuiScreen parentScreen;
    
    @SuppressWarnings("rawtypes")
    private List paintingList;
    private GuiCustomPaintingList guiPaintingList;
    
    private static final int BT_ID_DONE = 1;
    private static final int BT_ID_ADDNEW = 2;

    
    

    public GuiScreenCustomPaintings(GuiScreen parentScreen)
    {
        this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
    }

    
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void initGui()
    {
        this.buttonList.add(new GuiOptionButton(BT_ID_ADDNEW, this.width / 2 - 154, this.height - 48, I18n.format("sidben.ateliercanvas.config.painting_selector.add_new", new Object[0])));       // TODO: encapsulate "sidben.ateliercanvas.config" namespace 
        this.buttonList.add(new GuiOptionButton(BT_ID_DONE, this.width / 2 + 4, this.height - 48, I18n.format("gui.done", new Object[0])));
        this.paintingList = new ArrayList();

        
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_01.png", "", "1", "1328", "Piggy", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_02.png", "", "1", "1325", "First Shelter", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_03.png", "", "1", "1444", "Quiet Days", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_04.png", "", "1", "1422", "", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_05.png", "", "1", "1394", "Forest", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_06.png", "", "1", "1367", "", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_07.png", "", "1", "1244", "Blazing Hot", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_10.png", "", "1", "1859", "Seasons", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_11.png", "", "1", "1555", "Chickens", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_12.png", "", "1", "1497", "Fishing Lake", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_13.png", "", "1", "1335", "", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_14.png", "", "1", "1682", "Banned Arthropod", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_17.png", "", "1", "1753", "Lost Promisses", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_18.png", "", "1", "1690", "Camouflage", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_20.png", "", "1", "3233", "Witch Hut?", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_21.png", "", "1", "2056", "In the Nether", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_23.png", "", "1", "2292", "Ancient Temple", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_24.png", "", "1", "2359", "Wither Rise", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_25.png", "", "1", "2521", "Zombie Terror", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_26.png", "", "1", "2232", "Abandoned Mines", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_27.png", "", "1", "2403", "Curious Kitty", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_28.png", "", "1", "1349", "Moonlight", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_29.png", "", "1", "1719", "Deep Sea", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_31.png", "", "1", "3121", "The End is Near", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_32.png", "", "1", "3588", "Jungle Mistery", "Pig_Rider" }));
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "png24_33.png", "", "1", "2772", "Mooshroom Paradise", "Pig_Rider" }));
        
        this.paintingList.add(new PaintingSelectorListEntry(this, new String[] { "Lollynl-guardian-(sidben-edit).png", "", "1", "6241", "Guardian", "Lollynl" }));
        
        
        this.guiPaintingList = new GuiCustomPaintingList(this.mc, 200, this.height, this.paintingList);
        this.guiPaintingList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.guiPaintingList.registerScrollButtons(7, 8);
    }
    
    
    /*
     * p1 = 187 (98 full screen)
     * p2 = 109 (184 full screen)
     * p3 = 0.3861351 (tick? frame? varies from 0 to 1)
     */
    @Override
    public void drawScreen(int param1, int param2, float param3)
    {
        this.drawBackground(0);
        this.guiPaintingList.drawScreen(param1, param2, param3);
        this.drawCenteredString(this.fontRendererObj, I18n.format("sidben.ateliercanvas.config.painting_selector.title"), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRendererObj, "_TOTAL_ paintings installed", this.width / 2 - 77, this.height - 26, 8421504);
        super.drawScreen(param1, param2, param3);
    }
    
    
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            
            if (button.id == BT_ID_DONE) 
            {
                this.mc.displayGuiScreen(this.parentScreen);
                
            }
            else if (button.id == BT_ID_ADDNEW) 
            {
                
                
            }

        }            
    }
    
}
