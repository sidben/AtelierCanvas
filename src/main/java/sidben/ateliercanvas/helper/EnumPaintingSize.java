package sidben.ateliercanvas.helper;


@Deprecated
public enum EnumPaintingSize
{

    SIZE_1x1(0, 1, 1),
    SIZE_2x1(1, 2, 1);

    

    public final int id;
    public final int tileWidth;
    public final int tileHeight;
    public final int pixelArraySize;
    
    

    private EnumPaintingSize(int id, int width, int height) {
        this.id = id;
        this.tileWidth = width;
        this.tileHeight = height;
        this.pixelArraySize = (width * 16) * (height * 16);
    }

    
    
    public static EnumPaintingSize getSizeFromId(int id) {
        EnumPaintingSize[] values = EnumPaintingSize.values();
        int j = values.length;
        
        for (int i = 0; i < j; i++)
        {
            EnumPaintingSize value = values[i];
            if (value.id == id) return value;
        }
        
        return null;
    }
    
}
