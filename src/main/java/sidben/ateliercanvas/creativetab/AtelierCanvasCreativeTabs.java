package sidben.ateliercanvas.creativetab;

import java.util.List;
import sidben.ateliercanvas.init.MyItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class AtelierCanvasCreativeTabs extends CreativeTabs
{

    public AtelierCanvasCreativeTabs(String unlocalizedName) {
        super(unlocalizedName);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
        return Items.painting;
    }

    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllReleventItems(List itemList) {
        
        //--- Regular mod content
        itemList.add(new ItemStack(MyItems.randomPainting, 1));

        //--- Adds custom paintings
        MyItems.customPainting.getSubItems(MyItems.customPainting, this, itemList);

    }
    
}
