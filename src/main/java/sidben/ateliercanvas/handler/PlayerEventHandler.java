package sidben.ateliercanvas.handler;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.network.NetworkHelper;
import sidben.ateliercanvas.world.storage.PaintingData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;


public class PlayerEventHandler
{

    private static final String debugName = "Sidben's Stick of Debuginess";
    
    
    
    @SuppressWarnings("rawtypes")
    @SubscribeEvent
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {
     
        if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR) {
            ItemStack itemInHand = event.entityPlayer.getHeldItem();
            
            if (itemInHand != null && itemInHand.getItem() == Items.stick && itemInHand.getDisplayName().equals(debugName)) {
                LogHelper.info("==Debuginess in Progress==");
                
                if (event.world.getBlock(event.x, event.y, event.z) == Blocks.glass){
                    
                    LogHelper.info("    Loading canvas 0 to 5...");
                    
                    WorldSavedData data;
                    for (int i = 0; i < 5; i++) {
                        String name = "canvas_" + i;
                        data = event.world.loadItemData(PaintingData.class, name);
                        LogHelper.info("        " + name + " : " + data);
                    }
                  
                    
                }
                else 
                {
                    
                    MapStorage mapStore = event.world.mapStorage;
    
                    Field fLoadedMaps, fLoadedLists, fIds;
                    Map<String, WorldSavedData> loadedDataMap = null;
                    List<WorldSavedData> loadedDataList = null;
                    Map<String, Short> idCounts = null;
                    
                    try 
                    {
                        fLoadedMaps = mapStore.getClass().getDeclaredField("loadedDataMap");
                        fLoadedLists = mapStore.getClass().getDeclaredField("loadedDataList");
                        fIds = mapStore.getClass().getDeclaredField("idCounts");
                        
                        fLoadedMaps.setAccessible(true);
                        fLoadedLists.setAccessible(true);
                        fIds.setAccessible(true);
                        
                        loadedDataMap = (Map)fLoadedMaps.get(mapStore);
                        loadedDataList = (List)fLoadedLists.get(mapStore);
                        idCounts = (Map)fIds.get(mapStore);
                    } 
                    catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) 
                    {
                        e.printStackTrace();
                    }
                    
                    
                    
                    
                    LogHelper.info("    MapStorage: " + mapStore);
    
                    LogHelper.info("    |- loadedDataMap (" + loadedDataMap.size() + ")");
                    for(Entry e : loadedDataMap.entrySet()) {
                        LogHelper.info("        " + e.getKey() + " / " + e.getValue());   
                    }
    
                    LogHelper.info("    |- loadedDataList (" + loadedDataList.size() + ")");
                    for(WorldSavedData w : loadedDataList) {
                        LogHelper.info("        " + w.mapName + " / " + w.toString());   
                    }
    
                    LogHelper.info("    |- idCounts (" + idCounts.size() + ")");
                    for(Entry e : idCounts.entrySet()) {
                        LogHelper.info("        " + e.getKey() + " / " + e.getValue());    
                    }
                    
                    
                }
                
                
                LogHelper.info(" ");
            }
            
        }
        
    }
    
    
    
    
    
    
    @SubscribeEvent
    public void onPlayerStartTracking(PlayerEvent.StartTracking event)
    {
        
        // TODO: Update so only sends once per player / Only sends if the player don't have the painting
        
        /*
         * Check if the player started tracking a custom painting entity
         * and send the picture image.
         */
        
        /*
        if (event.target instanceof EntityCustomPainting && !event.entity.worldObj.isRemote) {
            LogHelper.info("--Player start tracking custom painting--");
            
            final EntityCustomPainting painting = (EntityCustomPainting)event.target;
            NetworkHelper.sendCustomPaintingInfoMessage(painting.getImageUUID(), painting.worldObj, event.entityPlayer); 
        }
        */

    }
        
}
