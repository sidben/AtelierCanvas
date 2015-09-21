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
        LogHelper.info(width + " | " + height + " | " + offsetX + " | " + offsetY);


        final float f = (width) / 2.0F;
        final float f1 = (height) / 2.0F;
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


        /*
         * for (int i = 0; i < width / 16; ++i)
         * {
         * for (int j = 0; j < height / 16; ++j)
         * {
         */

        final int i = 0;
        final int j = 0;

        float xEnd = f + (i + 1) * 16;
        float xStart = f + i * 16;
        float yEnd = f1 + (j + 1) * 16;
        float yStart = f1 + j * 16;

        // xEnd = 80;
        final float xMargin = 8F;
        final float yMargin = 8F;

        xStart = 0 + xMargin;
        xEnd = -width + xMargin;
        yStart = 0 + yMargin;
        yEnd = -height + yMargin;

        LogHelper.info(xEnd + " | " + xStart + " | " + yEnd + " | " + yStart);


        this.setupLightmap(painting, (xEnd + xStart) / 2.0F, (yEnd + yStart) / 2.0F);

        /*
         * final float uStart = (float)(offsetX + width - i * 16) / 256.0F;;
         * final float uEnd = (float)(offsetX + width - (i + 1) * 16) / 256.0F;
         * final float vStart = (float)(offsetY + height - j * 16) / 256.0F;
         * final float vEnd = (float)(offsetY + height - (j + 1) * 16) / 256.0F;
         */

        final float uStart = 0F;
        final float uEnd = 1F;
        final float vStart = 0F;
        final float vEnd = 1F;


        // LogHelper.info(uStart + " | " + uEnd + " | " + vStart + " | " + vEnd);


        final Tessellator tessellator = Tessellator.instance;

        bindTexture(vanillaPainting);
        tessellator.startDrawingQuads();

        // Borders
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV(xEnd, yEnd, -zPos, f7, f9);
        tessellator.addVertexWithUV(xStart, yEnd, -zPos, f8, f9);
        tessellator.addVertexWithUV(xStart, yEnd, zPos, f8, f10);
        tessellator.addVertexWithUV(xEnd, yEnd, zPos, f7, f10);

        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        tessellator.addVertexWithUV(xEnd, yStart, zPos, f7, f9);
        tessellator.addVertexWithUV(xStart, yStart, zPos, f8, f9);
        tessellator.addVertexWithUV(xStart, yStart, -zPos, f8, f10);
        tessellator.addVertexWithUV(xEnd, yStart, -zPos, f7, f10);

        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        tessellator.addVertexWithUV(xEnd, yEnd, zPos, f12, f13);
        tessellator.addVertexWithUV(xEnd, yStart, zPos, f12, f14);
        tessellator.addVertexWithUV(xEnd, yStart, -zPos, f11, f14);
        tessellator.addVertexWithUV(xEnd, yEnd, -zPos, f11, f13);

        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        tessellator.addVertexWithUV(xStart, yEnd, -zPos, f12, f13);
        tessellator.addVertexWithUV(xStart, yStart, -zPos, f12, f14);
        tessellator.addVertexWithUV(xStart, yStart, zPos, f11, f14);
        tessellator.addVertexWithUV(xStart, yEnd, zPos, f11, f13);

        // back
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(xEnd, yEnd, zPos, f3, f5);
        tessellator.addVertexWithUV(xStart, yEnd, zPos, f4, f5);
        tessellator.addVertexWithUV(xStart, yStart, zPos, f4, f6);
        tessellator.addVertexWithUV(xEnd, yStart, zPos, f3, f6);

        tessellator.draw();

        // Picture
        this.bindEntityTexture(painting);
        tessellator.startDrawingQuads();

        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addVertexWithUV(xEnd, yStart, -zPos, uEnd, vStart);
        tessellator.addVertexWithUV(xStart, yStart, -zPos, uStart, vStart);
        tessellator.addVertexWithUV(xStart, yEnd, zPos, uStart, vEnd);
        tessellator.addVertexWithUV(xEnd, yEnd, zPos, uEnd, vEnd);

        tessellator.draw();

        /*
         * 
         * }
         * }
         */
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
