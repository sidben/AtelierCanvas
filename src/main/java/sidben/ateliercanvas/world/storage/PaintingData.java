package sidben.ateliercanvas.world.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;


@Deprecated
public class PaintingData extends WorldSavedData
{



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
     */

    // public int id;

    // NOTE: Will I want to save the actual username (that can change) or just an arbitrary name? For now
    // I'll go with the second, to allow non-players to create paintings
    public String author;

    public String name;

    public int[]  pixels = new int[256];     // stackoverflow.com/questions/1086054/how-to-convert-int-to-byte (vanilla uses an array of byte for map colors)


    // TODO: Adds controls for current row, column and max row/column for big paintings


    public PaintingData(String uniqueName) {
        super(uniqueName);
    }


    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        // this.id = tag.getInteger("id");
        this.author = tag.getString("author");
        this.name = tag.getString("name");
        // this.pixels = tag.getByteArray("pixels");
        this.pixels = tag.getIntArray("pixels");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        // tag.setInteger("id", this.id);
        tag.setString("author", this.author);
        tag.setString("name", this.name);
        // tag.setByteArray("pixels", this.pixels);
        tag.setIntArray("pixels", this.pixels);
    }



    /*
     * public static PaintingData get(World world) {
     * PaintingData data = (PaintingData) world.mapStorage.loadData(PaintingData.class, identifier);
     * if (data == null) {
     * world.mapStorage.setData(identifier, (data = new PaintingData(identifier)));
     * }
     * return data;
     * }
     */


}
