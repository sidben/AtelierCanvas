package sidben.ateliercanvas.world.storage;

import sidben.ateliercanvas.helper.LogHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;



public class PaintingData extends WorldSavedData
{

    private static final String identifier = "atelier";
    
    
    
    public int id;
    // NOTE: Will I want to save the actual username (that can change) or just an arbitrary name? For now 
    // I'll go with the second, to allow non-players to create paintings
    public String author;           
    public String name;
    public int[] pixels = new int[256];     // stackoverflow.com/questions/1086054/how-to-convert-int-to-byte

    
    // TODO: Adds controls for current row, column and max row/column for big paintings
    
    
    public PaintingData(String uniqueName) {
        super(uniqueName);
    }

    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        this.id = tag.getInteger("id");
        this.author = tag.getString("author");
        this.name = tag.getString("name");
        //this.pixels = tag.getByteArray("pixels");
        this.pixels = tag.getIntArray("pixels");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger("id", this.id);
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
    
    
    // NOTE: return boolean with result?
    // NOTE: How to check if a painting already exists? GUID? Probably GUID
    public static void AddPainting(World world, String author, String name, int[] imageData) {
        int newId = world.getUniqueDataId("canvas");
        String s = "canvas_" + newId;
        
        LogHelper.info("Hey, I'm adding a painting! -> " + s);
        
        PaintingData data = new PaintingData(s);
        world.setItemData(s, data);
        
        data.author = author;
        data.name = name;
        data.pixels = imageData;
        data.markDirty();
    }
    
}
