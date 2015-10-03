package sidben.ateliercanvas.client.gui;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.helper.ImageFilenameFilter;
import sidben.ateliercanvas.helper.LocalizationHelper;
import sidben.ateliercanvas.helper.LocalizationHelper.Category;
import sidben.ateliercanvas.helper.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * Base class for GUI that loads the custom painting preview icon
 * using information from an CustomPaintingConfigEntry object.
 * 
 * @author sidben
 */
@SideOnly(Side.CLIENT)
public abstract class GuiElementPaintingIconLoader extends GuiScreen
{

    private ResourceLocation         _locationPaintingIcon;
    private int                      _iconWidth;
    private int                      _iconHeight;
    private long                     _fileSize;
    private final int                _resolution = ConfigurationHandler.defaultResolution;           // TODO: support for high-res paintings
    private String                   _warnings;
    private boolean                  _validImage = false;
    private boolean                  _changed    = false;

    protected final Minecraft        mc;
    protected final GuiScreen        _parentScreen;
    private CustomPaintingConfigItem _entryData;



    public GuiElementPaintingIconLoader(GuiScreen parentScreen, CustomPaintingConfigItem entryData) {
        this.mc = Minecraft.getMinecraft();
        this._parentScreen = parentScreen;
        updateConfigItem(entryData);
    }



    protected void updateConfigItem(CustomPaintingConfigItem entryData)
    {
        this._entryData = entryData;
        this.loadPaintingIcon();
    }


    public CustomPaintingConfigItem getConfigItem()
    {
        return this._entryData;
    }


    public void swapIsEnabled()
    {
        this._entryData.setIsEnabled(!this._entryData.getIsEnabled());
        this._changed = true;
    }


    public void setPaintingInfo(String title, String author)
    {
        this._entryData.setPaintingTitle(title);
        this._entryData.setPaintingAuthor(author);
        this._changed = true;
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
                    this._warnings = LocalizationHelper.translate(Category.ERROR, "not_found");
                }

                // Validate file size
                else if (this._fileSize > ConfigurationHandler.maxFileSize) {
                    this._warnings = LocalizationHelper.translate(Category.ERROR, "big_file");
                }

                /*
                 * // Compare file size (actual VS config) TODO: Maybe make it an option for the implemented class to auto-update file size "Size don't match, edit to update"
                 * else if (this._fileSize != this._entryData.getExpectedSize()) {
                 * this._warnings = LocalizationHelper.translate(Category.ERROR, "size_not_match");
                 * }
                 */

                // Validate file extension
                else if (!fileExtensionChecker.accept(iconFile.getParentFile(), iconFile.getName())) {
                    this._warnings = LocalizationHelper.translate(Category.ERROR, "invalid_extension");
                }

                // Try to load the image
                else {
                    iconStream = new BufferedInputStream(new FileInputStream(iconFile));
                    paintingIcon = ImageIO.read(iconStream);
                    iconStream.close();

                    this._iconWidth = paintingIcon.getWidth();
                    this._iconHeight = paintingIcon.getHeight();

                    // Updates the config entry with the dimensions
                    if (_entryData.getWidth() != _iconWidth || _entryData.getHeight() != _iconHeight) {
                        this._entryData.setSizePixels(_iconWidth, _iconHeight);
                        this._changed = true;
                    }

                    // Updates the config entry with the size
                    if (this._fileSize != this._entryData.getExpectedSize()) {
                        this._entryData.setActualFileSize(_fileSize);
                        this._changed = true;
                    }

                    // Validate the max dimensions
                    if (this._iconWidth > ConfigurationHandler.maxPaintingSize || this._iconHeight > ConfigurationHandler.maxPaintingSize) {
                        this._warnings = LocalizationHelper.translate(Category.ERROR, "big_pixels");
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

    protected boolean hasValidImage()
    {
        return this._validImage;
    }

    /**
     * <p>
     * Indicates if the config entry of this element changed and needs to be saved.
     * </p>
     * <p>
     * Currently this is used to update the image width/height to match the actual image.
     * </p>
     */
    public boolean changed()
    {
        return this._changed;
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
     * Extracts only the config elements of the given list of GuiElementPaintingIconLoader.
     */
    public static List<CustomPaintingConfigItem> extractConfig(List<? extends GuiElementPaintingIconLoader> list)
    {
        final List<CustomPaintingConfigItem> configList = new ArrayList<CustomPaintingConfigItem>();

        for (final GuiElementPaintingIconLoader item : list) {
            if (item != null && item._entryData != null) {
                configList.add(item._entryData);
            }
        }

        return configList;
    }


}
