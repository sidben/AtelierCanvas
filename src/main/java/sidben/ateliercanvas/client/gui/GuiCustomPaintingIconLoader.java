package sidben.ateliercanvas.client.gui;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import sidben.ateliercanvas.client.config.CustomPaintingConfigItem;
import sidben.ateliercanvas.helper.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * Base class for GUI that loads the custom painting preview icon
 * using information from an CustomPaintingConfigEntry object.
 * 
 * 
 * @author sidben
 *
 */
@SideOnly(Side.CLIENT)
public abstract class GuiCustomPaintingIconLoader extends GuiScreen
{

    private final static String ICON_BASE_PATH = "config/AtelierCanvas_Paintings/";
    private ResourceLocation _locationPaintingIcon;
    private int _iconWidth;
    private int _iconHeight;
    private long _fileSize;
    private int _resolution = 16;           // TODO: support for high-res paintings

    protected final Minecraft mc;
    protected final GuiScreenCustomPaintings _ownerGui;
    protected CustomPaintingConfigItem _entryData;

    
    
    
    public GuiCustomPaintingIconLoader(GuiScreenCustomPaintings ownerGui, CustomPaintingConfigItem entryData) {
        this.mc = Minecraft.getMinecraft();
        this._ownerGui = ownerGui;
        updateConfigItem(entryData);
    }

    
    
    protected void updateConfigItem(CustomPaintingConfigItem entryData) {
        this._entryData = entryData;
        this.loadPaintingIcon();
    }
    
    
    
    private void loadPaintingIcon() {
        _locationPaintingIcon = null;
        _iconWidth = 0;
        _iconHeight = 0;
        _fileSize = 0;
                
        if (this._entryData != null && this._entryData.isValid()) {
            String iconPath;
            InputStream iconStream;
            BufferedImage paintingIcon;

            // Sets the path of the painting file
            iconPath = GuiCustomPaintingIconLoader.ICON_BASE_PATH + this._entryData.getPaintingFileName();
            

            try {
                // TODO: Avoid loading files too big. Individually, paintings have up to 4KB. The vanilla "paintings_kristoffer_zetterstrand.png" file has 76 KB
                File iconFile = new File(this.mc.mcDataDir, iconPath);
                this._fileSize = iconFile.length();
                
                // TODO: validate if the file size matches the config
                // TODO: validate max dimensions in pixels (mod param)
                // TODO: Decide about images that don't follow the 16x16 ratio (accept, reject, edit or make optional)
    
                iconStream = new BufferedInputStream(new FileInputStream(iconFile));
                paintingIcon = ImageIO.read(iconStream);
                this._iconWidth = paintingIcon.getWidth();
                this._iconHeight = paintingIcon.getHeight();
                this._locationPaintingIcon = this.mc.getTextureManager().getDynamicTextureLocation("paintingicon", new DynamicTexture(paintingIcon));
                
            } catch (IOException e) {
                _locationPaintingIcon = null;
                LogHelper.error("Error loading custom painting - " + this._entryData.toString());
                e.printStackTrace();
                
            }

        }
        
        
        // Loads the broken icon, if the correct wasn't found
        if (this._locationPaintingIcon == null) {
            DynamicTexture dynamictexture = TextureUtil.missingTexture;

            this._locationPaintingIcon = this.mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamictexture);
            this._iconHeight = 16;
            this._iconHeight = 16;
        }
    }
    

    
    
    protected ResourceLocation getPaintingIcon() {
        return _locationPaintingIcon;
    }


    protected int getIconWidth() {
        return this._iconWidth;
    }
    
    protected int getIconHeight() {
        return this._iconHeight;
    }
    
    protected int getTileWidth() {
        return this._iconWidth / this._resolution;
    }
    
    protected int getTileHeight() {
        return this._iconHeight / this._resolution;
    }
    
    protected long getFileSizeBytes() {
        return this._fileSize;
    }
    
    protected float getFileSizeKBytes() {
        return (float)this.getFileSizeBytes() / 1024.0F;
    }

    
    
}
