package sidben.ateliercanvas.client.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.reference.ColorTable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * Represents one item of the GuiElementPaintingList ListBox.
 * 
 * 
 * @see sidben.ateliercanvas.client.gui.GuiElementPaintingList
 * @see net.minecraft.client.resources.ResourcePackListEntry
 * @author sidben
 * 
 */
@SideOnly(Side.CLIENT)
public class GuiElementPaintingListEntry extends GuiElementPaintingIconLoader implements GuiListExtended.IGuiListEntry
{
    /*
     * OBS: GuiElementPaintingIconLoader extends GuiScreen, but this class don't need to be a GuiScreen.
     * 
     * Will this cause any problems? Find out on the next episode!
     */



    private String _tooltip;
    



    public GuiElementPaintingListEntry(GuiScreen ownerGui, CustomPaintingConfigItem entryData) {
        super(ownerGui, entryData);
    }



    @Override
    public void drawEntry(int index, int listInitialX, int listInitialY, int listItemWidth, int listItemHeight, Tessellator p_148279_6_, int mouseX, int mouseY, boolean mouseOver)
    {
        String paintingName = "???";
        String paintingInfo1 = "";
        String paintingInfo2 = "";



        // Painting icon texture
        this.mc.getTextureManager().bindTexture(super.getPaintingIcon());


        // Size of the preview image on the right
        int sampleWidth = 32, sampleHeight = 32;

        // Icon ratio for non-square images
        float iconWidthStretchRatio = 1.0F;
        float iconHeightStretchRatio = 1.0F;
        int paddingTop = 0;
        int paddingLeft = 0;


        if (this.getConfigItem().isValid()) {
            paintingName = this.getConfigItem().getPaintingTitle();
            paintingInfo1 = String.format("%s: %s", StatCollector.translateToLocal(this.getLanguageKey("author_label")), this.getConfigItem().getPaintingAuthor());
            if (super.hasValidImage()) {
                paintingInfo2 = String.format("%s: %dx%d", StatCollector.translateToLocal(this.getLanguageKey("size_label")), super.getTileWidth(), super.getTileHeight());
            } else {
                paintingInfo2 = String.format("%s: -", StatCollector.translateToLocal(this.getLanguageKey("size_label")));
            }


            // Adjusts the width/height ratio and padding to display the painting correctly
            if (super.getIconWidth() > super.getIconHeight()) {
                iconHeightStretchRatio = (float) super.getIconHeight() / (float) super.getIconWidth();
                paddingTop = (int) ((sampleWidth * (1F - iconHeightStretchRatio)) / 2);
            } else if (super.getIconHeight() > super.getIconWidth()) {
                iconWidthStretchRatio = (float) super.getIconWidth() / (float) super.getIconHeight();
                paddingLeft = (int) ((sampleHeight * (1F - iconWidthStretchRatio)) / 2);
            }

            sampleWidth = (int) (sampleWidth * iconWidthStretchRatio);
            sampleHeight = (int) (sampleHeight * iconHeightStretchRatio);
        }



        // On mouse over
        if ((this.mc.gameSettings.touchscreen || mouseOver)) {
            // Hover
            Gui.drawRect(listInitialX - 2, listInitialY - 2, listInitialX + listItemWidth - 10, listInitialY + listItemHeight + 2, 0x33FFFFFF);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            // Tooltip
            this._tooltip = this.getConfigItem().getValiadtionErrors();
        }



        // Draw the icon
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.func_146110_a(listInitialX + paddingLeft, listInitialY + paddingTop, 0.0F, 0.0F, sampleWidth, sampleHeight, 32.0F * iconWidthStretchRatio, 32.0F * iconHeightStretchRatio);


        // Draws the texts
        int textWidth;

        // Painting name
        textWidth = this.mc.fontRenderer.getStringWidth(paintingName);
        if (textWidth > 157) {
            paintingName = this.mc.fontRenderer.trimStringToWidth(paintingName, 157 - this.mc.fontRenderer.getStringWidth("...")) + "...";
        }
        int auxColor = this.getConfigItem().getIsEnabled() ? ColorTable.WHITE : ColorTable.LIGHT_GRAY;
        this.mc.fontRenderer.drawStringWithShadow(paintingName, listInitialX + 32 + 2, listInitialY + 1, auxColor);

        // Painting data (author, size, etc)
        this.mc.fontRenderer.drawStringWithShadow(paintingInfo1, listInitialX + 32 + 2, listInitialY + 12, ColorTable.GRAY);
        this.mc.fontRenderer.drawStringWithShadow(paintingInfo2, listInitialX + 32 + 2, listInitialY + 22, ColorTable.GRAY);

        
        // Draw a mask if the entry is disabled
        if (!this.getConfigItem().getIsEnabled()) {
            Gui.drawRect(listInitialX + paddingLeft, listInitialY + paddingTop, listInitialX + paddingLeft + sampleWidth, listInitialY + paddingTop + sampleHeight, 0xcc000000);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        
    }


    @Override
    public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
        return false;        // True disables the scroll of the listbox. No idea why.
    }


    @Override
    public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }



    /**
     * Tooltip for the list item under the mouse.
     */
    public String getTooltip()
    {
        return StringUtils.isNullOrEmpty(super.getWarningMessage()) ? (this._tooltip == null ? "" : this._tooltip) : super.getWarningMessage();
        
        // TODO: refactor isNullOrEmpty to isBlank (StringUtils.isBlank, not Minecraft)
    }



}
