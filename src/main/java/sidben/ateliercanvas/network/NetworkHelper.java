package sidben.ateliercanvas.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import sidben.ateliercanvas.ModAtelierCanvas;
import sidben.ateliercanvas.helper.AtelierHelper;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.world.storage.PaintingData;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;



/**
 * Responsible for sending and receiving packets / messages between client and server. 
 *
 */
public class NetworkHelper
{
    
    //---------------------------------------------------------------------
    //      Message Dispatch
    //---------------------------------------------------------------------

    /**
     * Sends painting data to the client, so it can cache it and render the painting correctly.
     * 
     * Server -> Client
     * 
     */
    public static void sendPaintingDataMessage(int canvasId, World world, EntityPlayer target) {
        PaintingData data = AtelierHelper.getPaintingData(world, canvasId);
        MessagePaintingData message = new MessagePaintingData(canvasId, data);
        
        // Debug
        LogHelper.info("~~Sending Message - Painting Data~~");
        LogHelper.info("    target: " + target);
        LogHelper.info("    " + message.toString());

        // Sends a message to the player
        ModAtelierCanvas.NetworkWrapper.sendTo(message, (EntityPlayerMP)target);
    }
    

    
    
    
    
    //---------------------------------------------------------------------
    //      Message Receival
    //---------------------------------------------------------------------

    public static class PaintingDataHandler implements IMessageHandler<MessagePaintingData, IMessage> {

        @Override
        public IMessage onMessage(MessagePaintingData message, MessageContext ctx) {

            // Debug
            LogHelper.info("~~Receiving Message - Painting Data~~");
            LogHelper.info("    " + message.toString());
            
            AtelierHelper.addPaintingData(message);

            return null;
        }
        
    }
    
}
