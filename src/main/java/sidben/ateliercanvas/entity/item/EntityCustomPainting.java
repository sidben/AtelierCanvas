package sidben.ateliercanvas.entity.item;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.init.MyItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;



public class EntityCustomPainting extends EntityHanging implements IEntityAdditionalSpawnData 
{

    public EntityCustomPainting(World world) {
        super(world);
        
        LogHelper.info("EntityCustomPainting()");
        LogHelper.info(".   world: " + world);
    }

    public EntityCustomPainting(World world, int x, int y, int z, int direction) {
        super(world, x, y, z, direction);
        this.setDirection(direction);
        
        LogHelper.info("EntityCustomPainting()");
        LogHelper.info(".   pos: " + x + ", " + y + "," + z);
        LogHelper.info(".   direaction: " + direction);
        LogHelper.info(".   pos (this): " + this.posX + ", " + this.posY + "," + this.posZ);
        LogHelper.info(".   direaction (this): " + this.hangingDirection);
    }
    

    @Override
    public int getWidthPixels()
    {
        return 16;
    }

    @Override
    public int getHeightPixels()
    {
        return 16;
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
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        this.field_146063_b = buffer.readInt();
        this.field_146064_c = buffer.readInt();
        this.field_146062_d = buffer.readInt();
        this.setDirection(buffer.readByte());

        LogHelper.info("readSpawnData()");
        LogHelper.info("_    " + this.hangingDirection);
    }

}
