package sidben.ateliercanvas.item;

import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.helper.LogHelper;
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
        this.setUnlocalizedName("custom_painting");  // TODO: fix localization
    }

    
    
    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        LogHelper.info("onItemUse()");
        LogHelper.info("    side: " + side);
        LogHelper.info("    pos: " + x + ", " + y + ", " + z);
        
        
        if (side == 0 || side == 1) // side 0 == bottom of the block, side 1 == top of the block
        {
            return false;
        }
        else
        {
            int i1 = Direction.facingToDirection[side];
            EntityHanging entityhanging = this.createHangingEntity(world, x, y, z, i1);
            
            EntityPainting ref = new EntityPainting(world, x, y, z, i1);
            
            
            LogHelper.info("    direaction: " + i1);
            LogHelper.info("    entity: " + entityhanging);
            LogHelper.info("    entity ref: " + ref);
            LogHelper.info("    can edit: " + player.canPlayerEdit(x, y, z, side, itemstack));
            
            if (entityhanging != null) {
                LogHelper.info("    valid surface: " + entityhanging.onValidSurface());
            }
            if (ref != null) {
                LogHelper.info("    valid surface (ref): " + ref.onValidSurface());
            }
            

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
                        LogHelper.info("--Spawning custom painting at " + x + ", " + y + ", " + z);
                        
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
        return new EntityCustomPainting(world, x, y, z, direction);
    }
    
    
}
