package sidben.ateliercanvas.proxy;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import sidben.ateliercanvas.ModAtelierCanvas;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.init.MyItems;
import sidben.ateliercanvas.network.MessagePaintingData;
import sidben.ateliercanvas.network.NetworkHelper;
import sidben.ateliercanvas.reference.Reference;
import sidben.ateliercanvas.world.storage.PaintingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;


/*
 * Base proxy class, here I initialize everything that must happen on both, server and client.
 * 
 */
public abstract class CommonProxy implements IProxy
{

    

    @Override
    public void pre_initialize()
    {
        // Register network messages
        ModAtelierCanvas.NetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ModChannel);
        ModAtelierCanvas.NetworkWrapper.registerMessage(NetworkHelper.PaintingDataHandler.class, MessagePaintingData.class, 0, Side.CLIENT);

        
        // Register blocks
        
        
        // Register items
        MyItems.register();
    }


    @Override
    public void initialize()
    {
        LogHelper.info("=================LOAD (common proxy)========================");
        
        
        // Recipes

        
        // Achievements

        
        // Event Handlers
        
        
        // Entities
        LogHelper.info("-- Registering custom painting entity");
        EntityRegistry.registerModEntity(EntityCustomPainting.class, "CustomPainting", 0, ModAtelierCanvas.instance, 160, Integer.MAX_VALUE, false);
    }


    @Override
    public void post_initialize()
    {
        
    }


    @Override
    public Object getServerGuiElement(int guiID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    
    @Override
    public Object getClientGuiElement(int guiID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }


}
