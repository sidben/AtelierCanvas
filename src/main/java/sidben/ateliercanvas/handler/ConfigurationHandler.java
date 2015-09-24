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
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.reference.Reference;
import cpw.mods.fml.client.config.GuiConfigEntries;
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



    private static final int                      DEFAULT_maxFileSize        = 30;                               // NOTE: the default file size is in KBytes to make the property slider more friendly
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
        final List<String> propOrder = new ArrayList<String>();
        Property prop;
        ConfigCategory cat;



        // Load properties
        prop = config.get(Configuration.CATEGORY_GENERAL, "simple_recipes", DEFAULT_simpleRecipes);
        prop.setLanguageKey(getLanguageKey("simple_recipes"));
        simpleRecipes = prop.getBoolean(DEFAULT_simpleRecipes);
        propOrder.add(prop.getName());

        prop = config.get(Configuration.CATEGORY_GENERAL, "max_filesize_kb", DEFAULT_maxFileSize, "", 10, 1024);     // 1048576 bytes == 1024KB == 1 MB
        prop.setLanguageKey(getLanguageKey("max_filesize_kb"));
        maxFileSize = (prop.getInt(DEFAULT_maxFileSize) * 1024);
        prop.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        propOrder.add(prop.getName());

        prop = config.get(Configuration.CATEGORY_GENERAL, "max_image_size", DEFAULT_maxPaintingSize, "", 64, 128);
        prop.setLanguageKey(getLanguageKey("max_image_size"));
        maxPaintingSize = prop.getInt(DEFAULT_maxPaintingSize);
        prop.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        propOrder.add(prop.getName());

        prop = config.get(Configuration.CATEGORY_GENERAL, "date_format", DEFAULT_paintingDateFormat);
        prop.setLanguageKey(getLanguageKey("date_format"));
        paintingDateFormat = prop.getString();
        prop.setValidValues(new String[] { "yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy", "dd-MMM-yyyy", "yyyy-MMM-dd" });
        propOrder.add(prop.getName());


        config.setCategoryPropertyOrder(Configuration.CATEGORY_GENERAL, propOrder);



        cat = config.getCategory(CATEGORY_PAINTINGS);
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

        LogHelper.info("Loaded complete, [" + mahPaintings.size() + "] entries found.");



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



    /**
     * Returns the full language key for elements of this GUI.
     */
    protected static String getLanguageKey(String name)
    {
        return "sidben.ateliercanvas.config.prop." + name;
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


        // Current installed paintings
        final String[] currentFilesArray = new String[ConfigurationHandler.mahPaintings.size()];

        for (int i = 0; i < ConfigurationHandler.mahPaintings.size(); i++) {
            currentFilesArray[i] = ConfigurationHandler.mahPaintings.get(i).getPaintingFileName().toLowerCase();
        }

        final List<String> currentFiles = Arrays.asList(currentFilesArray);


        // Sub-folder of the mod
        final File folder = new File(Minecraft.getMinecraft().mcDataDir, ConfigurationHandler.IMAGES_BASE_PATH);

        // Look all files in the folder, but filter by the valid extension types
        for (final File fileEntry : folder.listFiles(new ImageFilenameFilter())) {
            final String name = fileEntry.getName().toLowerCase();

            // If the filename is not installed, adds to the return list
            if (!currentFiles.contains(name)) {
                final CustomPaintingConfigItem item = new CustomPaintingConfigItem(name, true, fileEntry.length(), name, "");
                fakeConfigList.add(item);
            }
        }


        return fakeConfigList;
    }



    public static void addNewItemAndSaveConfig(CustomPaintingConfigItem item)
    {
        if (item == null) {
            return;
        }
        if (item.isValid()) {

            // Adds the item
            ConfigurationHandler.mahPaintings.add(item);
            updateAndSaveConfig();

        } else {
            LogHelper.info("    Error importing a config entry: [" + item.getValiadtionErrors() + "]");
        }
    }



    /**
     * Clear current paintings config list and updates it with values from the current inner list.
     */
    public static void updateAndSaveConfig()
    {

        // Clear all content of the category
        ConfigurationHandler.config.getCategory(CATEGORY_PAINTINGS).clear();

        // Re-adds all the valid entries
        Property configProp;
        int c = 0;

        for (final CustomPaintingConfigItem item : mahPaintings) {
            final String configKey = String.format("%s_%03d", ConfigurationHandler.PAINTINGS_ARRAY_KEY, c);

            configProp = ConfigurationHandler.config.get(CATEGORY_PAINTINGS, configKey, new String[] {});
            configProp.set(item.ToStringArray());

            c++;
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
                if (item.getUUID() == uuid) {
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


}
