package sidben.ateliercanvas.world.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;



public class PaintingData extends WorldSavedData
{

    public byte[] pixels = new byte[256];
    public String author;
    // TODO: Adds controls for current row, column and max row/column for big paintings
    
    
    public PaintingData(String id) {
        super(id);
    }

    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        this.author = tag.getString("author");
        this.pixels = tag.getByteArray("pixels");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setString("author", this.author);
        tag.setByteArray("pixels", this.pixels);
    }

    // DynamicTexture
}
