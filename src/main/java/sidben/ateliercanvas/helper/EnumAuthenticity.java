package sidben.ateliercanvas.helper;

import java.util.Random;
import net.minecraft.util.StatCollector;
import sidben.ateliercanvas.handler.ConfigurationHandler;


/**
 * Represents the authenticity of a painting. Currently the authenticity have no effect
 * other than change the rarity of the item.
 * 
 * @author sidben
 */
public enum EnumAuthenticity {

    /** An authentic or original painting */
    ORIGINAL(1, "original"),

    /** A simple copy of the original work */
    COPY(2, "copy"),

    /** The copy of a copy */
    FORGERY(4, "copy_copy");



    private final byte   _id;
    private final String _langKey;


    private EnumAuthenticity(int id, String languageKey) {
        this._id = (byte) id;
        this._langKey = languageKey;
    }



    public byte getId()
    {
        return _id;
    }

    public String getTranslatedName()
    {
        return StatCollector.translateToLocal("sidben.ateliercanvas:item.custom_painting." + this._langKey);
    }



    public static EnumAuthenticity parse(int id)
    {
        final EnumAuthenticity[] values = EnumAuthenticity.values();
        final int j = values.length;

        for (int i = 0; i < j; i++) {
            final EnumAuthenticity value = values[i];
            if (value.getId() == id) {
                return value;
            }
        }

        return null;
    }


    public static EnumAuthenticity getRandom()
    {
        final Random rand = new Random();
        final int luckyDraw = rand.nextInt(100) + 1;

        if (luckyDraw <= ConfigurationHandler.chanceAuthentic) {
            return EnumAuthenticity.ORIGINAL;
        } else if (luckyDraw <= ConfigurationHandler.chanceForgery + ConfigurationHandler.chanceAuthentic) {
            return EnumAuthenticity.FORGERY;
        } else {
            return EnumAuthenticity.COPY;
        }
    }

}
