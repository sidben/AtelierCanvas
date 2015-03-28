package sidben.ateliercanvas.entity.item;

import sidben.ateliercanvas.init.MyItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;



public class EntityCustomPainting extends EntityHanging
{

    public EntityCustomPainting(World world) {
        super(world);
    }

    public EntityCustomPainting(World world, int x, int y, int z, int direction) {
        super(world, x, y, z, direction);
        this.setDirection(direction);
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
    public void onBroken(Entity p_110128_1_)
    {
        this.entityDropItem(new ItemStack(MyItems.customPainting), 0.0F);
    }

}
