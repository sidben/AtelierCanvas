package sidben.ateliercanvas.client.gui;


/**
 * Interface for screens containing a GuiElementPaintingList 
 *
 * @see sidben.ateliercanvas.client.gui.GuiElementPaintingList
 * @author sidben
 */
public interface IListContainer
{
    public void onItemSelected(GuiElementPaintingList list, int index);
}
