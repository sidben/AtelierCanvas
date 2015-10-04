package sidben.ateliercanvas.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import scala.actors.threadpool.Arrays;
import sidben.ateliercanvas.helper.ImageFilenameFilter;
import sidben.ateliercanvas.helper.LocalizationHelper;
import sidben.ateliercanvas.helper.LocalizationHelper.Category;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.reference.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;


public class ConfigurationHandler
{

    public static final String                    CATEGORY_PAINTINGS         = "paintings";
    public static final String                    CATEGORY_IMPORT            = "paintings_import";
    public static final String                    CATEGORY_EXPORT            = "paintings_export";
    public static final String                    PAINTINGS_ARRAY_KEY        = "painting_list";
    public static final String                    IMAGES_BASE_PATH           = "config/AtelierCanvas_Paintings/";
    public static final UUID                      EMPTY_UUID                 = UUID.fromString("00000000-0000-0000-0000-000000000000");



    /** List with info of all paintings installed */
    private static List<CustomPaintingConfigItem> mahPaintings;

    /** List with UUIDs of enabled paintings */
    private static List<UUID>                     visiblePaintings;

    /** Largest file size (in bytes) accepted by the mod */
    public static long                            maxFileSize;

    /** Format in which the painting dates (created / last updated) will be displayed */
    public static String                          paintingDateFormat;

    /** Maximum painting size, in pixels, accepted by the mod. */
    public static int                             maxPaintingSize;

    /** Default resolution of paintings */
    public static final int                       minPaintingSize            = 16;

    /** Default resolution of paintings */
    public static final int                       defaultResolution          = 16;

    /** Maximum amount of paintings cached */
    public static final int                       maxPaintings               = 256;

    /** Time in minutes a painting will remain in cache */
    public static final int                       expirationTime             = 10;

    /** Chance of a random painting giving an original/authentic painting */
    public static final int                       chanceAuthentic            = 5;

    /** Chance of a random painting giving a forgery */
    public static final int                       chanceForgery              = 15;

    /** Activates simpler crafting recipes, if the player plays on peaceful or just wants to use paintings quickly */
    public static boolean                         simpleRecipes;



    private static final int                      DEFAULT_maxFileSize        = 30;                                                     // NOTE: the default file size is in KBytes to make the property slider more friendly
    private static final int                      DEFAULT_maxPaintingSize    = 64;
    private static final String                   DEFAULT_paintingDateFormat = "yyyy-MM-dd";
    private static final boolean                  DEFAULT_simpleRecipes      = false;



    // Instance
    public static Configuration                   config;



    public static void init(File configFile)
    {

        // Create configuration object from config file
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }

    }



    private static void loadConfig()
    {

        // Load properties - general
        simpleRecipes = config.getBoolean("simple_recipes", Configuration.CATEGORY_GENERAL, DEFAULT_simpleRecipes, "", LocalizationHelper.getLanguageKey(Category.CONFIG_PROPERTIES, "simple_recipes"));
        maxFileSize = config.getInt("max_filesize_kb", Configuration.CATEGORY_GENERAL, DEFAULT_maxFileSize * 1024, 10, 1024, "", LocalizationHelper.getLanguageKey(Category.CONFIG_PROPERTIES, "max_filesize_kb")) * 1024;
        maxPaintingSize = config.getInt("max_image_size", Configuration.CATEGORY_GENERAL, DEFAULT_maxPaintingSize, 64, 128, "", LocalizationHelper.getLanguageKey(Category.CONFIG_PROPERTIES, "max_image_size"));
        paintingDateFormat = config.getString("date_format", Configuration.CATEGORY_GENERAL, DEFAULT_paintingDateFormat, "", new String[] { "yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy", "dd-MMM-yyyy", "yyyy-MMM-dd" }, LocalizationHelper.getLanguageKey(Category.CONFIG_PROPERTIES, "date_format"));



        // Custom paintings category
        final ConfigCategory cat = config.getCategory(CATEGORY_PAINTINGS);
        cat.setComment(CustomPaintingConfigItem.getArrayDescription());


        // Loads custom paintings
        LogHelper.info("Loading custom paintings config info...");
        mahPaintings = new ArrayList<CustomPaintingConfigItem>();
        visiblePaintings = new ArrayList<UUID>();

        for (final Property item : cat.getOrderedValues()) {
            if (item.getName().startsWith(PAINTINGS_ARRAY_KEY)) {
                final String[] content = item.getStringList();
                final CustomPaintingConfigItem configItem = new CustomPaintingConfigItem(content);

                if (configItem.isValid()) {
                    mahPaintings.add(configItem);
                    if (configItem.getIsEnabled()) {
                        visiblePaintings.add(configItem.getUUID());
                    }
                } else {
                    LogHelper.info("    Error loading a config entry: [" + configItem.getValiadtionErrors() + "]");
                }
            }
        }

        LogHelper.info("Loading complete, [" + mahPaintings.size() + "] entries found.");



        // saving the configuration to its file
        if (config.hasChanged()) {
            config.save();
        }
    }



    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equalsIgnoreCase(Reference.ModID)) {
            // Resync config
            loadConfig();
        }
    }



    // -------------------------------------------------------------------------
    // Custom Paintings properties - Currently installed
    // -------------------------------------------------------------------------

    /**
     * <p>
     * Returns all the paintings that were loaded from the config file.
     * </p>
     * <p>
     * This means all paintings which <b>config is valid</b> (have all required fields), but it returns paintings enabled and disabled. At this point, the config makes no check if the files actually exists or have a proper size, those rules are checked before rendering the painting.
     * </p>
     * 
     * @see sidben.ateliercanvas.helper.AtelierHelper
     */
    public static List<CustomPaintingConfigItem> getAllMahGoodPaintings()
    {
        return mahPaintings;
    }



    /**
     * <p>
     * Returns a list with all files in the paintings config folder that are <b>not</b> already installed in the config file. (I bet you would never guess by the method name)
     * </p>
     * <p>
     * The code only checks the file names, so it will return files that are not already in the config. If a file is configured with the wrong size or dimensions, it's still considered 'installed'.
     * </p>
     * <p>
     * The list returned by this method is not persisted anywhere.
     * </p>
     * 
     * @see sidben.ateliercanvas.client.gui.GuiScreenCustomPaintingsAddFileSelector
     */
    @SuppressWarnings("unchecked")
    public static List<CustomPaintingConfigItem> getPaintingsInConfigFolderNotInstalled()
    {
        final List<CustomPaintingConfigItem> fakeConfigList = new ArrayList<CustomPaintingConfigItem>();
        boolean folderOk = false;


        // Current installed paintings
        final String[] currentFilesArray = new String[ConfigurationHandler.mahPaintings.size()];

        for (int i = 0; i < ConfigurationHandler.mahPaintings.size(); i++) {
            currentFilesArray[i] = ConfigurationHandler.mahPaintings.get(i).getPaintingFileName().toLowerCase();
        }

        final List<String> currentFiles = Arrays.asList(currentFilesArray);


        // Sub-folder of the mod
        final File folder = new File(Minecraft.getMinecraft().mcDataDir, ConfigurationHandler.IMAGES_BASE_PATH);

        // Creates the folder if it doesn't exist
        if (folder.exists() && folder.isDirectory()) {
            folderOk = true;
        } else {
            if (folder.exists() && !folder.isDirectory()) {
                LogHelper.error("Error: There is a file named [" + ConfigurationHandler.IMAGES_BASE_PATH + "], this will cause problems");
            }

            folderOk = folder.mkdirs();
            if (!folderOk) {
                LogHelper.error("Error creating the custom paintings folder");
            }
        }


        if (folderOk) {
            // Look all files in the folder, but filter by the valid extension types
            for (final File fileEntry : folder.listFiles(new ImageFilenameFilter())) {
                final String name = fileEntry.getName().toLowerCase();

                // If the filename is not installed, adds to the return list
                if (!currentFiles.contains(name)) {
                    final CustomPaintingConfigItem item = new CustomPaintingConfigItem(name, true, fileEntry.length(), name, "");
                    fakeConfigList.add(item);
                }
            }
        }


        return fakeConfigList;
    }



    /**
     * Updates the item with the same UUID of the given element. If the item UUID
     * is not found, it's added to the list.
     */
    public static void addOrUpdateEntry(CustomPaintingConfigItem item)
    {
        if (item == null || item.getUUID() == EMPTY_UUID) {
            return;
        }
        if (item.isValid()) {

            // Tries to find the item
            final CustomPaintingConfigItem currentEntry = findPaintingByUUID(item.getUUID());

            if (currentEntry != null) {
                // Updates the existing entry
                if (!currentEntry.equals(item)) {
                    currentEntry.updateEntryFrom(item);
                }
            } else {
                // Adds a new entry
                ConfigurationHandler.mahPaintings.add(item);
            }

        } else {
            LogHelper.info("    Error updating a config entry: [" + item.getValiadtionErrors() + "]");

        }
    }


    /**
     * Removes the item with the informed UUID from the config file.
     */
    public static void removeEntry(UUID uuid)
    {

        // Tries to find the item
        final CustomPaintingConfigItem targetEntry = findPaintingByUUID(uuid);

        if (targetEntry != null) {
            // Removes the existing entry
            ConfigurationHandler.mahPaintings.remove(targetEntry);
        }

    }



    /**
     * Clear current paintings config list and updates it with values from the current inner list.
     */
    public static void updateAndSaveConfig()
    {
        final List<UUID> uniqueEntries = new ArrayList<UUID>();

        // Clear all content of the category
        ConfigurationHandler.config.getCategory(CATEGORY_PAINTINGS).clear();
        visiblePaintings.clear();

        // Re-adds all the valid entries
        Property configProp;
        int c = 0;

        for (final CustomPaintingConfigItem item : mahPaintings) {
            // Check for duplicate UUIDs
            if (!uniqueEntries.contains(item.getUUID())) {

                // Updates the config
                final String configKey = String.format("%s_%03d", ConfigurationHandler.PAINTINGS_ARRAY_KEY, c);

                configProp = ConfigurationHandler.config.get(CATEGORY_PAINTINGS, configKey, new String[] {});
                configProp.set(item.ToStringArray());

                // Updates the visible paintings array
                if (item.getIsEnabled()) {
                    visiblePaintings.add(item.getUUID());
                }

                uniqueEntries.add(item.getUUID());
                c++;

            }

            // TODO: remove the duplicate entries from mahPaintings (Note: what can cause duplicate entries on the current version?)
        }

        // Saves the config file
        ConfigurationHandler.config.save();
    }



    /**
     * Looks on the current custom paintings installed for an element with the given UUID.
     * 
     * @param uuid
     *            UUID
     * @return Config element of the given painting or NULL
     */
    public static CustomPaintingConfigItem findPaintingByUUID(UUID uuid)
    {
        if (uuid != null && mahPaintings.size() > 0) {
            for (final CustomPaintingConfigItem item : mahPaintings) {
                if (item.getUUID().equals(uuid)) {
                    return item;
                }
            }
        }

        return null;
    }


    /**
     * Looks on the current custom paintings installed for an element with the given UUID.
     * 
     * @param uuid
     *            UUID in string format
     * @return Config element of the given painting or NULL
     */
    public static CustomPaintingConfigItem findPaintingByUUID(String uuidString)
    {
        UUID uuid = null;

        try {
            uuid = UUID.fromString(uuidString);
        } catch (final IllegalArgumentException e) {
            return null;
        }

        return findPaintingByUUID(uuid);
    }



    /**
     * Returns a random item from the current installed <b>and enabled</b> custom paintings.
     * 
     * @return Config element of a random painting or NULL
     */
    public static CustomPaintingConfigItem getRandomPainting()
    {
        if (mahPaintings.size() > 0 && visiblePaintings.size() > 0) {
            final Random rand = new Random();

            // Draws a random entry from the array of enabled paintings
            final int luckyDraw = rand.nextInt(visiblePaintings.size());

            // Returns the painting with the drawn UUID
            return findPaintingByUUID(visiblePaintings.get(luckyDraw));
        }

        return null;
    }



    /**
     * Check if the given UUID is present in the config file and also
     * if the UUID is enabled.
     */
    public static boolean isUUIDEnabled(String uuidString)
    {
        UUID uuid = null;

        try {
            uuid = UUID.fromString(uuidString);
            return isUUIDEnabled(uuid);
        } catch (final IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Check if the given UUID is present in the config file and also
     * if the UUID is enabled.
     */
    public static boolean isUUIDEnabled(UUID uuid)
    {
        return visiblePaintings.contains(uuid);
    }

}
