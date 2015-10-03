package sidben.ateliercanvas.helper;

import net.minecraft.util.StatCollector;
import sidben.ateliercanvas.reference.Reference;



/**
 * Generates the language keys for localized resources.
 * 
 * @author sidben
 */
public class LocalizationHelper
{


    /**
     * Returns the language key of a given type.
     */
    public static String getLanguageKey(Category type, String name)
    {
        return Reference.ResourcesNamespace + type.getCategoryKey() + "." + name;
    }


    /**
     * Returns the translated value of a key that contains one or more arguments
     */
    public static String translateFormatted(Category type, String name, Object... values)
    {
        return StatCollector.translateToLocalFormatted(getLanguageKey(type, name), values);
    }


    /**
     * Returns the translated value of a key
     */
    public static String translate(Category type, String name)
    {
        return StatCollector.translateToLocal(getLanguageKey(type, name));
    }



    /**
     * Type of language key returned.
     */
    public enum Category {
        CONFIG("config"),
        CONFIG_PROPERTIES("config.prop"),
        CONFIG_PAINTING_INFO("config.painting_info"),
        CONFIG_EDITOR("config.editor"),
        ERROR("error"),
        WARNING("warning"),
        CREATIVE_TAB("creative_tab"),
        ITEM("item"),
        ITEM_CUSTOM_PAINTING("item.custom_painting");



        private final String _key;

        public String getCategoryKey()
        {
            return this._key;
        }


        Category(String key) {
            this._key = key;
        }


    }

}
