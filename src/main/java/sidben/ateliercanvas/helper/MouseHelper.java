package sidben.ateliercanvas.helper;

import net.minecraft.client.gui.GuiButton;


public class MouseHelper
{
    
    /**
     * @return TRUE if the given mouse coordinates are inside the given element.
     */
    public static boolean isMouseInside(int mouseX, int mouseY, GuiButton button) {
        return MouseHelper.isMouseInside(mouseX, mouseY, button.xPosition, button.yPosition, button.width, button.height);
    }
    
    
    /**
     * @return TRUE if the given mouse coordinates are inside the given element coordinates.
     */
    public static boolean isMouseInside(int mouseX, int mouseY, int elementX, int elementY, int elementWidth, int elementHeight) {
        return (mouseX >= elementX 
                && mouseX <= elementX + elementWidth 
                && mouseY >= elementY 
                && mouseY <= elementY + elementHeight);
    }

}
