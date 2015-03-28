package sidben.ateliercanvas.client.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.helper.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



@SideOnly(Side.CLIENT)
public class RenderCustomPainting extends Render
{
    
    private static final ResourceLocation vabillaPainting = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
    private final RenderBlocks field_147916_f = new RenderBlocks();
    

    
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
        return this.vabillaPainting;
    }
    
    
    public void doRender(EntityCustomPainting painting, double par2, double par3, double par4, float par5, float par6)
    {
        LogHelper.info("doRender()");
        LogHelper.info("    par2-4: " + par2 + ", " + par3 + ", " + par4);
        LogHelper.info("    par5-6: " + par5 + ", " + par6);
        LogHelper.info("    painting pos: " + painting.posX + ", " + painting.posY + ", " + painting.posZ);
        
        
        GL11.glPushMatrix();
        GL11.glTranslated(par2, par3, par4);
        GL11.glRotatef(par5, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        this.bindEntityTexture(painting);
        float f2 = 0.0625F;
        GL11.glScalef(f2, f2, f2);
        
        f2 = 0.5F;
        
        
        float f = (float)(-par2) / 2.0F;
        float f1 = (float)(-par3) / 2.0F;

        
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addVertexWithUV(f + 16, f1 + 0, (double)(-f2), 1, 0);
        tessellator.addVertexWithUV(f + 0, f1 + 0, (double)(-f2), 0, 0);
        tessellator.addVertexWithUV(f + 0, f1 + 16, (double)(-f2), 0, 1);
        tessellator.addVertexWithUV(f + 16, f1 + 16, (double)(-f2), 1, 1);
        
        tessellator.draw();

        
        
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    


}
