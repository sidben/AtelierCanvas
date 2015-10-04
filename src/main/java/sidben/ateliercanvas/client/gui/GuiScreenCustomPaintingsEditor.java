package sidben.ateliercanvas.client.gui;

import static sidben.ateliercanvas.reference.TextFormatTable.GLYPH_BACK;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.helper.LocalizationHelper;
import sidben.ateliercanvas.helper.LocalizationHelper.Category;
import sidben.ateliercanvas.helper.MouseHelper;
import sidben.ateliercanvas.reference.ColorTable;
import sidben.ateliercanvas.reference.TextFormatTable;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * Screen where the player can edit information about a painting,
 * like author or name.
 * 
 * @author sidben
 */
@SideOnly(Side.CLIENT)
public class GuiScreenCustomPaintingsEditor extends GuiElementPaintingIconLoader
{

    private static final int BT_ID_BACK = 1;
    private static final int BT_ID_DONE = 2;

    private GuiTextField     txtName;
    private GuiTextField     txtAuthor;

    /**
     * Code returned by the confirmClicked, to identify this GUI as the origin.
     */
    final private int        _confirmedReturnCode;



    public GuiScreenCustomPaintingsEditor(GuiScreen parentScreen, CustomPaintingConfigItem entryData, int returnCode) {
        super(parentScreen, entryData);
        this._confirmedReturnCode = returnCode;
    }



    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
        this.txtName.updateCursorCounter();
        this.txtAuthor.updateCursorCounter();
    }


    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }


    @Override
    @SuppressWarnings({ "unchecked" })
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);

        // Buttons
        final int secondColumnX = this.width / 2 + 4;

        this.buttonList.add(new GuiOptionButton(BT_ID_DONE, this.width / 2 - 154, this.height - 29, StatCollector.translateToLocal("gui.done")));
        this.buttonList.add(new GuiUnicodeGlyphButton(BT_ID_BACK, secondColumnX, this.height - 29, 150, 20, " " + StatCollector.translateToLocal("gui.back"), GLYPH_BACK, 2.0F));


        // Text fields
        this.txtName = new GuiTextField(this.fontRendererObj, this.width / 2 - 4 - 200, 38, 200, 20);
        this.txtName.setMaxStringLength(50);
        this.txtName.setEnableBackgroundDrawing(true);
        this.txtName.setTextColor(ColorTable.WHITE);
        this.txtName.setText(super.getConfigItem().getPaintingTitleRaw());
        this.txtName.setFocused(true);

        this.txtAuthor = new GuiTextField(this.fontRendererObj, this.width / 2 - 4 - 200, 67, 200, 20);
        this.txtAuthor.setMaxStringLength(50);
        this.txtAuthor.setEnableBackgroundDrawing(true);
        this.txtAuthor.setTextColor(ColorTable.WHITE);
        this.txtAuthor.setText(super.getConfigItem().getPaintingAuthorRaw());

    }



    @SuppressWarnings("rawtypes")
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        String tooltip = "";


        // Clear the background
        this.drawDefaultBackground();


        // Image icon
        if (this.getConfigItem() != null && this.getConfigItem().isValid()) {

            // Selected painting extra info
            final int boxX = this.width / 2 + 4;
            final int boxY = 32;
            final int boxWidth = 200;
            final int boxHeight = 128;
            final int lineSpacing = 11;
            final int titleMaginTop = 5;
            final int titleMaginBottom = 15;

            // Size of the preview image
            int sampleWidth = super.getIconWidth() > 64 ? 64 : super.getIconWidth();
            int sampleHeight = super.getIconHeight() > 64 ? 64 : super.getIconHeight();

            // If the GUI is large, allows more info and a bigger picture
            sampleWidth *= 2;
            sampleHeight *= 2;

            final int textStartX = this.width / 2 + 4;
            final int textStartY = boxY + boxHeight + titleMaginTop;


            // Painting file name
            final String paintingFilename = this.getConfigItem().getPaintingFileName();

            // Painting extra info (file size, dimensions)
            /*
             * NOTE: I have to format the file size here, because "%.1f" in the language file will be replace by "%s"
             * on the StringTranslate.parseLangFile() method.
             */
            String extraInfo = LocalizationHelper.translateFormatted(Category.CONFIG_PAINTING_INFO, "filesize", String.format("%.1f", super.getFileSizeKBytes())) + "\n";
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
            this.mc.fontRenderer.drawStringWithShadow(paintingFilename, textStartX, textStartY, ColorTable.YELLOW);

            // Draw the painting extra info
            final List extraInfoList = this.mc.fontRenderer.listFormattedStringToWidth(extraInfo, 157);
            for (int i = 0; i < extraInfoList.size(); i++) {
                this.mc.fontRenderer.drawStringWithShadow((String) extraInfoList.get(i), textStartX, textStartY + titleMaginBottom + (i * lineSpacing), ColorTable.GRAY);
            }


            // Tooltip
            final boolean isMouseOverIconArea = MouseHelper.isMouseInside(mouseX, mouseY, boxX, boxY, boxWidth, boxHeight);
            if (isMouseOverIconArea) {
                tooltip = TextFormatTable.BOLD + this.getConfigItem().getPaintingFileName() + TextFormatTable.RESET + "\n";
                tooltip += LocalizationHelper.translateFormatted(Category.CONFIG_PAINTING_INFO, "date_created", this.getConfigItem().getFormatedCreationDate()) + "\n";
                tooltip += LocalizationHelper.translateFormatted(Category.CONFIG_PAINTING_INFO, "date_updated", this.getConfigItem().getFormatedCreationDate()) + "\n";
                tooltip += "UUID: " + this.getConfigItem().getUUID();
            }


        }


        // Texts - Title, Total paintings installed
        this.drawCenteredString(this.fontRendererObj, LocalizationHelper.translate(Category.CONFIG, "title"), this.width / 2, 8, ColorTable.WHITE);
        this.drawCenteredString(this.fontRendererObj, LocalizationHelper.translate(Category.CONFIG, "title_new"), this.width / 2, 18, ColorTable.WHITE);


        // Text fields labels
        this.drawString(this.fontRendererObj, LocalizationHelper.translate(Category.CONFIG_EDITOR, "title_label"), this.width / 2 - 4 - 200, 37, ColorTable.LIGHT_GRAY);
        this.drawString(this.fontRendererObj, LocalizationHelper.translate(Category.CONFIG_EDITOR, "author_label"), this.width / 2 - 4 - 200, 81, ColorTable.LIGHT_GRAY);
        this.drawString(this.fontRendererObj, LocalizationHelper.translate(Category.CONFIG_EDITOR, "enabled_label"), this.width / 2 - 4 - 200, 125, ColorTable.LIGHT_GRAY);
        if (this.getConfigItem().getIsEnabled()) {
            this.drawString(this.fontRendererObj, LocalizationHelper.translate(Category.CONFIG, "enabled"), this.width / 2 - 4 - 200, 138, ColorTable.WHITE);
        } else {
            this.drawString(this.fontRendererObj, LocalizationHelper.translate(Category.CONFIG, "disabled"), this.width / 2 - 4 - 200, 138, ColorTable.WHITE);
        }

        // Text fields
        this.txtName.yPosition = 50;
        this.txtAuthor.yPosition = 94;

        this.txtName.drawTextBox();
        this.txtAuthor.drawTextBox();


        // Parent call (draws buttons)
        super.drawScreen(mouseX, mouseY, partialTicks);


        if (!StringUtils.isNullOrEmpty(tooltip)) {
            super.func_146283_a(this.mc.fontRenderer.listFormattedStringToWidth(tooltip, 300), mouseX, mouseY);
        }

    }



    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled) {

            // Return code
            final boolean confirmed = (button.id == BT_ID_DONE);
            this._parentScreen.confirmClicked(confirmed, this._confirmedReturnCode);

        }
    }



    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char eventChar, int eventKey)
    {
        this.txtName.textboxKeyTyped(eventChar, eventKey);
        this.txtAuthor.textboxKeyTyped(eventChar, eventKey);

        if (eventKey == 15) {
            this.txtName.setFocused(!this.txtName.isFocused());
            this.txtAuthor.setFocused(!this.txtAuthor.isFocused());
        }

        if (eventKey == 28 || eventKey == 156) {
            this.actionPerformed((GuiButton) this.buttonList.get(0));
        }
    }



    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int partialTicks)
    {
        super.mouseClicked(mouseX, mouseY, partialTicks);
        this.txtName.mouseClicked(mouseX, mouseY, partialTicks);
        this.txtAuthor.mouseClicked(mouseX, mouseY, partialTicks);
    }



    /**
     * Returns the name of the painting from the textbox.
     */
    public String getPaintingName()
    {
        return this.txtName.getText();
    }


    /**
     * Returns the author of the painting from the textbox.
     */
    public String getPaintingAuthor()
    {
        return this.txtAuthor.getText();
    }


}
