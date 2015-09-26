package sidben.ateliercanvas.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;


public class PlayerEventHandler
{

    
    
    @SubscribeEvent
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {
    }
    
    
    
    @SubscribeEvent
    public void onEntityInteractEvent(EntityInteractEvent event)
    {
        boolean clientSide = event.target.worldObj.isRemote;
        
        
        if (!clientSide) {
            
            // Checks if the player right-clicked a painting
            if (event.target instanceof EntityPainting) {

                // Check if the play has an empty hand
                final ItemStack handItem = event.entityPlayer.inventory.getCurrentItem();
                if (handItem == null) {
                    
                    EntityPainting currentPainting = (EntityPainting) event.target;
                    boolean isSneaking = event.entityPlayer.isSneaking();
                    World world = event.target.worldObj;

                    
                    // Creates an array with all vanilla paintings
                    EntityPainting.EnumArt[] artArray = EntityPainting.EnumArt.values();
                    
                    // Finds out the index of the current art
                    int currentIndex = -1;
                    for (int i = 0; i < artArray.length; ++i)
                    {
                        if (artArray[i].equals(currentPainting.art)) {
                            currentIndex = i;
                            break;
                        }
                    }
                    
                    
                    if (currentIndex > -1) {

                        // Creates a new painting to replace the old one
                        EntityPainting newPainting = new EntityPainting(world, currentPainting.field_146063_b, currentPainting.field_146064_c, currentPainting.field_146062_d, currentPainting.hangingDirection);

                        // Finds out what is the next valid index. 
                        int newIndex = currentIndex;

                        /* 
                         * This loop intends to make a limited number of checks for a valid index. In case 
                         * something goes wrong, at least it wont create an infinite loop.
                         * 
                         * It's supposed to give a try for every painting, at least one should be valid.
                         */
                        for (int j = 0; j < artArray.length; j++) {

                            // RIGHT-CLICK move up, SHIFT + RIGHT-CLICK moves down
                            if (isSneaking) {
                                newIndex--;
                            } else {
                                newIndex++;
                            }
                            
                            // Loops the array index, if needed
                            if (newIndex >= artArray.length) newIndex = 0;
                            if (newIndex < 0) newIndex = artArray.length - 1;
                            

                            // Check if the surface is valid
                            newPainting.art = artArray[newIndex];
                            newPainting.setDirection(currentPainting.hangingDirection);
                            

                            if (newPainting.onValidSurface())
                            {
                                // Deletes the current painting entity
                                world.removeEntity(currentPainting);
                                
                                // Spawns a new painting at that place
                                world.spawnEntityInWorld(newPainting);
                                
                                break;
                            }

                        }
                        

                    }
                    

                    
                }
                
            }

        }
        
        
    }
    
    
    
    
    @SubscribeEvent
    public void onPlayerStartTracking(PlayerEvent.StartTracking event)
    {
    }
        
}
