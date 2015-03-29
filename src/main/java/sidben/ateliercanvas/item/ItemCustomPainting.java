package sidben.ateliercanvas.item;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

// TODO - add CanvasID (damage value) and render it
// TODO: Flag if the painting is original or copy

public class ItemCustomPainting extends Item
{
    
    
    public ItemCustomPainting()
    {
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setTextureName("painting");
        this.setUnlocalizedName("custom_painting");  // TODO: fix localization
        this.setHasSubtypes(true);
    }

    
    
    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        int auxId = itemstack.getItemDamage();

        LogHelper.info("onItemUse()");
        LogHelper.info("    side: " + side);
        LogHelper.info("    pos: " + x + ", " + y + ", " + z);
        LogHelper.info("    id: " + auxId);
        
        
        if (side == 0 || side == 1) // side 0 == bottom of the block, side 1 == top of the block
        {
            return false;
        }
        else
        {
            int i1 = Direction.facingToDirection[side];
            EntityHanging entityhanging = this.createHangingEntity(world, x, y, z, i1, auxId);
            
            
            LogHelper.info("    direaction: " + i1);
            LogHelper.info("    entity: " + entityhanging);
            LogHelper.info("    can edit: " + player.canPlayerEdit(x, y, z, side, itemstack));
            
            if (entityhanging != null) {
                LogHelper.info("    valid surface: " + entityhanging.onValidSurface());
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
    
    
    
    private EntityHanging createHangingEntity(World world, int x, int y, int z, int direction, int canvasId)
    {
        return new EntityCustomPainting(world, x, y, z, direction, canvasId);
    }
    
    
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List infolist, boolean debugmode)
    {
        /*
        if (mapdata == null)
        {
            p_77624_3_.add("Unknown painting");
        }
        else
        {
            p_77624_3_.add("Scaling at 1:" + (1 << mapdata.scale));
            p_77624_3_.add("(Level " + mapdata.scale + "/" + 4 + ")");
        }
        */
    }

    
}
