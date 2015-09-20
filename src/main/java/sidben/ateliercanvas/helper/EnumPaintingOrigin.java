package sidben.ateliercanvas.helper;


/**
 * Origin of the image file.
 * 
 * @author sidben
 */
public enum EnumPaintingOrigin
{
    /** Imported from a local file */
    FILE,
    
    /** Imported from a URL */
    URL,
    
    /** Created in-game by the player (singleplayer). */
    INGAME_SSP,
    
    /** Created in-game by any player (multiplayer) */
    INGAME_SMP
}
