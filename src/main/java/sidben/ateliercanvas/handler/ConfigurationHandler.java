package sidben.ateliercanvas.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import sidben.ateliercanvas.reference.Reference;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ConfigurationHandler
{

    public static final String  CATEGORY_PAINTINGS         = "paintings";

    
    /** Array with info of all paintings installed */
    public static String[]      installedPaintings;
    
    
    public static int           dummy;
    public static String        dummyPic;
    
    
    
    
    
    // Instance
    public static Configuration config;



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


        /*
         *             
            // Numbers category
            numbersList.add((new DummyConfigElement<Integer>("basicInteger", 42, ConfigGuiType.INTEGER, "fml.config.sample.basicInteger")));
            numbersList.add((new DummyConfigElement<Integer>("boundedInteger", 42, ConfigGuiType.INTEGER, "fml.config.sample.boundedInteger", -1, 256)));
            numbersList.add((new DummyConfigElement<Integer>("sliderInteger", 2000, ConfigGuiType.INTEGER, "fml.config.sample.sliderInteger", 100, 10000)).setCustomListEntryClass(NumberSliderEntry.class));
            numbersList.add(new DummyConfigElement<Double>("basicDouble", 42.4242D, ConfigGuiType.DOUBLE, "fml.config.sample.basicDouble"));
            numbersList.add(new DummyConfigElement<Double>("boundedDouble", 42.4242D, ConfigGuiType.DOUBLE, "fml.config.sample.boundedDouble", -1.0D, 256.256D));
            numbersList.add(new DummyConfigElement<Double>("sliderDouble", 42.4242D, ConfigGuiType.DOUBLE, "fml.config.sample.sliderDouble", -1.0D, 256.256D).setCustomListEntryClass(NumberSliderEntry.class));

         */
        
        // Load properties
        prop = config.get(CATEGORY_PAINTINGS, "dummy", 7, "", 0, 10);
        prop.setLanguageKey("sidben.ateliercanvas.config.dummy");
        dummy = prop.getInt(7);
        prop.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        propOrder.add(prop.getName());

        /*
        prop = config.get(CATEGORY_PAINTINGS, "dummy_pic", "");
        prop.setLanguageKey("sidben.ateliercanvas.config.dummy_pic");
        dummyPic = prop.getString();
        prop.setConfigEntryClass(PaintingEntry.class);
        propOrder.add(prop.getName());
        */

        prop = config.get(CATEGORY_PAINTINGS, "painting_list", new String[] {});
        prop.setLanguageKey("sidben.ateliercanvas.config.painting_list");
        installedPaintings = prop.getStringList();
        propOrder.add(prop.getName());

        config.setCategoryPropertyOrder(CATEGORY_PAINTINGS, propOrder);



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
    
}
