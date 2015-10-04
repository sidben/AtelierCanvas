package sidben.ateliercanvas.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import sidben.ateliercanvas.ModAtelierCanvas;
import sidben.ateliercanvas.client.renderer.RenderCustomPainting;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.helper.AtelierHelper;
import sidben.ateliercanvas.init.MyItems;
import cpw.mods.fml.client.registry.RenderingRegistry;



public class ClientProxy extends CommonProxy
{



    @Override
    public void pre_initialize()
    {
        super.pre_initialize();

        // Block and item textures
        MyItems.registerRender();
    }



    @Override
    public void initialize()
    {
        super.initialize();

        // Helper classes single instances
        ModAtelierCanvas.instance.setAtelierHelper(new AtelierHelper(Minecraft.getMinecraft()));

        // Entity renderer
        RenderingRegistry.registerEntityRenderingHandler(EntityCustomPainting.class, new RenderCustomPainting());
    }


    // returns an instance of the GUI
    @Override
    public Object getClientGuiElement(int guiID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }



}