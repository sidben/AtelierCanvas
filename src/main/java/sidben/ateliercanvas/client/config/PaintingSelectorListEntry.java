package sidben.ateliercanvas.client.config;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import sidben.ateliercanvas.client.gui.GuiCustomPaintingIconLoader;
import sidben.ateliercanvas.client.gui.GuiScreenCustomPaintings;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.reference.ColorTable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;


/**
 * Represents one item of the GuiCustomPaintingList ListBox.
 * 
 * 
 * @see sidben.ateliercanvas.client.gui.GuiCustomPaintingList
 * @see net.minecraft.client.resources.ResourcePackListEntry
 * @author sidben
 *
 */
@SideOnly(Side.CLIENT)
public class PaintingSelectorListEntry extends GuiCustomPaintingIconLoader implements GuiListExtended.IGuiListEntry
{
    /* 
     * OBS: GuiCustomPaintingIconLoader extends GuiScreen, but this class don't need to be a GuiScreen.
     * 
     * Will this cause any problems? Find out on the next episode!
     */
    

    
    
    public PaintingSelectorListEntry(GuiScreenCustomPaintings ownerGui, CustomPaintingConfigItem entryData) {
        super(ownerGui, entryData);
    }
    
    
    

    
    
    @Override
    public void drawEntry(int itemId, int listInitialX, int listInitialY, int listItemWidth, int listItemHeight, Tessellator p_148279_6_, int mouseX, int mouseY, boolean mouseOver)
    {
        String paintingName = "UNKNOWN";
        String paintingInfo1 = "";
        String paintingInfo2 = "";
        


        // Painting icon texture
        this.mc.getTextureManager().bindTexture(super.getPaintingIcon());
        
        
        // Size of the preview image on the right
        int sampleWidth = 32, sampleHeight = 32;         
        
        // Icon ratio for non-square images
        float iconWidthStretchRatio = 1.0F;
        float iconHeightStretchRatio = 1.0F;
        int paddingTop = 0;
        int paddingLeft = 0;

        
        if (this._entryData.isValid()) {
            // TODO: Localization of "Artist", "Date added", "File size", etc
            paintingName = this._entryData.getPaintingTitle();
            paintingInfo1 = "Artist: " + this._entryData.getPaintingAuthor();
            paintingInfo2 = String.format("Size: %dx%d", super.getTileWidth(), super.getTileHeight());

        
            // Adjusts the width/height ratio and padding to display the painting correctly
            if (super.getIconWidth() > super.getIconHeight()) {
                iconHeightStretchRatio = (float)super.getIconHeight() / (float)super.getIconWidth();
                paddingTop = (int)((sampleWidth * (1F - iconHeightStretchRatio)) / 2);
            }
            else if (super.getIconHeight() > super.getIconWidth()) {
                iconWidthStretchRatio = (float)super.getIconWidth() / (float)super.getIconHeight();  
                paddingLeft = (int)((sampleHeight * (1F - iconWidthStretchRatio)) / 2);
            }
            
            sampleWidth = (int)(sampleWidth * iconWidthStretchRatio);
            sampleHeight = (int)(sampleHeight * iconHeightStretchRatio);
        }

        
        
        // On mouse over
        if ((this.mc.gameSettings.touchscreen || mouseOver))
        {
            // Hover
            Gui.drawRect(listInitialX - 2, listInitialY - 2, listInitialX + listItemWidth - 10, listInitialY + listItemHeight + 2, 0x33FFFFFF);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            // Tooltip
            String tooltipMessage = this._entryData.getValiadtionErrors();
            if (!tooltipMessage.isEmpty())
                super._ownerGui.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(tooltipMessage, 300), mouseX, mouseY);
        }

        
        
        // Draw the icon
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.func_146110_a(listInitialX + paddingLeft, listInitialY + paddingTop, 0.0F, 0.0F, sampleWidth, sampleHeight, 32.0F * iconWidthStretchRatio, 32.0F * iconHeightStretchRatio);


        // Draws the texts
        int textWidth;

        // Painting name
        textWidth = this.mc.fontRenderer.getStringWidth(paintingName);
        if (textWidth > 157) paintingName = this.mc.fontRenderer.trimStringToWidth(paintingName, 157 - this.mc.fontRenderer.getStringWidth("...")) + "...";
        this.mc.fontRenderer.drawStringWithShadow(paintingName, listInitialX + 32 + 2, listInitialY + 1, ColorTable.WHITE);

        // Painting data (author, size, etc)
        this.mc.fontRenderer.drawStringWithShadow(paintingInfo1, listInitialX + 32 + 2, listInitialY + 12, ColorTable.GRAY);
        this.mc.fontRenderer.drawStringWithShadow(paintingInfo2, listInitialX + 32 + 2, listInitialY + 22, ColorTable.GRAY);
        
    }

    
    @Override
    public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
        this._ownerGui.displayDetails(index);
        return false;
    }


    @Override
    public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }

}
