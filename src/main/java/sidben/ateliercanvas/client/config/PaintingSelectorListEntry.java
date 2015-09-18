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
    
    private static final ResourceLocation field_148316_c = new ResourceLocation("textures/gui/resource_packs.png");
    private final ResourceLocation field_148321_e;
    protected final Minecraft field_148317_a;
    protected final GuiScreenCustomPaintings field_148315_b;


    public PaintingSelectorListEntry(GuiScreenCustomPaintings p_i45051_1_)
    {
        this.field_148315_b = p_i45051_1_;
        this.field_148317_a = Minecraft.getMinecraft();
        
        
        DynamicTexture dynamictexture;
        dynamictexture = TextureUtil.missingTexture;

        this.field_148321_e = this.field_148317_a.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamictexture);
    }

    
    
    
    
    @SuppressWarnings("rawtypes")
    @Override
    public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
    {
        
        /*
         * getInputStreamByName()
         *      return new BufferedInputStream(new FileInputStream(new File(this.resourcePackFile, p_110591_1_)));
         * 
         * public BufferedImage getPackImage() throws IOException
         *      return ImageIO.read(this.getInputStreamByName("pack.png"));
         * 
         * 
         * private BufferedImage texturePackIcon;
         * private ResourceLocation locationTexturePackIcon;
         * 
         * this.texturePackIcon = this.reResourcePack.getPackImage();
         * 
         * this.locationTexturePackIcon = p_110518_1_.getDynamicTextureLocation("texturepackicon", new DynamicTexture(this.texturePackIcon));
         * TextureManager / p_110518_1_.bindTexture(this.locationTexturePackIcon);
         * 
         */
        
        String a1 = ConfigurationHandler.config.toString();                 // "C:\\{folder}\\{folder}\\{folder}\\run\\config\\AtelierCanvas.cfg"
        //String a2 = getAbridgedConfigPath(a1);                            // "/.minecraft/config/AtelierCanvas.cfg"
        String a3 = this.field_148317_a.mcDataDir.getAbsolutePath();        // "C:\\{folder}\\{folder}\\{fodler}\\run\\."

        
        
        String iconPath = "config/AtelierCanvas_Paintings/png24_32.png";
        iconPath = "config/AtelierCanvas_Paintings/png24_18.png";
        
        InputStream iconStream;
        BufferedImage paintingIcon;
        ResourceLocation locationPaintingIcon = null;
        
        int iconWidth = 0;
        int iconHeight = 0;
        long iconSize = 0;      // bytes
        // Dimension test = new Dimension(1024, 768);

        try {
            // TODO: Avoid loading files too big. Individually, paintings have up to 4KB. The vanilla "paintings_kristoffer_zetterstrand.png" file has 76 KB
            File iconFile = new File(this.field_148317_a.mcDataDir, iconPath);
            iconSize = iconFile.length();

            iconStream = new BufferedInputStream(new FileInputStream(iconFile));
            paintingIcon = ImageIO.read(iconStream);
            iconWidth = paintingIcon.getWidth();
            iconHeight = paintingIcon.getHeight();
            
            locationPaintingIcon = this.field_148317_a.getTextureManager().getDynamicTextureLocation("paintingicon", new DynamicTexture(paintingIcon));
        } catch (IOException e) {
            e.printStackTrace();
        }


        
        // Painting icon
        if (locationPaintingIcon != null) {
            this.field_148317_a.getTextureManager().bindTexture(locationPaintingIcon);
        } else {
            this.field_148317_a.getTextureManager().bindTexture(this.field_148321_e);
        }
        
        
        int sampleWidth = 32, sampleHeight = 32;         // Size of the preview image on the right
        
        // p_148279_2_ = X coord where the list item starts
        // p_148279_3_ = Y coord where the list item starts
        
        float iconWidthStretchRatio = 0.5F;
        float iconHeightStretchRatio = 1.0F;
        
        sampleWidth = (int)(sampleWidth * iconWidthStretchRatio);
        sampleHeight = (int)(sampleHeight * iconHeightStretchRatio);
        
        // TODO: horizontal and vertical center alignment (add 1/2 of the ratio spare space)
        
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.func_146110_a(p_148279_2_, p_148279_3_, 0.0F, 0.0F, sampleWidth, sampleHeight, 32.0F * iconWidthStretchRatio, 32.0F * iconHeightStretchRatio);
        int i2;
        
        

        if ((this.field_148317_a.gameSettings.touchscreen || p_148279_9_))
        {
            // Hover
            this.field_148317_a.getTextureManager().bindTexture(field_148316_c);
            Gui.drawRect(p_148279_2_, p_148279_3_, p_148279_2_ + 32, p_148279_3_ + 32, -1601138544);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            i2 = p_148279_8_ - p_148279_3_;
        }

        
        // Painting name
        String s = "Painting Name";
        i2 = this.field_148317_a.fontRenderer.getStringWidth(s);

        if (i2 > 157)
        {
            s = this.field_148317_a.fontRenderer.trimStringToWidth(s, 157 - this.field_148317_a.fontRenderer.getStringWidth("...")) + "...";
        }
        this.field_148317_a.fontRenderer.drawStringWithShadow(s, p_148279_2_ + 32 + 2, p_148279_3_ + 1, 16777215);


        // Painting data (author, size, etc)
        List list = this.field_148317_a.fontRenderer.listFormattedStringToWidth("Description of the thingy", 157);
        for (int j2 = 0; j2 < 2 && j2 < list.size(); ++j2)
        {
            this.field_148317_a.fontRenderer.drawStringWithShadow((String)list.get(j2), p_148279_2_ + 32 + 2, p_148279_3_ + 12 + 10 * j2, 8421504);
        }
        
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
