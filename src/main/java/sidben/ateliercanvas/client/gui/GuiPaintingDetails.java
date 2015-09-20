package sidben.ateliercanvas.client.gui;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.lwjgl.opengl.GL11;
import sidben.ateliercanvas.client.config.CustomPaintingConfigItem;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.reference.ColorTable;
import sidben.ateliercanvas.reference.TextFormatTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * Display details of the selected painting on the custom painting selection GUI.
 * 
 * 
 * @see sidben.ateliercanvas.client.gui.GuiScreenCustomPaintings
 * @author sidben
 *
 */
@SideOnly(Side.CLIENT)
public class GuiPaintingDetails extends GuiCustomPaintingIconLoader
{
    
    private String _tooltip; 
    
    
    
    
    @SuppressWarnings("unchecked")
    public GuiPaintingDetails(GuiScreenCustomPaintings ownerGui, CustomPaintingConfigItem entryData) {
        super(ownerGui, entryData);
    }
    
    
    
    
    public void updateConfigItem(CustomPaintingConfigItem entryData) {
        super.updateConfigItem(entryData);
    }
    
    

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        // Buttons
    }
    
    
    
    @SuppressWarnings("rawtypes")
    @Override
    public void drawScreen(int mouseX, int mouseY, float param3)
    {
        if (this._entryData != null && this._entryData.isValid()) {
            
            // Selected painting extra info
            int boxX = this._ownerGui.width / 2 + 4;
            int boxY = 32;
            int boxWidth = 200;
            int boxHeight = 64;
            int lineSpacing = 11;
            int titleMaginTop = 35;
            int titleMaginBottom = 15;
            
            // Size of the preview image
            int sampleWidth = super.getIconWidth() > 64 ? 64 : super.getIconWidth();
            int sampleHeight = super.getIconHeight() > 64 ? 64 : super.getIconHeight();      
            // TODO: support for paintings larger than 64x64 pixels

            // If the GUI is large, allows more info and a bigger picture
            if (this._ownerGui.height > 320) {
                boxHeight = 128;
                sampleWidth *= 2;
                sampleHeight *= 2;
            }
    
            int textStartX = this._ownerGui.width / 2 + 4;
            int textStartY = boxY + boxHeight + titleMaginTop;
    
    
            String paintingName = this._entryData.getPaintingTitle();
            String extraInfo = String.format("%s: %s", StatCollector.translateToLocal(this.getLanguageKey("author_label")), this._entryData.getPaintingAuthor());
            extraInfo += String.format("\n%s: %.1f KB", StatCollector.translateToLocal(this.getLanguageKey("filesize_label")), super.getFileSizeKBytes());
            extraInfo += String.format("\n%s: %dx%d (%dx%d pixels)", StatCollector.translateToLocal(this.getLanguageKey("size_label")), super.getTileWidth(), super.getTileHeight(), super.getIconWidth(), super.getIconHeight());
            // extraInfo += "\n" + (this._entryData.getIsEnabled() ? StatCollector.translateToLocal(this.getLanguageKey("enabled")) : StatCollector.translateToLocal(this.getLanguageKey("disabled")));
    
    
            // Draw the background box for the painting
            Gui.drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0x77000000);
            
            // Painting icon texture
            this.mc.getTextureManager().bindTexture(super.getPaintingIcon());
            
            
            // Icon ratio for non-square images
            int paddingTop = 0;
            int paddingLeft = 0;
            
            if (sampleHeight < boxHeight) {
                paddingTop = (int)((boxHeight - sampleHeight) / 2);
            }
            if (sampleWidth < boxWidth) {
                paddingLeft = (int)((boxWidth - sampleWidth) / 2);
            }
            

            // Draw the icon
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.func_146110_a(boxX + paddingLeft, boxY + paddingTop, 0.0F, 0.0F, sampleWidth, sampleHeight, sampleWidth, sampleHeight);

            
            
            // Draw the painting title
            this.mc.fontRenderer.drawStringWithShadow(paintingName, textStartX, textStartY, ColorTable.YELLOW);
    
            // Draw the painting extra info
            List extraInfoList = this.mc.fontRenderer.listFormattedStringToWidth(extraInfo, 157);
            for (int i = 0; i < extraInfoList.size(); i++)
            {
                this.mc.fontRenderer.drawStringWithShadow((String)extraInfoList.get(i), textStartX, textStartY + titleMaginBottom + (i * lineSpacing), ColorTable.GRAY);
            }
            
            
            // Tooltip
            this._tooltip = "";
            boolean isMouseOverIconArea = (mouseX >= boxX && mouseX <= boxX + boxWidth && mouseY >= boxY && mouseY <= boxY + boxHeight);
            if (isMouseOverIconArea) {
                this._tooltip = TextFormatTable.BOLD + this._entryData.getPaintingFileName() + TextFormatTable.RESET;
                this._tooltip += String.format("\n%s: %s", StatCollector.translateToLocal(this.getLanguageKey("date_created_label")), this._entryData.getFormatedCreationDate());
                this._tooltip += String.format("\n%s: %s", StatCollector.translateToLocal(this.getLanguageKey("date_updated_label")), this._entryData.getFormatedLastUpdateDate());
                this._tooltip += "\nUUID: " + this._entryData.getUUID();
            }
            
            
        }
        
        super.drawScreen(mouseX, mouseY, param3);
    }

    
    
    /**
     * Tooltip for the list item under the mouse. 
     */
    public String getTooltip() {
        return this._tooltip == null ? "" : this._tooltip;
    }



    /**
     * Returns the full language key for elements of this GUI. 
     */
    protected String getLanguageKey(String name) {
        return "sidben.ateliercanvas.config.painting_info." + name;
    }

}
