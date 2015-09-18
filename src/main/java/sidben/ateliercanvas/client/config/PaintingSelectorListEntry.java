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
import sidben.ateliercanvas.client.gui.GuiScreenCustomPaintings;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
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
public class PaintingSelectorListEntry implements GuiListExtended.IGuiListEntry
{
    
    private static final ResourceLocation resourcePacksTextures = new ResourceLocation("textures/gui/resource_packs.png");
    protected final Minecraft mc;
    protected final GuiScreenCustomPaintings parentGui;
    private final ResourceLocation missingTextureLocation;
    
    
    /**
     * Information about the custom painting. It's expected the following format:
     * 
     * <ul>
     *  <li><b>[0]</b> - Image file name. String, required.</li>
     *  <li><b>[1]</b> - Painting GUID. Required (created automatically by the mod).</li>
     *  <li><b>[2]</b> - Painting enabled. Boolean, required.</li>
     *  <li><b>[3]</b> - File size, in bytes. Float, required.</li>
     *  <li><b>[4]</b> - Painting name. String, optional.</li>
     *  <li><b>[5]</b> - Painting author. String, optional.</li>
     *  <li><b>[?]</b> - Painting lore. String, optional.</li>
     *  <li><b>[?]</b> - Player name that imported / created / uploaded the painting.</li>
     *  <li><b>[?]</b> - Player UUID that imported / created / uploaded the painting.</li>
     *  <li><b>[?]</b> - Creation date. Datetime, required.</li>
     *  <li><b>[?]</b> - Last update date. Datetime, required. Initially, will be the same as the creation date.</li>
     * </ul>
     * 
     */
    private final String[] _entryData;
    


    public PaintingSelectorListEntry(GuiScreenCustomPaintings p_i45051_1_, String[] entryData)
    {
        this.parentGui = p_i45051_1_;
        this.mc = Minecraft.getMinecraft();
        
        
        DynamicTexture dynamictexture;
        dynamictexture = TextureUtil.missingTexture;

        this.missingTextureLocation = this.mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamictexture);
        this._entryData = entryData;
    }
    
    
    

    /**
     * @return TRUE is the entryData array has a valid size.
     */
    public boolean isValid() {
        // TODO: add other checks, like file size. Add a tooltip telling what is the problem.
        
        int expectedLength = 6;
        return (this._entryData != null && this._entryData.length >= expectedLength);
    }

    
    
    protected String getPaintingFileName() {
        return this._entryData[0];
    }
    
    protected String getPaintingTitle() {
        return this._entryData[4].isEmpty() ? "§oUnnamed§r" : this._entryData[4];
    }
    
    protected String getPaintingAuthor() {
        return this._entryData[5].isEmpty() ? "§oUnknown§r" : this._entryData[5];
    }
    
    
    
    
    
    // TODO: DrawInvalidEntry() / DrawFoundEntry()
    
    @SuppressWarnings("rawtypes")
    @Override
    public void drawEntry(int p_148279_1_, int listInitialX, int listInitialY, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean mouseOver)
    {
        String iconPath = "config/AtelierCanvas_Paintings/";
        InputStream iconStream;
        BufferedImage paintingIcon;
        ResourceLocation locationPaintingIcon = null;
        
        int iconWidth = 0;
        int iconHeight = 0;
        long iconSize = 0;      // bytes
        
        String paintingName = "UNKOWN";
        String paintingInfo1 = "";
        String paintingInfo2 = "";

        
        if (this.isValid()) {
            
            iconPath += this.getPaintingFileName();
            paintingName = this.getPaintingTitle();
            
            
            // TODO: Check if I can avoid reading the file every drawEntry call. Can't I load just on the class constructor?
            try {
                // TODO: Avoid loading files too big. Individually, paintings have up to 4KB. The vanilla "paintings_kristoffer_zetterstrand.png" file has 76 KB
                File iconFile = new File(this.mc.mcDataDir, iconPath);
                iconSize = iconFile.length();
                
                // TODO: validate max dimensions in pixels (mod param)
                // TODO: Decide about images that don't follow the 16x16 ratio (accept, reject, edit or make optional)
    
                iconStream = new BufferedInputStream(new FileInputStream(iconFile));
                paintingIcon = ImageIO.read(iconStream);
                iconWidth = paintingIcon.getWidth();
                iconHeight = paintingIcon.getHeight();
                
                locationPaintingIcon = this.mc.getTextureManager().getDynamicTextureLocation("paintingicon", new DynamicTexture(paintingIcon));
            } catch (IOException e) {
                e.printStackTrace();
            }

            
            paintingInfo1 = "Artist: " + this.getPaintingAuthor();
            paintingInfo2 = iconWidth + "x" + iconHeight +  " - Enabled";

        }


        
        // Painting icon texture
        if (locationPaintingIcon != null) {
            this.mc.getTextureManager().bindTexture(locationPaintingIcon);
        } else {
            this.mc.getTextureManager().bindTexture(this.missingTextureLocation);
        }
        
        
        // Size of the preview image on the right
        int sampleWidth = 32, sampleHeight = 32;         
        
        // Icon ratio for non-square images
        float iconWidthStretchRatio = 1.0F;
        float iconHeightStretchRatio = 1.0F;
        int paddingTop = 0;
        int paddingLeft = 0;
        
        if (iconWidth > iconHeight) {
            iconHeightStretchRatio = (float)iconHeight / (float)iconWidth;
            paddingTop = (int)((sampleWidth * (1F - iconHeightStretchRatio)) / 2);
        }
        else if (iconHeight > iconWidth) {
            iconWidthStretchRatio = (float)iconWidth / (float)iconHeight;  
            paddingLeft = (int)((sampleHeight * (1F - iconWidthStretchRatio)) / 2);
        }
        
        sampleWidth = (int)(sampleWidth * iconWidthStretchRatio);
        sampleHeight = (int)(sampleHeight * iconHeightStretchRatio);
        
        
        // Draw the icon
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.func_146110_a(listInitialX + paddingLeft, listInitialY + paddingTop, 0.0F, 0.0F, sampleWidth, sampleHeight, 32.0F * iconWidthStretchRatio, 32.0F * iconHeightStretchRatio);

        
        
        
        if ((this.mc.gameSettings.touchscreen || mouseOver))
        {
            // Hover
            this.mc.getTextureManager().bindTexture(resourcePacksTextures);
            Gui.drawRect(listInitialX, listInitialY, listInitialX + 32, listInitialY + 32, -1601138544);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        
        
        int textWidth;

        
        // Painting name
        textWidth = this.mc.fontRenderer.getStringWidth(paintingName);
        if (textWidth > 157) paintingName = this.mc.fontRenderer.trimStringToWidth(paintingName, 157 - this.mc.fontRenderer.getStringWidth("...")) + "...";
        this.mc.fontRenderer.drawStringWithShadow(paintingName, listInitialX + 32 + 2, listInitialY + 1, 16777215);


        // Painting data (author, size, etc)
        this.mc.fontRenderer.drawStringWithShadow(paintingInfo1, listInitialX + 32 + 2, listInitialY + 12, 8421504);
        this.mc.fontRenderer.drawStringWithShadow(paintingInfo2, listInitialX + 32 + 2, listInitialY + 22, 8421504);
        
    }

    
    @Override
    public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        return false;
    }

    
    @Override
    public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_)
    {
    }

}
