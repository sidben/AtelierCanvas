package sidben.ateliercanvas.client.gui;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.lwjgl.opengl.GL11;
import sidben.ateliercanvas.client.config.CustomPaintingConfigItem;
import sidben.ateliercanvas.reference.ColorTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
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
    
    
    public GuiPaintingDetails(GuiScreenCustomPaintings ownerGui, CustomPaintingConfigItem entryData) {
        super(ownerGui, entryData);
    }
    
    
    
    
    public void updateConfigItem(CustomPaintingConfigItem entryData) {
        super.updateConfigItem(entryData);
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
            int titleMaginTop = 10;
            int titleMaginBottom = 15;
            
            // Size of the preview image
            int sampleWidth = super.getIconWidth() > 64 ? 64 : super.getIconWidth();
            int sampleHeight = super.getIconHeight() > 64 ? 64 : super.getIconHeight();      
            // TODO: support for paintings larger than 64x64 pixels

            // If the GUI is large, allows more info and a bigger pic
            if (this._ownerGui.height > 320) {
                boxHeight = 128;
                sampleWidth *= 2;
                sampleHeight *= 2;
            }
    
            int textStartX = this._ownerGui.width / 2 + 4;
            int textStartY = boxY + boxHeight + titleMaginTop;
    
    
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");          // TODO: make date format a mod config. Default: yyyy-MM-dd
            
            // TODO: Localization of "Artist", "Date added", "File size", etc
            String paintingName = this._entryData.getPaintingTitle();
            String extraInfo = "Artist: " + this._entryData.getPaintingAuthor();
            extraInfo += String.format("\nSize: %dx%d (%dx%d pixels)", super.getTileWidth(), super.getTileHeight(), super.getIconWidth(), super.getIconHeight());
            extraInfo += "\nFile size: " + super.getFileSizeKBytes() + " KB";
            extraInfo += "\nDate added: " + dateFormat.format(this._entryData.getCreationDate());
            extraInfo += "\n" + (this._entryData.getIsEnabled() ? "Enabled" : "Disabled");
    
    
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
            
        }
        
        super.drawScreen(mouseX, mouseY, param3);
    }

}
