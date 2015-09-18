package sidben.ateliercanvas.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import sidben.ateliercanvas.client.config.PaintingSelectorListEntry;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntryDefault;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackRepository;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class GuiScreenCustomPaintings extends GuiScreen
{

    private List paintingList;
    private GuiCustomPaintingList guiPaintingList;

    
    
    
    @SuppressWarnings({ "unchecked", "rawtypes", "unused" })
    public void initGui()
    {
        this.buttonList.add(new GuiOptionButton(2, this.width / 2 - 154, this.height - 48, I18n.format("sidben.ateliercanvas.config.painting_selector.add_new", new Object[0])));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 4, this.height - 48, I18n.format("gui.done", new Object[0])));
        this.paintingList = new ArrayList();
        
        ResourcePackRepository resourcepackrepository = this.mc.getResourcePackRepository();
        resourcepackrepository.updateRepositoryEntriesAll();
        ArrayList arraylist = Lists.newArrayList(resourcepackrepository.getRepositoryEntriesAll());
        arraylist.removeAll(resourcepackrepository.getRepositoryEntries());
        Iterator iterator = arraylist.iterator();
        ResourcePackRepository.Entry entry;

        while (iterator.hasNext())
        {
            entry = (ResourcePackRepository.Entry)iterator.next();
            // this.field_146966_g.add(new ResourcePackListEntryFound(this, entry));
        }

        //this.field_146969_h.add(new ResourcePackListEntryDefault(this));
        
        
        this.paintingList.add(new PaintingSelectorListEntry(this));
        this.paintingList.add(new PaintingSelectorListEntry(this));
        this.paintingList.add(new PaintingSelectorListEntry(this));
        this.paintingList.add(new PaintingSelectorListEntry(this));
        this.paintingList.add(new PaintingSelectorListEntry(this));
        this.paintingList.add(new PaintingSelectorListEntry(this));
        this.paintingList.add(new PaintingSelectorListEntry(this));
        this.paintingList.add(new PaintingSelectorListEntry(this));
        this.paintingList.add(new PaintingSelectorListEntry(this));
        
        
        
        this.guiPaintingList = new GuiCustomPaintingList(this.mc, 200, this.height, this.paintingList);
        this.guiPaintingList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.guiPaintingList.registerScrollButtons(7, 8);
        /*
        this.field_146967_r = new GuiResourcePackSelected(this.mc, 200, this.height, this.field_146969_h);
        this.field_146967_r.setSlotXBoundsFromLeft(this.width / 2 + 4);
        this.field_146967_r.registerScrollButtons(7, 8);
        */
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
        //this.field_146967_r.drawScreen(param1, param2, param3);
        this.drawCenteredString(this.fontRendererObj, I18n.format("sidben.ateliercanvas.config.painting_selector.title"), this.width / 2, 16, 16777215);
        //this.drawCenteredString(this.fontRendererObj, I18n.format("sidben.ateliercanvas.config.painting_selector.title", new Object[0]), this.width / 2, 16, 16777215);
        // this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.folderInfo2", new Object[0]), this.width / 2 - 77, this.height - 26, 8421504);
        super.drawScreen(param1, param2, param3);
    }
    
}
