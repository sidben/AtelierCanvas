package sidben.ateliercanvas.entity.item;

import java.util.Date;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.init.MyItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;



public class EntityCustomPainting extends EntityHanging implements IEntityAdditionalSpawnData 
{
    
    private CustomPaintingConfigItem _entry;
        
    
    public EntityCustomPainting(World world) {
        super(world);
        
        LogHelper.info("EntityCustomPainting()");
        LogHelper.info(".   world: " + world);
    }
    
    
    public EntityCustomPainting(World world, int x, int y, int z, int direction, String uuid) {
        super(world, x, y, z, direction);
        this.setDirection(direction);
        this.setPaintingEntry(uuid);
        
        LogHelper.info("EntityCustomPainting()");
        LogHelper.info(".   uuid: " + uuid);
        LogHelper.info(".   pos: " + x + ", " + y + "," + z);
        LogHelper.info(".   direaction: " + direction);
        LogHelper.info(".   pos (this): " + this.posX + ", " + this.posY + "," + this.posZ);
        LogHelper.info(".   direaction (this): " + this.hangingDirection);
    }
    
    
    
    /**
     * Loads the config info for a given UUID, or a generic placeholder
     * if the config info is invalid or not found.  
     * 
     * @param uuid
     */
    private void setPaintingEntry(String uuid) {
        System.out.println("setPaintingEntry('" + uuid + "')");
        
        final CustomPaintingConfigItem paintingConfig = ConfigurationHandler.findPaintingByUUID(uuid);
        if (paintingConfig != null) {
            this._entry = paintingConfig;
        } else {
            this._entry = new CustomPaintingConfigItem("", uuid, false, 0, "", "",  new Date(), new Date());
        }

        LogHelper.info(".   entry: " + this._entry.toString());
    }
    
    
    

    public String getImageUUID() {
        if (this._entry == null) return "";
        return this._entry.getUUID();
    }
    
    @Override
    public int getWidthPixels()
    {
        if (this._entry == null) return 16;
        return this._entry.getTileWidth() * ConfigurationHandler.defaultResolution;
    }

    @Override
    public int getHeightPixels()
    {
        if (this._entry == null) return 16;
        return this._entry.getTileHeight() * ConfigurationHandler.defaultResolution;
    }

    @Override
    public void onBroken(Entity entity)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;
            if (player.capabilities.isCreativeMode) return;
        }

        this.entityDropItem(new ItemStack(MyItems.customPainting), 0.0F);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        LogHelper.info("writeSpawnData()");
        LogHelper.info("_    " + this.hangingDirection);
        
        buffer.writeInt(this.field_146063_b);       // x
        buffer.writeInt(this.field_146064_c);       // y
        buffer.writeInt(this.field_146062_d);       // z
        buffer.writeByte(this.hangingDirection);

        ByteBufUtils.writeUTF8String(buffer, this.getImageUUID());
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        this.field_146063_b = buffer.readInt();     // x
        this.field_146064_c = buffer.readInt();     // y
        this.field_146062_d = buffer.readInt();     // z  TODO: is this needed?
        this.setDirection(buffer.readByte());

        String uniqueId = ByteBufUtils.readUTF8String(buffer);
        this.setPaintingEntry(uniqueId);

        LogHelper.info("readSpawnData()");
        LogHelper.info("_    " + this.hangingDirection);
    }

}
