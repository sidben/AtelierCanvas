package sidben.ateliercanvas.reference;


/**
 * GlColor in integer format, as used by Minecraft. 
 *
 */
public class ColorTable
{
    
    /**
     * Pure white - #FFFFFF - 16777215
     */
    public static final int WHITE       = 0xFFFFFF;
    
    /**
     * 50% black - #808080 - 8421504
     */
    public static final int GRAY        = 0x808080;

    /**
     * 66% black - #AAAAAA - 11184810
     */
    public static final int LIGHT_GRAY        = 0xAAAAAA;

    /**
     * Yellow - #FFFF00 - 16776960
     */
    public static final int YELLOW      = 0xFFFF00;

    
    
    /**
     * Helper method to manually convert an integer to RGB or RGBA values.
     * Must be used in debug mode.
     */
    public static void FindColor(int color) {
        int r = (color >> 16 & 255);
        int g = (color & 255);
        int b = (color >> 8 & 255);
        int a = (color >> 24 & 255);

        //-------[Insert a breakpoint below]------------------------------
        System.out.println(r + ", " + g + ", " + b + ", " + a);
    }
    
}
