package sidben.ateliercanvas.world.storage;

import java.awt.Color;
import java.util.HashMap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import sidben.ateliercanvas.helper.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapData;



public class PaintingData extends WorldSavedData
{

    private static final String identifier = "canvas";
    
    
    /*
     * TODO: Think of a way to avoid adding a painting that already exists. Options:
     * 
     * 1) Each painting has a GUID. When players share their paintings, a GUID would
     * avoid adding the same image twice. Problem is, depending on the way it's shared,
     * GUID can be altered manually, defeating it's purpose.
     * 
     * The "addImage" command I have is an example, either the user manually inputs
     * a GUID or the code generates a new one for every command, both ways can bypass
     * the goal of having a GUID.
     * 
     * 
     * 2) Random pixel check. Step one compares random pixels, if they match, compares
     * all pixels to see if the image is the same. Probably slower than GUID...
     * 
     * 
     * 3) Hash check. If I can find a way to create a unique hash for each image, I can
     * implement the same goal of option 2. I have no idea if it would be faster or slower though. 
     * 
     */
    
    // public int id;
    
    // NOTE: Will I want to save the actual username (that can change) or just an arbitrary name? For now 
    // I'll go with the second, to allow non-players to create paintings
    public String author;
    
    public String name;
    
    public int[] pixels = new int[256];     // stackoverflow.com/questions/1086054/how-to-convert-int-to-byte (vanilla uses an array of byte for map colors)

    
    // TODO: Adds controls for current row, column and max row/column for big paintings
    
    
    public PaintingData(String uniqueName) {
        super(uniqueName);
    }

    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        //this.id = tag.getInteger("id");
        this.author = tag.getString("author");
        this.name = tag.getString("name");
        //this.pixels = tag.getByteArray("pixels");
        this.pixels = tag.getIntArray("pixels");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        //tag.setInteger("id", this.id);
        tag.setString("author", this.author);
        tag.setString("name", this.name);
        //tag.setByteArray("pixels", this.pixels);
        tag.setIntArray("pixels", this.pixels);
    }



    /*
    public static PaintingData get(World world) {
        PaintingData data = (PaintingData) world.mapStorage.loadData(PaintingData.class, identifier);
        if (data == null) {
            world.mapStorage.setData(identifier, (data = new PaintingData(identifier)));
        }
        return data;
    }
    */
    
    
    // NOTE: How to check if a painting already exists? (TODO)
    public static int addPainting(World world, String author, String name, int[] imageData) {
        int newId = world.getUniqueDataId(identifier);
        String s = identifier + "_" + newId;
        
        LogHelper.info("Hey, I'm adding a painting! -> " + s);
        
        PaintingData data = new PaintingData(s);
        world.setItemData(s, data);
        
        data.author = author;
        data.name = name;
        data.pixels = imageData;
        data.markDirty();
        
        return newId;
    }

    
    public static PaintingData getPainting(World world, int id) {
        PaintingData data = null;
        String s = identifier + "_" + id;
        
        LogHelper.info("   => map_0: " + world.loadItemData(MapData.class, "map_0"));
        LogHelper.info("   => map_1: " + world.loadItemData(MapData.class, "map_1"));
        LogHelper.info("   => canvas_0: " + world.loadItemData(PaintingData.class, "canvas_0"));
        LogHelper.info("   => canvas_1: " + world.loadItemData(PaintingData.class, "canvas_1"));
        LogHelper.info("   => canvas_2: " + world.loadItemData(PaintingData.class, "canvas_2"));
        LogHelper.info("   => canvas_3: " + world.loadItemData(PaintingData.class, "canvas_3"));
        LogHelper.info("   => canvas_4: " + world.loadItemData(PaintingData.class, "canvas_4"));
        
        
        data = (PaintingData)world.loadItemData(PaintingData.class, s);
        
        return data;
    }
    
    
    
    @SideOnly(Side.CLIENT)
    protected static HashMap<String, DynamicTexture> loadedPaintings = new HashMap<String, DynamicTexture>();
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final TextureManager texMan = mc.renderEngine;
        
    @SideOnly(Side.CLIENT)
    public static ResourceLocation getTexture(World world, int id) {
        String mapName = identifier + "_" + id;
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
            
            PaintingData data = getPainting(world, id);
            
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
            
        }

        
        LogHelper.info("    texture = " + texture);
        
        if (texture != null) {
            return texMan.getDynamicTextureLocation(mapName, texture);
        }
        return null;
    }

    
}
