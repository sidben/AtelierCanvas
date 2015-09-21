package sidben.ateliercanvas.network;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



/**
 * Used to send the custom painting unique ID to the clients
 */
public class MessageCustomPaintingInfo implements IMessage
{

    private String _uuid;



    public MessageCustomPaintingInfo() {
    }

    public MessageCustomPaintingInfo(String uuid) {
        this._uuid = uuid;
    }



    @SideOnly(Side.CLIENT)
    public String getUUID()
    {
        return this._uuid;
    }



    @Override
    public void fromBytes(ByteBuf buf)
    {
        this._uuid = ByteBufUtils.readUTF8String(buf);
    }


    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this._uuid);
    }



    @Override
    public String toString()
    {
        final StringBuilder r = new StringBuilder();

        r.append("UUID = ");
        r.append(this._uuid);

        return r.toString();
    }


}
