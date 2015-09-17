package sidben.ateliercanvas.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import sidben.ateliercanvas.reference.Reference;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ConfigurationHandler
{

    public static final String  CATEGORY_PAINTINGS         = "paintings";

    
    
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


        // Load properties
        /*
        prop = config.get(CATEGORY_TRADING, "max_stores", DEFAULT_maxStores, "", 16, 1024);
        prop.setLanguageKey("sidben.redstonejukebox.config.max_stores");
        maxStores = prop.getInt(DEFAULT_maxStores);
        propOrder.add(prop.getName());
        */

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
