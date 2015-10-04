package sidben.ateliercanvas.entity.item;

import static sidben.ateliercanvas.handler.ConfigurationHandler.EMPTY_UUID;
import io.netty.buffer.ByteBuf;
import java.util.Date;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.helper.EnumAuthenticity;
import sidben.ateliercanvas.init.MyItems;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;



public class EntityCustomPainting extends EntityHanging implements IEntityAdditionalSpawnData
{

    // ------------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------------
    public static final String       NBTPaintingUUID    = "uuid";
    public static final String       NBTItemDamageValue = "damage";



    private CustomPaintingConfigItem _entry;
    private int                      _itemDamageValue;


    public EntityCustomPainting(World world) {
        super(world);
    }


    public EntityCustomPainting(World world, int x, int y, int z, int direction, String uuid, int damageValue) {
        super(world, x, y, z, direction);
        this.setPaintingEntry(uuid);
        this.setDirection(direction);
        this._itemDamageValue = damageValue;
    }



    /**
     * Loads the config info for a given UUID, or a generic placeholder
     * if the config info is invalid or not found.
     * 
     * @param uuid
     */
    private void setPaintingEntry(String uuid)
    {
        final CustomPaintingConfigItem paintingConfig = ConfigurationHandler.findPaintingByUUID(uuid);
        if (paintingConfig != null) {
            this._entry = paintingConfig;
        } else {
            this._entry = new CustomPaintingConfigItem("", null, false, 0, "", "", new Date(), new Date(), 16, 16);
        }
    }



    // ------------------------------------------------------------------------------
    // Info
    // ------------------------------------------------------------------------------

    public UUID getImageUUID()
    {
        if (this._entry == null) {
            return EMPTY_UUID;
        }
        return this._entry.getUUID();
    }

    public String getTitle()
    {
        if (this._entry == null) {
            return "";
        }
        return this._entry.getPaintingTitleRaw();
    }

    public String getAuthor()
    {
        if (this._entry == null) {
            return "";
        }
        return this._entry.getPaintingAuthor();
    }

    public boolean getIsAuthentic()
    {
        return this._itemDamageValue == EnumAuthenticity.ORIGINAL.getId();
    }



    @Override
    public int getWidthPixels()
    {
        if (this._entry == null) {
            return 16;
        }
        return this._entry.getTileWidth() * ConfigurationHandler.defaultResolution;
    }

    @Override
    public int getHeightPixels()
    {
        if (this._entry == null) {
            return 16;
        }
        return this._entry.getTileHeight() * ConfigurationHandler.defaultResolution;
    }



    // ------------------------------------------------------------------------------
    // Behavior
    // ------------------------------------------------------------------------------

    @Override
    public void onBroken(Entity entity)
    {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;
            if (player.capabilities.isCreativeMode) {
                return;
            }
        }

        final ItemStack its_a_me_Painting = this.getMyItem();
        this.entityDropItem(its_a_me_Painting, 0.0F);
    }


    /**
     * Called when a user uses the creative pick block button on this entity.
     * 
     * @param target
     *            The full target the player is looking at
     * @return A ItemStack to add to the player's inventory, Null if nothing should be added.
     */
    @Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        final ItemStack its_a_me_Painting = this.getMyItem();
        return its_a_me_Painting;
    }


    /**
     * Returns the ItemStack of this painting.
     */
    private ItemStack getMyItem()
    {
        if (this._entry == null) {
            return null;
        }


        // Gets an item stack with the current painting data
        final ItemStack its_a_me_Painting = new ItemStack(MyItems.customPainting);
        MyItems.customPainting.addPaintingData(its_a_me_Painting, this._entry, false);

        // Adds the authenticity
        its_a_me_Painting.setItemDamage(this._itemDamageValue);

        return its_a_me_Painting;
    }



    /*
     * Complete copy-paste of the setDirection method so I can override the private
     * [func_70517_b] method and add support for paintings larger than 64 pixels.
     * 
     * @see net.minecraft.entity.EntityHanging#setDirection(int)
     */
    @Override
    public void setDirection(int direction)
    {
        this.hangingDirection = direction;
        this.prevRotationYaw = this.rotationYaw = direction * 90;
        float f = this.getWidthPixels();
        float f1 = this.getHeightPixels();
        float f2 = this.getWidthPixels();

        if (direction != 2 && direction != 0) {
            f = 0.5F;
        } else {
            f2 = 0.5F;
            this.rotationYaw = this.prevRotationYaw = Direction.rotateOpposite[direction] * 90;
        }

        f /= 32.0F;
        f1 /= 32.0F;
        f2 /= 32.0F;
        float f3 = this.field_146063_b + 0.5F;
        float f4 = this.field_146064_c + 0.5F;
        float f5 = this.field_146062_d + 0.5F;
        final float f6 = 0.5625F;       // OBS: 0.5625 == 9/16 

        if (direction == 2) {
            f5 -= f6;
        }

        if (direction == 1) {
            f3 -= f6;
        }

        if (direction == 0) {
            f5 += f6;
        }

        if (direction == 3) {
            f3 += f6;
        }

        if (direction == 2) {
            f3 -= this.func_70517_b(this.getWidthPixels());
        }

        if (direction == 1) {
            f5 += this.func_70517_b(this.getWidthPixels());
        }

        if (direction == 0) {
            f3 += this.func_70517_b(this.getWidthPixels());
        }

        if (direction == 3) {
            f5 -= this.func_70517_b(this.getWidthPixels());
        }

        f4 += this.func_70517_b(this.getHeightPixels());
        this.setPosition(f3, f4, f5);
        final float f7 = -0.03125F;     // OBS: -0.03125 == 1/32 
        
        double minX = f3 - f - f7;
        double minY = f4 - f1 - f7;
        double minZ = f5 - f2 - f7;
        double maxX = f3 + f + f7;
        double maxY = f4 + f1 + f7;
        double maxZ = f5 + f2 + f7;
        
        this.boundingBox.setBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }


    /**
     * Adjusts the position of the painting.
     */
    private float func_70517_b(int value)
    {
        return value % 32 == 0 ? 0.5F : 0F;
    }



    // ------------------------------------------------------------------------------
    // Data Persistence
    // ------------------------------------------------------------------------------

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setString(EntityCustomPainting.NBTPaintingUUID, this.getImageUUID().toString());
        nbtTagCompound.setInteger(EntityCustomPainting.NBTItemDamageValue, this._itemDamageValue);
        super.writeEntityToNBT(nbtTagCompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbtTagCompound)
    {
        final String uniqueId = nbtTagCompound.getString(EntityCustomPainting.NBTPaintingUUID);
        this._itemDamageValue = nbtTagCompound.getInteger(EntityCustomPainting.NBTItemDamageValue);

        this.setPaintingEntry(uniqueId);
        super.readEntityFromNBT(nbtTagCompound);
    }


    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        /*
         * Sends the painting info to the client. I need to send the direction and
         * XYZ coordinates because the client won't have the correct data when the
         * entity spawns.
         * 
         * This behaves like the vanilla [handleSpawnPainting], that handles packets
         * client-side when an EntityPainting spawns.
         */
        ByteBufUtils.writeUTF8String(buffer, this.getImageUUID().toString());
        buffer.writeByte(hangingDirection);
        buffer.writeInt(this.field_146063_b);       // x
        buffer.writeInt(this.field_146064_c);       // y
        buffer.writeInt(this.field_146062_d);       // z
        buffer.writeInt(this._itemDamageValue);
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        /*
         * Reads the entity info from the server and updates the client.
         * 
         * First I need to read the UUID of the painting, so the object can be load by
         * the [setPaintingEntry] method.
         * 
         * Then I need to set the correct XYZ coordinates and call the [setDirection]
         * method in order to create the correct bounding box.
         */
        final String uniqueId = ByteBufUtils.readUTF8String(buffer);
        final int direction = buffer.readByte();
        this.field_146063_b = buffer.readInt();     // x
        this.field_146064_c = buffer.readInt();     // y
        this.field_146062_d = buffer.readInt();     // z
        this._itemDamageValue = buffer.readInt();

        this.setPaintingEntry(uniqueId);
        this.setDirection(direction);
    }


}
