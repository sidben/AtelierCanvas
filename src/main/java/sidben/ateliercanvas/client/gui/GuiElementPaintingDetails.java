package sidben.ateliercanvas.client.gui;

import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.helper.LocalizationHelper;
import sidben.ateliercanvas.helper.LocalizationHelper.Category;
import sidben.ateliercanvas.helper.MouseHelper;
import sidben.ateliercanvas.reference.ColorTable;
import sidben.ateliercanvas.reference.TextFormatTable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * Display details of the selected painting on the custom painting selection GUI.
 * 
 * 
 * @see sidben.ateliercanvas.client.gui.GuiScreenCustomPaintingsManage
 * @author sidben
 * 
 */
@SideOnly(Side.CLIENT)
public class GuiElementPaintingDetails extends GuiElementPaintingIconLoader
{

    private String _tooltip;



    public GuiElementPaintingDetails(GuiScreen ownerGui, CustomPaintingConfigItem entryData) {
        super(ownerGui, entryData);
    }



    @Override
    public void updateConfigItem(CustomPaintingConfigItem entryData)
    {
        super.updateConfigItem(entryData);
    }



    @SuppressWarnings("rawtypes")
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (this.getConfigItem() != null && this.getConfigItem().isValid()) {

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


            // Painting title
            final String paintingName = this.getConfigItem().getPaintingTitle();

            // Painting extra info (author, file size, dimensions)
            String extraInfo = LocalizationHelper.translateFormatted(Category.CONFIG_PAINTING_INFO, "author", this.getConfigItem().getPaintingAuthor()) + "\n";

            /*
             * NOTE: I have to format the file size here, because "%.1f" in the language file will be replace by "%s"
             * on the StringTranslate.parseLangFile() method.
             */
            extraInfo += LocalizationHelper.translateFormatted(Category.CONFIG_PAINTING_INFO, "filesize", String.format("%.1f", super.getFileSizeKBytes())) + "\n";

            if (super.hasValidImage()) {
                extraInfo += LocalizationHelper.translateFormatted(Category.CONFIG_PAINTING_INFO, "dimensions", super.getTileWidth(), super.getTileHeight(), super.getIconWidth(),
                        super.getIconHeight());
            } else {
                extraInfo += LocalizationHelper.translate(Category.CONFIG_PAINTING_INFO, "dimensions_empty");
            }


            // Draw the background box for the painting
            Gui.drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0x77000000);


            if (super.hasValidImage()) {
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
            }



            // Draw the painting title
            this.mc.fontRenderer.drawStringWithShadow(paintingName, textStartX, textStartY, ColorTable.YELLOW);

            // Draw the painting extra info
            final List extraInfoList = this.mc.fontRenderer.listFormattedStringToWidth(extraInfo, 157);
            for (int i = 0; i < extraInfoList.size(); i++) {
                this.mc.fontRenderer.drawStringWithShadow((String) extraInfoList.get(i), textStartX, textStartY + titleMaginBottom + (i * lineSpacing), ColorTable.GRAY);
            }


            // Tooltip
            this._tooltip = "";
            final boolean isMouseOverIconArea = MouseHelper.isMouseInside(mouseX, mouseY, boxX, boxY, boxWidth, boxHeight);
            if (isMouseOverIconArea) {
                this._tooltip = TextFormatTable.BOLD + this.getConfigItem().getPaintingFileName() + TextFormatTable.RESET + "\n";
                this._tooltip += LocalizationHelper.translateFormatted(Category.CONFIG_PAINTING_INFO, "date_created", this.getConfigItem().getFormatedCreationDate()) + "\n";
                this._tooltip += LocalizationHelper.translateFormatted(Category.CONFIG_PAINTING_INFO, "date_updated", this.getConfigItem().getFormatedCreationDate()) + "\n";
                this._tooltip += "UUID: " + this.getConfigItem().getUUID();
            }


        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }



    /**
     * Tooltip for the list item under the mouse.
     */
    public String getTooltip()
    {
        return this._tooltip == null ? "" : this._tooltip;
    }


}
