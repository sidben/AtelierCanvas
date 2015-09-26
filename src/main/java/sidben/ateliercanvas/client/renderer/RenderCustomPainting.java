package sidben.ateliercanvas.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sidben.ateliercanvas.ModAtelierCanvas;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.reference.TextFormatTable;
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
        
        this.func_147914_a(painting, x + (double)((float)Direction.offsetX[painting.hangingDirection] * 0.3F), y - 0.25D, z + (double)((float)Direction.offsetZ[painting.hangingDirection] * 0.3F));
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
        
        int iMax = width / 16;
        int jMax = height / 16;

        for (int i = 0; i < iMax; ++i) {

            for (int j = 0; j < jMax; ++j) {

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


                // Borders - top
                if (j == jMax - 1) {
                    tessellator.setNormal(0.0F, 1.0F, 0.0F);
                    tessellator.addVertexWithUV((double)xEnd, (double)yEnd, (double)(-zPos), (double)f7, (double)f9);
                    tessellator.addVertexWithUV((double)xStart, (double)yEnd, (double)(-zPos), (double)f8, (double)f9);
                    tessellator.addVertexWithUV((double)xStart, (double)yEnd, (double)zPos, (double)f8, (double)f10);
                    tessellator.addVertexWithUV((double)xEnd, (double)yEnd, (double)zPos, (double)f7, (double)f10);
                }
                
                // Borders - bottom
                if (j == 0) {
                    tessellator.setNormal(0.0F, -1.0F, 0.0F);
                    tessellator.addVertexWithUV((double)xEnd, (double)yStart, (double)zPos, (double)f7, (double)f9);
                    tessellator.addVertexWithUV((double)xStart, (double)yStart, (double)zPos, (double)f8, (double)f9);
                    tessellator.addVertexWithUV((double)xStart, (double)yStart, (double)(-zPos), (double)f8, (double)f10);
                    tessellator.addVertexWithUV((double)xEnd, (double)yStart, (double)(-zPos), (double)f7, (double)f10);
                }

                // Borders - left
                if (i == iMax - 1) {
                    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                    tessellator.addVertexWithUV((double)xEnd, (double)yEnd, (double)zPos, (double)f12, (double)f13);
                    tessellator.addVertexWithUV((double)xEnd, (double)yStart, (double)zPos, (double)f12, (double)f14);
                    tessellator.addVertexWithUV((double)xEnd, (double)yStart, (double)(-zPos), (double)f11, (double)f14);
                    tessellator.addVertexWithUV((double)xEnd, (double)yEnd, (double)(-zPos), (double)f11, (double)f13);
                }    

                // Borders - right
                if (i == 0) {
                    tessellator.setNormal(1.0F, 0.0F, 0.0F);
                    tessellator.addVertexWithUV((double)xStart, (double)yEnd, (double)(-zPos), (double)f12, (double)f13);
                    tessellator.addVertexWithUV((double)xStart, (double)yStart, (double)(-zPos), (double)f12, (double)f14);
                    tessellator.addVertexWithUV((double)xStart, (double)yStart, (double)zPos, (double)f11, (double)f14);
                    tessellator.addVertexWithUV((double)xStart, (double)yEnd, (double)zPos, (double)f11, (double)f13);
                }

                
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

    
    
    
    /**
     * Renders the name of the painting, like an item frame renders the item custom name.
     */
    protected void func_147914_a(EntityCustomPainting painting, double x, double y, double z)
    {
        System.out.println("func_147914_a()");
        System.out.println("    " + Minecraft.isGuiEnabled());
        
            
        if (Minecraft.isGuiEnabled() && this.renderManager.livingPlayer.isSneaking())
        {
            float titleSize = 0.016666668F * 1.4F;
            float subtitleSize = 0.016666668F * 1.0F;
            double playerDistance = painting.getDistanceSqToEntity(this.renderManager.livingPlayer);
            float displayDistance = 8.0F;
            float actualHeight = (float) (painting.boundingBox.maxY - painting.boundingBox.minY);
            float directionAngle = 0F;
            float titleBoxOpacity = 0.5F;
            
            System.out.println(String.format("    %.3f | %.3f | %.3f   (%.1f)", (float)x, (float)y, (float)z, actualHeight));
            System.out.println("    " + playerDistance + " of " + displayDistance);
            System.out.println("    " + this.renderManager.livingPlayer.isSneaking());
            System.out.println("    " + this.renderManager.playerViewX + " / " + this.renderManager.playerViewY);
            System.out.println("    " + painting.hangingDirection + ": " + Direction.offsetX[painting.hangingDirection] + " x " + Direction.offsetZ[painting.hangingDirection]);
            

            if (playerDistance < displayDistance)
            {
                String paintingTitle = painting.getTitle();
                String paintingSubtitle = painting.getAuthor();
                int maxTextSize = 128;

                // Formats the title and subtitle
                if (!StringUtils.isNullOrEmpty(paintingTitle)){
                    paintingTitle = this.getFontRendererFromRenderManager().trimStringToWidth(paintingTitle, maxTextSize);
                    if (painting.getIsAuthentic()) {
                        paintingTitle = TextFormatTable.COLOR_YELLOW + paintingTitle;
                    }
                }
                if (!StringUtils.isNullOrEmpty(paintingSubtitle)){
                    paintingSubtitle = StatCollector.translateToLocalFormatted("sidben.ateliercanvas:item.custom_painting.author_label", new Object[] { this.getFontRendererFromRenderManager().trimStringToWidth(paintingSubtitle, maxTextSize) });
                }
                
                
                // Finds the angle to rotate the floating text. I bet there is a better way to do this...
                if (painting.hangingDirection == 0) {
                    directionAngle = 180.0F;
                } else if (painting.hangingDirection == 1) {
                    directionAngle = 90.0F;
                } else if (painting.hangingDirection == 3) {
                    directionAngle = -90.0F;
                }


                FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                Tessellator tessellator = Tessellator.instance;
                int i;

                
                // Title
                if (!StringUtils.isNullOrEmpty(paintingTitle)){
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)x, (float)y + 0.45F - (actualHeight / 2), (float)z);
                    
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(directionAngle, 0.0F, 1.0F, 0.0F);                               // -this.renderManager.playerViewY
                    GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);               // this.renderManager.playerViewX
                    GL11.glScalef(-titleSize, -titleSize, titleSize);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glTranslatef(0.0F, 1.0F, 0.0F);
                    GL11.glDepthMask(false);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    
                    // Draws the box behind the text
                    i = fontrenderer.getStringWidth(paintingTitle) / 2;
                    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, titleBoxOpacity);
                    tessellator.addVertex((double)(-i - 1), -1.0D, 0.0D);
                    tessellator.addVertex((double)(-i - 1), 8.0D, 0.0D);
                    tessellator.addVertex((double)(i + 1), 8.0D, 0.0D);
                    tessellator.addVertex((double)(i + 1), -1.0D, 0.0D);
                    tessellator.draw();
                    
                    // Draws the text
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    fontrenderer.drawString(paintingTitle, -fontrenderer.getStringWidth(paintingTitle) / 2, 0, 553648127);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDepthMask(true);
                    fontrenderer.drawString(paintingTitle, -fontrenderer.getStringWidth(paintingTitle) / 2, 0, -1);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();                    
                }


                
                
                
                // Subtitle
                if (!StringUtils.isNullOrEmpty(paintingSubtitle)){
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)x, (float)y + 0.2F - (actualHeight / 2), (float)z);
                    
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(directionAngle, 0.0F, 1.0F, 0.0F);                               // -this.renderManager.playerViewY
                    GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);               // this.renderManager.playerViewX
                    GL11.glScalef(-subtitleSize, -subtitleSize, subtitleSize);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glTranslatef(0.0F, 1.0F, 0.0F);
                    GL11.glDepthMask(false);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    
                    // Draws the box behind the text
                    i = fontrenderer.getStringWidth(paintingSubtitle) / 2;
                    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, titleBoxOpacity);
                    tessellator.addVertex((double)(-i - 1), -1.0D, 0.0D);
                    tessellator.addVertex((double)(-i - 1), 8.0D, 0.0D);
                    tessellator.addVertex((double)(i + 1), 8.0D, 0.0D);
                    tessellator.addVertex((double)(i + 1), -1.0D, 0.0D);
                    tessellator.draw();
                    
                    // Draws the text
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    fontrenderer.drawString(paintingSubtitle, -fontrenderer.getStringWidth(paintingSubtitle) / 2, 0, 553648127);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDepthMask(true);
                    fontrenderer.drawString(paintingSubtitle, -fontrenderer.getStringWidth(paintingSubtitle) / 2, 0, -1);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();
                }
                
                /*
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(true);
                fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
                */

                
                /*
                if (painting.isSneaking())
                {
                    FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)x + 0.0F, (float)y + painting.height + 0.5F, (float)z);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GL11.glScalef(-f1, -f1, f1);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                    GL11.glDepthMask(false);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    Tessellator tessellator = Tessellator.instance;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    int i = fontrenderer.getStringWidth(s) / 2;
                    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                    tessellator.addVertex((double)(-i - 1), -1.0D, 0.0D);
                    tessellator.addVertex((double)(-i - 1), 8.0D, 0.0D);
                    tessellator.addVertex((double)(i + 1), 8.0D, 0.0D);
                    tessellator.addVertex((double)(i + 1), -1.0D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glDepthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();
                }
                else
                {
                    this.func_147906_a(painting, s, x, y, z, 64);
                }
                */
            }
        }
    }

    
}
