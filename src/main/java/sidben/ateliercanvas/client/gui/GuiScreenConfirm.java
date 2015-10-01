package sidben.ateliercanvas.client.gui;

import static sidben.ateliercanvas.reference.TextFormatTable.GLYPH_BACK;
import org.lwjgl.opengl.GL11;
import sidben.ateliercanvas.reference.ColorTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



/*
 * OBS: This class is similar to net.minecraft.client.gui.GuiYesNo
 */


@SideOnly(Side.CLIENT)
public class GuiScreenConfirm extends GuiScreen
{
    
    private static final int BT_ID_YES      = 1;
    private static final int BT_ID_NO   = 2;

    
    /**
     * Code returned by the confirmClicked, to identify this GUI as the origin.
     */
    final private int _confirmedReturnCode; 
    
    /**
     * Confirmation message displayed.
     */
    final private String _message;
    
    
    private final GuiScreen   _parentScreen;
    
    
    
    public int oldSelectedIndex;
    


    /**
     * @param parentScreen
     * @param message Confirmation message displayed.
     * @param returnCode Code returned by the confirmClicked, to identify this GUI as the origin.
     */
    public GuiScreenConfirm(GuiScreen parentScreen, String message, int returnCode) {
        this.mc = Minecraft.getMinecraft();
        this._parentScreen = parentScreen;
        this._message = message;
        this._confirmedReturnCode = returnCode;
        
        this.width = parentScreen.width;
        this.height = parentScreen.height;
    }

    
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        final int buttonX1 = this.width / 2 - 154;
        final int buttonX2 = this.width / 2 + 4;
        final int buttonY = 64;

        this.buttonList.add(new GuiOptionButton(BT_ID_YES, buttonX1, buttonY, StatCollector.translateToLocal("gui.yes")));
        this.buttonList.add(new GuiOptionButton(BT_ID_NO, buttonX2, buttonY, StatCollector.translateToLocal("gui.no")));
    }

    
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        
        this.drawDefaultBackground();
        
        // Overlay
        // Gui.drawRect(100, 100, this.width, this.height, 0xcc000000);
        // GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // Texts - Title, Total paintings installed
        this.drawCenteredString(this.mc.fontRenderer, this._message, this.width / 2, 40, ColorTable.WHITE);

        // Parent call (draws buttons)
        super.drawScreen(mouseX, mouseY, partialTicks);

    }
    
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled) {

            if (button.id == BT_ID_YES || button.id == BT_ID_NO) {
                
                // Return code
                boolean confirmed = (button.id == BT_ID_YES);
                this._parentScreen.confirmClicked(confirmed, this._confirmedReturnCode);

            }

        }
    }

    
    
}
