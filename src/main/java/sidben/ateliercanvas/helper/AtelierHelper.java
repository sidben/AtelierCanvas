package sidben.ateliercanvas.helper;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;



/**
 * Helper class that can load custom paintings images.
 * 
 * @author sidben
 */
public class AtelierHelper
{

    private final Minecraft                            mc;
    private final TextureManager                       texMan;

    public final DynamicTexture                        blankTexture;

    private final LoadingCache<UUID, ResourceLocation> paintingsCache;



    // --------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------

    public AtelierHelper(Minecraft minecraft) {
        this.mc = minecraft;
        this.texMan = mc.renderEngine;


        // Blank Texture
        blankTexture = new DynamicTexture(16, 16);
        final int[] blankPixels = blankTexture.getTextureData();
        for (int i = 0; i < blankPixels.length; ++i) {
            blankPixels[i] = new Color(255, 255, 255).getRGB();         // OBS: Color(255, 255, 255).getRGB() == -1 == 0xFFFFFFFF (pure white, 100% alpha)
        }
        blankTexture.updateDynamicTexture();


        // Cache
        this.paintingsCache = CacheBuilder.newBuilder().maximumSize(ConfigurationHandler.maxPaintings).expireAfterAccess(ConfigurationHandler.expirationTime, TimeUnit.MINUTES)
                .build(new CacheLoader<UUID, ResourceLocation>()
                {
                    @Override
                    public ResourceLocation load(UUID key) throws Exception
                    {
                        ResourceLocation icon = null;
                        final CustomPaintingConfigItem entryData = ConfigurationHandler.findPaintingByUUID(key);

                        if (entryData != null && entryData.isValid()) {
                            String iconPath;
                            InputStream iconStream;
                            BufferedImage paintingIcon;
                            long fileSize;

                            // Sets the path of the painting file
                            iconPath = ConfigurationHandler.IMAGES_BASE_PATH + entryData.getPaintingFileName();


                            try {
                                final File iconFile = new File(mc.mcDataDir, iconPath);
                                fileSize = iconFile.length();
                                final ImageFilenameFilter fileExtensionChecker = new ImageFilenameFilter();


                                LogHelper.info(String.format("Adding custom painting to the cache: %s | %s", entryData.getPaintingFileName(), entryData.getUUID()));

                                // Validate if the file exists
                                if (!iconFile.exists()) {
                                    LogHelper.error("  File not found.");
                                }

                                // Validate file size
                                else if (fileSize > ConfigurationHandler.maxFileSize) {
                                    LogHelper.error("  File size above the config limit.");
                                }

                                // Compare file size (actual VS config)
                                else if (fileSize != entryData.getExpectedSize()) {
                                    LogHelper.error("  File size don't match the config value.");
                                }

                                // Validate file extension
                                else if (!fileExtensionChecker.accept(iconFile.getParentFile(), iconFile.getName())) {
                                    LogHelper.error("  Invalid file extension.");
                                }

                                // Try to load the image
                                else {
                                    iconStream = new BufferedInputStream(new FileInputStream(iconFile));
                                    paintingIcon = ImageIO.read(iconStream);
                                    iconStream.close();

                                    final int iconWidth = paintingIcon.getWidth();
                                    final int iconHeight = paintingIcon.getHeight();
                                    entryData.setSizePixels(iconWidth, iconHeight);

                                    // Validate the max dimensions
                                    if (iconWidth > ConfigurationHandler.maxPaintingSize || iconHeight > ConfigurationHandler.maxPaintingSize) {
                                        LogHelper.error("  Image dimensions above the config limit.");
                                    }

                                    else {
                                        icon = texMan.getDynamicTextureLocation("custom_painting_icon", new DynamicTexture(paintingIcon));
                                    }
                                }

                            } catch (final IOException e) {
                                icon = null;
                                LogHelper.error("  Error loading custom painting");
                                e.printStackTrace();

                            }
                        }


                        // Loads the broken icon, if the correct wasn't found
                        if (icon == null) {
                            final DynamicTexture dynamictexture = TextureUtil.missingTexture;
                            icon = texMan.getDynamicTextureLocation("missing_icon", dynamictexture);
                        }

                        return icon;
                    }
                });
    }



    // --------------------------------------------------------------
    // Methods
    // --------------------------------------------------------------

    public ResourceLocation getCustomPaintingImage(UUID uuid)
    {
        try {
            // Check if the painting is enabled
            final CustomPaintingConfigItem entryData = ConfigurationHandler.findPaintingByUUID(uuid);

            if (entryData != null && entryData.getIsEnabled()) {
                // Returns the cached item
                return this.paintingsCache.get(uuid);
            } else {
                // Returns a blank texture
                return texMan.getDynamicTextureLocation("blank_painting", this.blankTexture);
            }


        } catch (final ExecutionException e) {
            // Error loading the painting. Since this would be called every rendering tick, I can't call log methods here.

        }

        // If all fails, return the broken texture icon
        return texMan.getDynamicTextureLocation("missing_icon", TextureUtil.missingTexture);
    }



}
