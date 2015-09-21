package sidben.ateliercanvas.helper;

import java.awt.Color;
import java.util.HashMap;
import sidben.ateliercanvas.network.MessagePaintingData;
import sidben.ateliercanvas.world.storage.PaintingData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



/**
 * Old helper class, leaving in the code for future reference.
 * 
 * @author sidben
 */
@Deprecated
public class AtelierHelperOld
{

    @SideOnly(Side.CLIENT)
    protected static HashMap<String, DynamicTexture> loadedPaintings = new HashMap<String, DynamicTexture>();

    
    public static final String identifier = "canvas";

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final TextureManager texMan = mc.renderEngine;


    
    public static String getPaintingName(int id) {
        return identifier + "_" + id;
    }
    
    
    

    // NOTE: How to check if a painting already exists? (TODO)
    public static int addPaintingData(World world, String author, String name, int[] imageData) {
        int newId = world.getUniqueDataId(identifier);
        String s = getPaintingName(newId);
        
        LogHelper.info("Hey, I'm adding a painting! -> " + s);
        
        PaintingData data = new PaintingData(s);
        world.setItemData(s, data);
        
        data.author = author;
        data.name = name;
        data.pixels = imageData;
        data.markDirty();
        
        return newId;
    }
    

    @SideOnly(Side.CLIENT)
    public static void addPaintingData(MessagePaintingData message) {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        String s = getPaintingName(message.getCanvasId());
        
        LogHelper.info("Hey, I'm adding a painting (by message)! -> " + s);
        
        PaintingData data = new PaintingData(s);
        world.setItemData(s, data);
        
        data.author = message.getAuthor();
        data.name = message.getName();
        data.pixels = message.getPixels();
        data.markDirty();
    }

    
    public static PaintingData getPaintingData(World world, int id) {
        String s = getPaintingName(id);
        PaintingData data = (PaintingData)world.loadItemData(PaintingData.class, s);
        
        /*
        LogHelper.info("   => map_0: " + world.loadItemData(MapData.class, "map_0"));
        LogHelper.info("   => map_1: " + world.loadItemData(MapData.class, "map_1"));
        LogHelper.info("   => canvas_0: " + world.loadItemData(PaintingData.class, "canvas_0"));
        LogHelper.info("   => canvas_1: " + world.loadItemData(PaintingData.class, "canvas_1"));
        LogHelper.info("   => canvas_2: " + world.loadItemData(PaintingData.class, "canvas_2"));
        LogHelper.info("   => canvas_3: " + world.loadItemData(PaintingData.class, "canvas_3"));
        LogHelper.info("   => canvas_4: " + world.loadItemData(PaintingData.class, "canvas_4"));
        */
        

        /*
        // Creates a new PaintingData (copy from map, don't think I'll keep it)
        if (data == null && !world.isRemote)
        {
            data = new PaintingData(s);
            data.author = "";
            data.name = "";
            data.pixels = new int[256];
            data.markDirty();
            
            world.setItemData(s, data);
        }
        */

        return data;
    }
    
    
    
        
    @SideOnly(Side.CLIENT)
    public static ResourceLocation getTexture(World world, int id) {
        String mapName = getPaintingName(id);
        String uniqueName = identifier + "/" + mapName;
        DynamicTexture texture = null;
        int[] pixels = null;
        
        
        LogHelper.info("getTexture() - " + id);
        LogHelper.info("    name, uniquename: [" + mapName + "] [" + uniqueName + "]");

        
        // TODO: check if the "loadedPaintings" cache will conflict with multiple worlds
        // world.getWorldInfo().getWorldName()
        
        if (loadedPaintings.containsKey(uniqueName)) {
            LogHelper.info("    Found in cache");
            texture = loadedPaintings.get(uniqueName);
            
        }
        else {
            LogHelper.info("    Creating new...");
            
            PaintingData data = getPaintingData(world, id);
            
            LogHelper.info("    data = " + data);
            
            
            if (data == null) {
                LogHelper.info("    Creating random 'art'");
                
                // Painting not found, creates a random picture
                texture = new DynamicTexture(16, 16);
                pixels = texture.getTextureData();

                pixels[0] = new Color(255, 255, 255).getRGB();
                for (int i = 1; i < pixels.length; ++i)
                {
                    pixels[i] = new Color(world.rand.nextInt(256), world.rand.nextInt(256), world.rand.nextInt(256)).getRGB();
                }
                texture.updateDynamicTexture();

            }
            else {
                LogHelper.info("    Loading existing art");
                // TODO: support for bigger paintings
                
                texture = new DynamicTexture(16, 16);
                pixels = texture.getTextureData();
                
                if (data.pixels.length != pixels.length) {
                    LogHelper.error("Wrong painting size, expected " + pixels.length + ", found " + data.pixels.length);
                }
                else {
                    for (int i = 0; i < pixels.length; ++i)
                    {
                        pixels[i] = data.pixels[i];
                    }
                    texture.updateDynamicTexture();
                }

            }
            
            loadedPaintings.put(uniqueName, texture);
        }

        
        LogHelper.info("    texture = " + texture);
        
        if (texture != null) {
            return texMan.getDynamicTextureLocation(uniqueName, texture);
        }
        return null;
    }
    
    
    
}
