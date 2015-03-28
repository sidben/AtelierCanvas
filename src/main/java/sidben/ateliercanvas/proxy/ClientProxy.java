package sidben.ateliercanvas.proxy;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import sidben.ateliercanvas.client.renderer.RenderCustomPainting;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.init.MyItems;
import sidben.ateliercanvas.world.storage.PaintingData;




public class ClientProxy extends CommonProxy
{

    static final Minecraft mc = Minecraft.getMinecraft();
    private static PaintingData customData;
    private static DynamicTexture texture;
    public static ResourceLocation customPainting;
    private static int[] pixels;

    
    

    @Override
    public void pre_initialize()
    {
        LogHelper.info("=================PREINIT (client proxy)========================");
        
        super.pre_initialize();
        

        // Block and item textures
        MyItems.registerRender();

        // Special renderers
        
        
        
        
        

        

        
    }



    @Override
    public void initialize()
    {
        LogHelper.info("=================LOAD (client proxy)========================");
        
        
        super.initialize();
        
        
        // Entity renderer
        RenderingRegistry.registerEntityRenderingHandler(EntityCustomPainting.class, new RenderCustomPainting());

        
        
        
        
        
        
        
        
        
        TextureManager texMan = mc.renderEngine;

        customData = null;
        texture = new DynamicTexture(16, 16);
        pixels = texture.getTextureData();
        
        LogHelper.info("]   " + pixels.length);

        /*
        for (int i = 0; i < pixels.length; ++i)
        {
            //this.pixels[i] = 0;
            //this.pixels[i] = new Color(255, 128, 0).getRGB();
            pixels[i] = 16742144;
        }
        
        texture.updateDynamicTexture();
        */
        
        /*
        String auxPX = "";
        for (int i = 0; i < 16; ++i) {
            auxPX += pixels[i] + ", ";
        }
        LogHelper.info("]   " + auxPX);
        */
        
        //this.customPainting = texMan.getDynamicTextureLocation("custom_painting", texture);

        
        
        URL url;
        try {
            
            url = new URL("http://38.media.tumblr.com/avatar_dce0043e4cf6_16.png");
            texture = new DynamicTexture(ImageIO.read(url));
            customPainting = texMan.getDynamicTextureLocation("test", texture);
            
            
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pixels = texture.getTextureData();
        pixels[0] = new Color(255, 128, 0).getRGB();
        
        texture.updateDynamicTexture();
        
        
        pixels = texture.getTextureData();
        String auxPX = "";
        for (int i = 0; i < 16; ++i) {
            auxPX += pixels[i] + ", ";
        }
        LogHelper.info("]   " + auxPX);
        
        LogHelper.info("]   " + new Color(145, 170, 76).getRGB() + " / -7230900");
        

    }


    // returns an instance of the GUI
    @Override
    public Object getClientGuiElement(int guiID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }



/*
    private String getResourceName(String name)
    {
        return Reference.ModID + ":" + name;
    }
  */
    
}
