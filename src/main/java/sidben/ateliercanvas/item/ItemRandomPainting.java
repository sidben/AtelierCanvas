package sidben.ateliercanvas.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.init.MyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemRandomPainting extends Item
{


    // --------------------------------------------------------------------
    // Constants
    // --------------------------------------------------------------------
    public static final String unlocalizedName = "random_painting";



    // --------------------------------------------------------------------
    // Constructors
    // --------------------------------------------------------------------
    public ItemRandomPainting() {
        this.setUnlocalizedName(ItemRandomPainting.unlocalizedName);
        this.setHasSubtypes(false);
    }



    // --------------------------------------------------------------------
    // Textures and Rendering
    // --------------------------------------------------------------------

    /*
     * When this method is called, your item should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        super.itemIcon = iconRegister.registerIcon(MyItems.randomPaintingIcon);
    }



    // ----------------------------------------------------
    // Item name and flavor text
    // ----------------------------------------------------

    @Override
    public String getUnlocalizedName()
    {
        // TODO: encapsulate into Reference.ResourcesNamespace
        return String.format("%s:item.%s", "sidben.ateliercanvas", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return String.format("%s:item.%s", "sidben.ateliercanvas", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }



    // --------------------------------------------------------------------
    // Behavior
    // --------------------------------------------------------------------

    /*
     * TODO: create an animation for unwrapping the painting. Right now there is a bug where the item is used twice.
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 10;
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.none;
    }
    */
    
    
    
    
    // --------------------------------------------------------------------
    // Actions
    // --------------------------------------------------------------------

    @Override
    /**
     * Called whenever this item is equipped and the right mouse button is pressed.
     */
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        System.out.println("onItemRightClick()");
        // player.setItemInUse(stack, this.getMaxItemUseDuration(stack));

        // Sound
        player.playSound("ateliercanvas:painting_open", 1.0F, 1.0F);
        
        return this.giveRandomPainting(stack, world, player);
    }
    
    
    /**
     * Called when an item finished the useDuration time.
     */
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
        return super.onEaten(stack, world, player);
    }
    
    
    private ItemStack giveRandomPainting(ItemStack stack, World world, EntityPlayer player)
    {

        // Uses the item (reduces stack)
        --stack.stackSize;

        
        // Creates a new custom painting stack (args: item, amount, damage)
        final ItemStack painting = new ItemStack(MyItems.customPainting, 1, 0);


        // Only on server...
        if (!world.isRemote) {

            // Gets a random painting
            final CustomPaintingConfigItem paintingConfig = ConfigurationHandler.getRandomPainting();
            MyItems.customPainting.addPaintingData(painting, paintingConfig, true);

        }


        /*
         * If the player only had one item, that item gets replaced. If he had a bigger stack,
         * drops the item if the player could not pick it up (because of full inventory).
         */
        if (stack.stackSize <= 0) {
            return painting;
        } else {
            if (!player.inventory.addItemStackToInventory(painting.copy())) {
                player.dropPlayerItemWithRandomChoice(painting, false);
            }

            return stack;
        }
    }

}
