package sidben.ateliercanvas.client.renderer;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sidben.ateliercanvas.ModAtelierCanvas;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.helper.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/*
 * Reference - CameraCraft3
 * github.com/diesieben07/CameraCraft3/blob/master/src/main/java/de/take_weiland/mods/cameracraft/client/render/RenderPoster.java
 * 
 * and also vanilla RenderPainting.java
 */

/**
 * Renders a custom painting entity.
 * 
 * @see net.minecraft.client.renderer.entity.RenderPainting
 * @author sidben
 */
@SideOnly(Side.CLIENT)
public class RenderCustomPainting extends Render
{

    private static final ResourceLocation vanillaPainting = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");

    @Override
    public void doRender(Entity entity, double x, double y, double z, float posYaw, float partialTickTime)
    {
        this.doRender((EntityCustomPainting) entity, x, y, z, posYaw, partialTickTime);
    }


    public void doRender(EntityCustomPainting painting, double x, double y, double z, float posYaw, float partialTickTime)
    {
        final float scale = 0.0625F;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(posYaw, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        this.bindEntityTexture(painting);
        GL11.glScalef(scale, scale, scale);

        this.drawPainting(painting, painting.getWidthPixels(), painting.getHeightPixels(), 0, 0);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }



    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityCustomPainting) entity);
    }



    protected ResourceLocation getEntityTexture(EntityCustomPainting entity)
    {
        return ModAtelierCanvas.instance.getAtelierHelper().getCustomPaintingImage(entity.getImageUUID());
    }



    private void drawPainting(EntityCustomPainting painting, int width, int height, int offsetX, int offsetY)
    {
        final float f = (float)(-width) / 2.0F;
        final float f1 = (float)(-height) / 2.0F;
        final double zPos = 0.5;
        final float f3 = 0.75F;
        final float f4 = 0.8125F;
        final float f5 = 0.0F;
        final float f6 = 0.0625F;
        final float f7 = 0.75F;
        final float f8 = 0.8125F;
        final float f9 = 0.001953125F;
        final float f10 = 0.001953125F;
        final float f11 = 0.7519531F;
        final float f12 = 0.7519531F;
        final float f13 = 0.0F;
        final float f14 = 0.0625F;


        float xEnd, xStart, yEnd, yStart;
        float uStart, uEnd, vStart, vEnd;
        final Tessellator tessellator = Tessellator.instance;


        
        bindTexture(vanillaPainting);

        for (int i = 0; i < width / 16; ++i) {

            for (int j = 0; j < height / 16; ++j) {

                xEnd = f + (float)((i + 1) * 16);
                xStart = f + (float)(i * 16);
                yEnd = f1 + (float)((j + 1) * 16);
                yStart = f1 + (float)(j * 16);

                this.setupLightmap(painting, (xEnd + xStart) / 2.0F, (yEnd + yStart) / 2.0F);


                uStart = (offsetX + width - i * 16) / 256.0F;
                uEnd = (offsetX + width - (i + 1) * 16) / 256.0F;
                vStart = (offsetY + height - j * 16) / 256.0F;
                vEnd = (offsetY + height - (j + 1) * 16) / 256.0F;


                // DEBUG
                /*
                LogHelper.info("{" + i + "," + j +"}: "+ width + "x" + height + " | " + xEnd + " | " + xStart + " | " + yEnd + " | " + yStart);
                LogHelper.info("{" + i + "," + j +"}: "+ uStart + " | " + uEnd + " | " + vStart + " | " + vEnd);
                LogHelper.info("");
                */


                tessellator.startDrawingQuads();

                
                // Back
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                tessellator.addVertexWithUV((double)xEnd, (double)yEnd, (double)zPos, (double)f3, (double)f5);
                tessellator.addVertexWithUV((double)xStart, (double)yEnd, (double)zPos, (double)f4, (double)f5);
                tessellator.addVertexWithUV((double)xStart, (double)yStart, (double)zPos, (double)f4, (double)f6);
                tessellator.addVertexWithUV((double)xEnd, (double)yStart, (double)zPos, (double)f3, (double)f6);

                // Borders
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                tessellator.addVertexWithUV((double)xEnd, (double)yEnd, (double)(-zPos), (double)f7, (double)f9);
                tessellator.addVertexWithUV((double)xStart, (double)yEnd, (double)(-zPos), (double)f8, (double)f9);
                tessellator.addVertexWithUV((double)xStart, (double)yEnd, (double)zPos, (double)f8, (double)f10);
                tessellator.addVertexWithUV((double)xEnd, (double)yEnd, (double)zPos, (double)f7, (double)f10);

                tessellator.setNormal(0.0F, -1.0F, 0.0F);
                tessellator.addVertexWithUV((double)xEnd, (double)yStart, (double)zPos, (double)f7, (double)f9);
                tessellator.addVertexWithUV((double)xStart, (double)yStart, (double)zPos, (double)f8, (double)f9);
                tessellator.addVertexWithUV((double)xStart, (double)yStart, (double)(-zPos), (double)f8, (double)f10);
                tessellator.addVertexWithUV((double)xEnd, (double)yStart, (double)(-zPos), (double)f7, (double)f10);

                tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                tessellator.addVertexWithUV((double)xEnd, (double)yEnd, (double)zPos, (double)f12, (double)f13);
                tessellator.addVertexWithUV((double)xEnd, (double)yStart, (double)zPos, (double)f12, (double)f14);
                tessellator.addVertexWithUV((double)xEnd, (double)yStart, (double)(-zPos), (double)f11, (double)f14);
                tessellator.addVertexWithUV((double)xEnd, (double)yEnd, (double)(-zPos), (double)f11, (double)f13);

                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                tessellator.addVertexWithUV((double)xStart, (double)yEnd, (double)(-zPos), (double)f12, (double)f13);
                tessellator.addVertexWithUV((double)xStart, (double)yStart, (double)(-zPos), (double)f12, (double)f14);
                tessellator.addVertexWithUV((double)xStart, (double)yStart, (double)zPos, (double)f11, (double)f14);
                tessellator.addVertexWithUV((double)xStart, (double)yEnd, (double)zPos, (double)f11, (double)f13);

                
                tessellator.draw();

            }
        }

        
        xStart = f;
        xEnd = f + width;
        yStart = f1;
        yEnd = f1 + height;

        uStart = 1F;
        uEnd = 0F;
        vStart = 1F;
        vEnd = 0F;


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



    private void setupLightmap(EntityCustomPainting painting, float p_77008_2_, float p_77008_3_)
    {
        int i = MathHelper.floor_double(painting.posX);
        final int j = MathHelper.floor_double(painting.posY + p_77008_3_ / 16.0F);
        int k = MathHelper.floor_double(painting.posZ);

        if (painting.hangingDirection == 2) {
            i = MathHelper.floor_double(painting.posX + p_77008_2_ / 16.0F);
        }

        if (painting.hangingDirection == 1) {
            k = MathHelper.floor_double(painting.posZ - p_77008_2_ / 16.0F);
        }

        if (painting.hangingDirection == 0) {
            i = MathHelper.floor_double(painting.posX - p_77008_2_ / 16.0F);
        }

        if (painting.hangingDirection == 3) {
            k = MathHelper.floor_double(painting.posZ + p_77008_2_ / 16.0F);
        }

        final int l = this.renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
        final int i1 = l % 65536;
        final int j1 = l / 65536;

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1, j1);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }

}
