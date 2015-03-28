package sidben.ateliercanvas.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;



public class ItemCustomPainting extends Item
{
    
    
    public ItemCustomPainting()
    {
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setTextureName("painting");
        this.setUnlocalizedName("custom_painting");
    }

    
    
    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (side == 0 || side == 1)
        {
            return false;
        }
        else
        {
            int i1 = Direction.facingToDirection[side];
            EntityHanging entityhanging = this.createHangingEntity(world, x, y, z, i1);

            if (!player.canPlayerEdit(x, y, z, side, itemstack))
            {
                return false;
            }
            else
            {
                if (entityhanging != null && entityhanging.onValidSurface())
                {
                    if (!world.isRemote)
                    {
                        world.spawnEntityInWorld(entityhanging);
                    }

                    --itemstack.stackSize;
                }

                return true;
            }
        }
        
    }
    
    
    
    private EntityHanging createHangingEntity(World world, int x, int y, int z, int direction)
    {
        return new EntityPainting(world, x, y, z, direction);
    }
    
    
}
