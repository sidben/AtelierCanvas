package sidben.ateliercanvas.client.config;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.config.GuiConfigEntries.ListEntryBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/* 
 * Refs: 
 *      cpw.mods.fml.client.config.GuiEditArrayEntries.BaseEntry.StringEntry
 *      cpw.mods.fml.client.config.GuiConfigEntries.StringEntry
 */


@SideOnly(Side.CLIENT)
public class PaintingEntry extends ListEntryBase
{
    protected final GuiTextField textFieldValue;
    protected final String       beforeValue;

    public PaintingEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<?> configElement)
    {
        super(owningScreen, owningEntryList, configElement);
        beforeValue = configElement.get().toString();
        this.textFieldValue = new GuiTextField(this.mc.fontRenderer, this.owningEntryList.controlX + 1, 0, this.owningEntryList.controlWidth - 3, 16);
        this.textFieldValue.setMaxStringLength(10000);
        this.textFieldValue.setText(configElement.get().toString());
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
    {
        super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
        this.textFieldValue.xPosition = this.owningEntryList.controlX + 2;
        this.textFieldValue.yPosition = y + 1;
        this.textFieldValue.width = 50;  // this.owningEntryList.controlWidth - 4;
        this.textFieldValue.height = 40;
        this.textFieldValue.setEnabled(enabled());
        this.textFieldValue.drawTextBox();
    }

    @Override
    public void keyTyped(char eventChar, int eventKey)
    {
        if (enabled() || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
        {
            this.textFieldValue.textboxKeyTyped((enabled() ? eventChar : Keyboard.CHAR_NONE), eventKey);

            if (configElement.getValidationPattern() != null)
            {
                if (configElement.getValidationPattern().matcher(this.textFieldValue.getText().trim()).matches())
                    isValidValue = true;
                else
                    isValidValue = false;
            }
        }
    }

    @Override
    public void updateCursorCounter()
    {
        this.textFieldValue.updateCursorCounter();
    }

    @Override
    public void mouseClicked(int x, int y, int mouseEvent)
    {
        this.textFieldValue.mouseClicked(x, y, mouseEvent);
    }

    @Override
    public boolean isDefault()
    {
        return configElement.getDefault() != null ? configElement.getDefault().toString().equals(this.textFieldValue.getText()) :
            this.textFieldValue.getText().trim().isEmpty();
    }

    @Override
    public void setToDefault()
    {
        if (enabled())
        {
            this.textFieldValue.setText(this.configElement.getDefault().toString());
            keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_HOME);
        }
    }

    @Override
    public boolean isChanged()
    {
        return beforeValue != null ? !this.beforeValue.equals(textFieldValue.getText()) : this.textFieldValue.getText().trim().isEmpty();
    }

    @Override
    public void undoChanges()
    {
        if (enabled())
            this.textFieldValue.setText(beforeValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean saveConfigElement()
    {
        if (enabled())
        {
            if (isChanged() && this.isValidValue)
            {
                this.configElement.set(this.textFieldValue.getText());
                return configElement.requiresMcRestart();
            }
            else if (isChanged() && !this.isValidValue)
            {
                this.configElement.setToDefault();
                return configElement.requiresMcRestart()
                        && beforeValue != null ? beforeValue.equals(configElement.getDefault()) : configElement.getDefault() == null;
            }
        }
        return false;
    }

    @Override
    public Object getCurrentValue()
    {
        return this.textFieldValue.getText();
    }

    @Override
    public Object[] getCurrentValues()
    {
        return new Object[] { getCurrentValue() };
    }
    
}