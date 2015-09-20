package sidben.ateliercanvas.client.gui;

import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import sidben.ateliercanvas.client.config.CustomPaintingConfigItem;
import sidben.ateliercanvas.reference.ColorTable;
import sidben.ateliercanvas.reference.TextFormatTable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * Display details of the selected painting on the custom painting selection GUI.
 * 
 * 
 * @see sidben.ateliercanvas.client.gui.GuiScreenCustomPaintings
 * @author sidben
 * 
 */
@SideOnly(Side.CLIENT)
public class GuiPaintingDetails extends GuiCustomPaintingIconLoader
{

    private String _tooltip;



    public GuiPaintingDetails(GuiScreenCustomPaintings ownerGui, CustomPaintingConfigItem entryData) {
        super(ownerGui, entryData);
    }



    @Override
    public void updateConfigItem(CustomPaintingConfigItem entryData)
    {
        super.updateConfigItem(entryData);
    }



    @SuppressWarnings("rawtypes")
    @Override
    public void drawScreen(int mouseX, int mouseY, float param3)
    {
        if (this._entryData != null && this._entryData.isValid()) {

            // Selected painting extra info
            final int boxX = this._ownerGui.width / 2 + 4;
            final int boxY = 32;
            final int boxWidth = 200;
            int boxHeight = 64;
            final int lineSpacing = 11;
            final int titleMaginTop = 35;
            final int titleMaginBottom = 15;

            // Size of the preview image
            int sampleWidth = super.getIconWidth() > 64 ? 64 : super.getIconWidth();
            int sampleHeight = super.getIconHeight() > 64 ? 64 : super.getIconHeight();
            // TODO: support for paintings larger than 64x64 pixels

            // If the GUI is large, allows more info and a bigger picture
            if (this._ownerGui.height > 320) {
                boxHeight = 128;
                sampleWidth *= 2;
                sampleHeight *= 2;
            }

            final int textStartX = this._ownerGui.width / 2 + 4;
            final int textStartY = boxY + boxHeight + titleMaginTop;


            final String paintingName = this._entryData.getPaintingTitle();
            String extraInfo = String.format("%s: %s", StatCollector.translateToLocal(this.getLanguageKey("author_label")), this._entryData.getPaintingAuthor());
            extraInfo += String.format("\n%s: %.1f KB", StatCollector.translateToLocal(this.getLanguageKey("filesize_label")), super.getFileSizeKBytes());
            extraInfo += String.format("\n%s: %dx%d (%dx%d pixels)", StatCollector.translateToLocal(this.getLanguageKey("size_label")), super.getTileWidth(), super.getTileHeight(),
                    super.getIconWidth(), super.getIconHeight());


            // Draw the background box for the painting
            Gui.drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0x77000000);

            // Painting icon texture
            this.mc.getTextureManager().bindTexture(super.getPaintingIcon());


            // Icon ratio for non-square images
            int paddingTop = 0;
            int paddingLeft = 0;

            if (sampleHeight < boxHeight) {
                paddingTop = (boxHeight - sampleHeight) / 2;
            }
            if (sampleWidth < boxWidth) {
                paddingLeft = (boxWidth - sampleWidth) / 2;
            }


            // Draw the icon
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.func_146110_a(boxX + paddingLeft, boxY + paddingTop, 0.0F, 0.0F, sampleWidth, sampleHeight, sampleWidth, sampleHeight);



            // Draw the painting title
            this.mc.fontRenderer.drawStringWithShadow(paintingName, textStartX, textStartY, ColorTable.YELLOW);

            // Draw the painting extra info
            final List extraInfoList = this.mc.fontRenderer.listFormattedStringToWidth(extraInfo, 157);
            for (int i = 0; i < extraInfoList.size(); i++) {
                this.mc.fontRenderer.drawStringWithShadow((String) extraInfoList.get(i), textStartX, textStartY + titleMaginBottom + (i * lineSpacing), ColorTable.GRAY);
            }


            // Tooltip
            this._tooltip = "";
            final boolean isMouseOverIconArea = (mouseX >= boxX && mouseX <= boxX + boxWidth && mouseY >= boxY && mouseY <= boxY + boxHeight);
            if (isMouseOverIconArea) {
                this._tooltip = TextFormatTable.BOLD + this._entryData.getPaintingFileName() + TextFormatTable.RESET;
                this._tooltip += String.format("\n%s: %s", StatCollector.translateToLocal(this.getLanguageKey("date_created_label")), this._entryData.getFormatedCreationDate());
                this._tooltip += String.format("\n%s: %s", StatCollector.translateToLocal(this.getLanguageKey("date_updated_label")), this._entryData.getFormatedLastUpdateDate());
                this._tooltip += "\nUUID: " + this._entryData.getUUID();
            }


        }

        super.drawScreen(mouseX, mouseY, param3);
    }



    /**
     * Tooltip for the list item under the mouse.
     */
    public String getTooltip()
    {
        return this._tooltip == null ? "" : this._tooltip;
    }



    /**
     * Returns the full language key for elements of this GUI.
     */
    @Override
    protected String getLanguageKey(String name)
    {
        return "sidben.ateliercanvas.config.painting_info." + name;
    }

}
