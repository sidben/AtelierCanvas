package sidben.ateliercanvas.client.gui;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.helper.ImageFilenameFilter;
import sidben.ateliercanvas.helper.LogHelper;
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
public abstract class GuiElementPaintingIconLoader extends GuiScreen
{

    private ResourceLocation                 _locationPaintingIcon;
    private int                              _iconWidth;
    private int                              _iconHeight;
    private long                             _fileSize;
    private final int                        _resolution    = ConfigurationHandler.defaultResolution;           // TODO: support for high-res paintings
    private String                           _warnings;
    private boolean                          _validImage    = false;

    protected final Minecraft                mc;
    protected final GuiScreen _ownerGui;
    protected CustomPaintingConfigItem       _entryData;



    public GuiElementPaintingIconLoader(GuiScreen ownerGui, CustomPaintingConfigItem entryData) {
        this.mc = Minecraft.getMinecraft();
        this._ownerGui = ownerGui;
        updateConfigItem(entryData);
    }



    protected void updateConfigItem(CustomPaintingConfigItem entryData)
    {
        this._entryData = entryData;
        this.loadPaintingIcon();
    }



    private void loadPaintingIcon()
    {
        _locationPaintingIcon = null;
        _iconWidth = 0;
        _iconHeight = 0;
        _fileSize = 0;
        _validImage = false;
        
        if (this._entryData != null && this._entryData.isValid()) {
            String iconPath;
            InputStream iconStream;
            BufferedImage paintingIcon;

            // Sets the path of the painting file
            iconPath = ConfigurationHandler.IMAGES_BASE_PATH + this._entryData.getPaintingFileName();


            try {
                final File iconFile = new File(this.mc.mcDataDir, iconPath);
                this._fileSize = iconFile.length();
                final ImageFilenameFilter fileExtensionChecker = new ImageFilenameFilter();

                // TODO: Decide about images that don't follow the 16x16 ratio (accept, reject, edit or make optional)

                // Validate if the file exists
                if (!iconFile.exists()) {
                    this._warnings = StatCollector.translateToLocal(this.getLanguageKey("error_not_found"));
                }

                // Validate file size
                else if (this._fileSize > ConfigurationHandler.maxFileSize) {
                    this._warnings = StatCollector.translateToLocal(this.getLanguageKey("error_big_file"));
                }

                // Compare file size (actual VS config)
                else if (this._fileSize != this._entryData.getExpectedSize()) {
                    this._warnings = StatCollector.translateToLocal(this.getLanguageKey("error_size_not_match"));
                }

                // Validate file extension
                else if (!fileExtensionChecker.accept(iconFile.getParentFile(), iconFile.getName())) {
                    this._warnings = StatCollector.translateToLocal(this.getLanguageKey("error_invalid_extension"));
                }

                // Try to load the image
                else {
                    iconStream = new BufferedInputStream(new FileInputStream(iconFile));
                    paintingIcon = ImageIO.read(iconStream);
                    iconStream.close();

                    this._iconWidth = paintingIcon.getWidth();
                    this._iconHeight = paintingIcon.getHeight();

                    // Validate the max dimensions
                    if (this._iconWidth > ConfigurationHandler.maxPaintingSize || this._iconHeight > ConfigurationHandler.maxPaintingSize) {
                        this._warnings = StatCollector.translateToLocal(this.getLanguageKey("error_big_pixels"));
                    }

                    else {
                        _locationPaintingIcon = this.mc.getTextureManager().getDynamicTextureLocation("custom_painting_icon", new DynamicTexture(paintingIcon));
                    }
                }


            } catch (final IOException e) {
                _locationPaintingIcon = null;
                LogHelper.error("Error loading custom painting - " + this._entryData.toString());
                e.printStackTrace();

            }

        }


        // Loads the broken icon, if the correct wasn't found
        if (this._locationPaintingIcon == null) {
            final DynamicTexture dynamictexture = TextureUtil.missingTexture;

            this._locationPaintingIcon = this.mc.getTextureManager().getDynamicTextureLocation("missing_icon", dynamictexture);
            this._iconWidth = 32;
            this._iconHeight = 32;
        } else {
            this._validImage = true;
        }
    }



    protected ResourceLocation getPaintingIcon()
    {
        return _locationPaintingIcon;
    }


    protected int getIconWidth()
    {
        return this._iconWidth;
    }

    protected int getIconHeight()
    {
        return this._iconHeight;
    }

    protected int getTileWidth()
    {
        return this._iconWidth / this._resolution;
    }

    protected int getTileHeight()
    {
        return this._iconHeight / this._resolution;
    }

    protected long getFileSizeBytes()
    {
        return this._fileSize;
    }

    protected float getFileSizeKBytes()
    {
        return this.getFileSizeBytes() / 1024.0F;
    }

    protected boolean hasValidImage() {
        return this._validImage;
    }
    
    

    /**
     * Returns any error/warning messages generated when this class
     * tried to load the painting image.
     */
    protected String getWarningMessage()
    {
        return this._warnings == null ? "" : this._warnings;
    }


    /**
     * Returns the full language key for elements of this GUI.
     */
    protected String getLanguageKey(String name)
    {
        return "sidben.ateliercanvas.config.painting_info." + name;
    }

}
