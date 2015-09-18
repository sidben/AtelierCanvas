package sidben.ateliercanvas.client.gui;

import java.util.List;
import sidben.ateliercanvas.client.config.PaintingSelectorListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// Ref: GuiResourcePackList
@SideOnly(Side.CLIENT)
public class GuiCustomPaintingList extends GuiListExtended
{

    protected final Minecraft mc;

    @SuppressWarnings("rawtypes")
    protected final List valueList;

    
    
    @SuppressWarnings("rawtypes")
    public GuiCustomPaintingList(Minecraft p_i45055_1_, int p_i45055_2_, int p_i45055_3_, List p_i45055_4_)
    {
        super(p_i45055_1_, p_i45055_2_, p_i45055_3_, 32, p_i45055_3_ - 55 + 4, 36);
        this.mc = p_i45055_1_;
        this.valueList = p_i45055_4_;
        this.field_148163_i = false;
    }


    @SuppressWarnings("rawtypes")
    public List func_148201_l()
    {
        return this.valueList;
    }

    protected int getSize()
    {
        return this.func_148201_l().size();
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public PaintingSelectorListEntry getListEntry(int p_148180_1_)
    {
        return (PaintingSelectorListEntry)this.func_148201_l().get(p_148180_1_);
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return this.width;
    }

    protected int getScrollBarX()
    {
        return this.right - 6;
    }

}
