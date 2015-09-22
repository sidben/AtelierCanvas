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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;



public class EntityCustomPainting extends EntityHanging implements IEntityAdditionalSpawnData 
{
    
    private CustomPaintingConfigItem _entry;
        
    
    public EntityCustomPainting(World world) {
        super(world);
        
        LogHelper.info("EntityCustomPainting()");
        LogHelper.info("-   entry null: " + (_entry == null));
    }
    
    
    public EntityCustomPainting(World world, int x, int y, int z, int direction, String uuid) {
        super(world, x, y, z, direction);
        this.setPaintingEntry(uuid);
        this.setDirection(direction);
        
        LogHelper.info("EntityCustomPainting()");
        LogHelper.info(".   entry null: " + (_entry == null));
        LogHelper.info(".   uuid: " + uuid);
        LogHelper.info(".   pos: " + x + ", " + y + "," + z);
    }
    
    
    
    /**
     * Loads the config info for a given UUID, or a generic placeholder
     * if the config info is invalid or not found.  
     * 
     * @param uuid
     */
    private void setPaintingEntry(String uuid) {
        LogHelper.info("setPaintingEntry('" + uuid + "')");
        
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

    
    
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbtTagCompound)
    {
        LogHelper.info("writeEntityToNBT()");
        LogHelper.info("    :" + this.getImageUUID());

        nbtTagCompound.setString("uuid", this.getImageUUID());
        super.writeEntityToNBT(nbtTagCompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbtTagCompound)
    {
        LogHelper.info("readEntityFromNBT()");

        String uniqueId = nbtTagCompound.getString("uuid");
        
        LogHelper.info("    :" + uniqueId);
        
        this.setPaintingEntry(uniqueId);
        super.readEntityFromNBT(nbtTagCompound);
    }
    

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        LogHelper.info("writeSpawnData()");
        LogHelper.info("    $" + this.getImageUUID());
        ByteBufUtils.writeUTF8String(buffer, this.getImageUUID());
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        LogHelper.info("readSpawnData()");
        String uniqueId = ByteBufUtils.readUTF8String(buffer);
        LogHelper.info("    $" + uniqueId);
        this.setPaintingEntry(uniqueId);
    }


}
