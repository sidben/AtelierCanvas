package sidben.ateliercanvas.network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import scala.actors.threadpool.Arrays;
import sidben.ateliercanvas.world.storage.PaintingData;
import sidben.ateliercanvas.helper.EnumPaintingSize;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



/**
 * Used to send the custom painting info to the clients
 * (including the painting itself, in a byte array). 
 *
 */
public class MessagePaintingData implements IMessage
{

    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    
    private int _canvasId;
    private String _author;
    private String _name;
    private EnumPaintingSize _size;
    private int[] _pixels;

    
    
    
    public MessagePaintingData() {}

    public MessagePaintingData(int canvasId, PaintingData data) {
        this._canvasId = canvasId;
        if (data == null) 
        {
            this._author = "Unknown";
            this._name = "Custom Painting";
            this._size = EnumPaintingSize.SIZE_1x1; 
            this._pixels = new int[1];
            this._pixels[0] = -1;
        }
        else 
        {
            this._author = data.author;
            this._name = data.name;
            this._size = EnumPaintingSize.SIZE_1x1;   // TODO: Size on the painting data 
            this._pixels = data.pixels;
        }
    }

    
    
    @SideOnly(Side.CLIENT)
    public int getCanvasId()
    {
        return this._canvasId;
    }

    @SideOnly(Side.CLIENT)
    public String getAuthor()
    {
        return this._author;
    }

    @SideOnly(Side.CLIENT)
    public String getName()
    {
        return this._name;
    }

    @SideOnly(Side.CLIENT)
    public int[] getPixels()
    {
        return this._pixels;
    }

    

    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this._canvasId = buf.readInt();

        /*
        byte[] authorBuffer = new byte[30];
        buf.readBytes(authorBuffer);
        this._author = new String(authorBuffer, UTF8_CHARSET);
        */
        this._author = ByteBufUtils.readUTF8String(buf);
        
        /*
        byte[] nameBuffer = new byte[30];
        buf.readBytes(nameBuffer);
        this._name = new String(nameBuffer, UTF8_CHARSET);
        */
        this._name = ByteBufUtils.readUTF8String(buf);

        this._size = EnumPaintingSize.getSizeFromId(ByteBufUtils.readVarShort(buf));
        
        byte[] imageBuffer = new byte[this._size.pixelArraySize * 4];
        buf.readBytes(imageBuffer);
        
        IntBuffer intBuf = ByteBuffer.wrap(imageBuffer).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
        this._pixels = new int[intBuf.remaining()];
        intBuf.get(this._pixels);
    }

    
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this._canvasId);
        ByteBufUtils.writeUTF8String(buf, this._author);
        ByteBufUtils.writeUTF8String(buf, this._name);
        ByteBufUtils.writeVarShort(buf, this._size.id);
        
        ByteBuffer byteBuf = ByteBuffer.allocate(this._size.pixelArraySize * 4);        
        IntBuffer intBuf = byteBuf.asIntBuffer();
        intBuf.put(this._pixels);
        buf.writeBytes(byteBuf.array());
    }

    
    
    
    @Override 
    public String toString() {
        StringBuilder r = new StringBuilder();
        
        r.append("Canvas ID = ");
        r.append(this._canvasId);
        r.append(", Name = ");
        r.append(this._name);
        r.append(", Author = ");
        r.append(this._author);
        r.append(", Size = ");
        r.append(this._size);
        r.append(", Pixels length = ");
        r.append(this._pixels.length);
        r.append(", First values = [");
        if (this._pixels.length >= 10) {
            for (int i = 0; i < 10; i++) {
                r.append(this._pixels[i]);
                r.append(" ");            
            }
        }
        r.append("]");

        return r.toString();
    }

    
}
