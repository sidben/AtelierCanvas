package sidben.ateliercanvas.client.renderer;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.proxy.ClientProxy;
import sidben.ateliercanvas.world.storage.PaintingData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/*
 * Reference - CameraCraft3
 * github.com/diesieben07/CameraCraft3/blob/master/src/main/java/de/take_weiland/mods/cameracraft/client/render/RenderPoster.java
 * 
 * and also vanilla RenderPainting.java
 * 
 */

@SideOnly(Side.CLIENT)
public class RenderCustomPainting extends Render
{
    
    private static final ResourceLocation vanillaPainting = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
    

    

    
    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityCustomPainting)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityCustomPainting)entity);
    }

    
    
    protected ResourceLocation getEntityTexture(EntityCustomPainting entity)
    {
        return ClientProxy.customPainting;
        
        // return this.vabillaPainting;
    }
    
    
    
    public void doRender(EntityCustomPainting painting, double x, double y, double z, float posYaw, float partialTickTime)
    {
        /*
        LogHelper.info("doRender()");
        LogHelper.info("    par2-4: " + x + ", " + y + ", " + z);
        LogHelper.info("    par5-6: " + posYaw + ", " + partialTickTime);
        LogHelper.info("    painting pos: " + painting.posX + ", " + painting.posY + ", " + painting.posZ);
        LogHelper.info("    painting direction: " + painting.hangingDirection);
        LogHelper.info("    painting field: " + painting.field_146063_b + ", " + painting.field_146064_c + ", " + painting.field_146062_d);
        LogHelper.info("    offset: " + Direction.offsetX[painting.hangingDirection] + ", " + Direction.offsetZ[painting.hangingDirection]);
        */

        // NOTE: on the client, painting entity has no info. Need to add packet support, datawatcher or something else
        
        
        double renderPosX = x;
        double renderPosY = y - 0.5D;
        double renderPosZ = z;
        
        // TODO: pretty this up
        if (posYaw == 0) 
        {
            renderPosX -= 0.5D;
        }
        else if (posYaw == -180) 
        {
            renderPosX += 0.5D;
        }
        else if (posYaw == -90) 
        {
            renderPosZ -= 0.5D;
        }
        else if (posYaw == 90) 
        {
            renderPosZ += 0.5D;
        }
        
        
        GL11.glPushMatrix();
        GL11.glTranslated(renderPosX, renderPosY, renderPosZ);
        GL11.glRotatef(posYaw, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        float f2 = 0.0625F;
        GL11.glScalef(f2, f2, f2);
        
        this.drawPainting(painting);
        
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    
    private void drawPainting(EntityCustomPainting painting) {
        int sizeX = 0;
        int sizeY = 0;
        
        float f = (float)(-sizeX) / 2.0F;
        float f1 = (float)(-sizeY) / 2.0F;
        double zPos = 0.5;
        float f3 = 0.75F;
        float f4 = 0.8125F;
        float f5 = 0.0F;
        float f6 = 0.0625F;
        float f7 = 0.75F;
        float f8 = 0.8125F;
        float f9 = 0.001953125F;
        float f10 = 0.001953125F;
        float f11 = 0.7519531F;
        float f12 = 0.7519531F;
        float f13 = 0.0F;
        float f14 = 0.0625F;
                

        LogHelper.info("    f, f1: " + f + ", " + f1);

        
        // TODO: add bigger painting support, will require a loop
        int tileX = 0;
        int tileY = 0;
        
        float xEnd = f + (float)((tileX + 1) * 16);
        float xStart = f + (float)(tileX * 16);
        float yEnd = f1 + (float)((tileY + 1) * 16);
        float yStart = f1 + (float)(tileY * 16);
        
        // setupLightmap(poster, (xEnd + xStart) / 2.0F, (yEnd + yStart) / 2.0F);
        // TODO: sync entity. Can't use the lightmap since the client entity has no info
        
        float uStart = 1;
        float uEnd = 0;
        float vStart = 1;
        float vEnd = 0;
        
        
        Tessellator tessellator = Tessellator.instance;


        bindTexture(vanillaPainting);
        tessellator.startDrawingQuads();
        
        // Borders
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV((double)xEnd, (double)yEnd, -zPos, (double)f7, (double)f9);
        tessellator.addVertexWithUV((double)xStart, (double)yEnd, -zPos, (double)f8, (double)f9);
        tessellator.addVertexWithUV((double)xStart, (double)yEnd, zPos, (double)f8, (double)f10);
        tessellator.addVertexWithUV((double)xEnd, (double)yEnd, zPos, (double)f7, (double)f10);
        
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        tessellator.addVertexWithUV((double)xEnd, (double)yStart, zPos, (double)f7, (double)f9);
        tessellator.addVertexWithUV((double)xStart, (double)yStart, zPos, (double)f8, (double)f9);
        tessellator.addVertexWithUV((double)xStart, (double)yStart, -zPos, (double)f8, (double)f10);
        tessellator.addVertexWithUV((double)xEnd, (double)yStart, -zPos, (double)f7, (double)f10);
        
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        tessellator.addVertexWithUV((double)xEnd, (double)yEnd, zPos, (double)f12, (double)f13);
        tessellator.addVertexWithUV((double)xEnd, (double)yStart, zPos, (double)f12, (double)f14);
        tessellator.addVertexWithUV((double)xEnd, (double)yStart, -zPos, (double)f11, (double)f14);
        tessellator.addVertexWithUV((double)xEnd, (double)yEnd, -zPos, (double)f11, (double)f13);
        
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        tessellator.addVertexWithUV((double)xStart, (double)yEnd, -zPos, (double)f12, (double)f13);
        tessellator.addVertexWithUV((double)xStart, (double)yStart, -zPos, (double)f12, (double)f14);
        tessellator.addVertexWithUV((double)xStart, (double)yStart, zPos, (double)f11, (double)f14);
        tessellator.addVertexWithUV((double)xStart, (double)yEnd, zPos, (double)f11, (double)f13);
        
        // back
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV((double)xEnd, (double)yEnd, zPos, (double)f3, (double)f5);
        tessellator.addVertexWithUV((double)xStart, (double)yEnd, zPos, (double)f4, (double)f5);
        tessellator.addVertexWithUV((double)xStart, (double)yStart, zPos, (double)f4, (double)f6);
        tessellator.addVertexWithUV((double)xEnd, (double)yStart, zPos, (double)f3, (double)f6);
        
        tessellator.draw();
        
        
        // Picture
        this.bindEntityTexture(painting);
        tessellator.startDrawingQuads();
        
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addVertexWithUV(xEnd, yStart, -zPos, uEnd, vStart);
        tessellator.addVertexWithUV(xStart, yStart, -zPos, uStart, vStart);
        tessellator.addVertexWithUV(xStart, yEnd, -zPos, uStart, vEnd);
        tessellator.addVertexWithUV(xEnd, yEnd, -zPos, uEnd, vEnd);
        
        tessellator.draw();

    }

    
    private void setupLightmap(EntityCustomPainting entity, float par2, float par3)
    {
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posY + (double)(par3 / 16.0F));
        int k = MathHelper.floor_double(entity.posZ);

        if (entity.hangingDirection == 2)
        {
            i = MathHelper.floor_double(entity.posX + (double)(par2 / 16.0F));
        }

        if (entity.hangingDirection == 1)
        {
            k = MathHelper.floor_double(entity.posZ - (double)(par2 / 16.0F));
        }

        if (entity.hangingDirection == 0)
        {
            i = MathHelper.floor_double(entity.posX - (double)(par2 / 16.0F));
        }

        if (entity.hangingDirection == 3)
        {
            k = MathHelper.floor_double(entity.posZ + (double)(par2 / 16.0F));
        }

        int l = this.renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int i1 = l % 65536;
        int j1 = l / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i1, (float)j1);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }

}
