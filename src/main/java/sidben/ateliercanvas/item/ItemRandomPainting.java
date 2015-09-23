package sidben.ateliercanvas.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.helper.EnumAuthenticity;
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
    // Actions
    // --------------------------------------------------------------------

    @Override
    /**
     * Called whenever this item is equipped and the right mouse button is pressed.
     */
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        System.out.println("onItemRightClick()");

        // Creates a new custom painting stack (args: item, amount, damage)
        final ItemStack painting = new ItemStack(MyItems.customPainting, 1, 0);


        // Only on server...
        if (!world.isRemote) {

            // Gets a random painting
            final CustomPaintingConfigItem paintingConfig = ConfigurationHandler.getRandomPainting();

            // Adds custom NBT
            if (paintingConfig != null) {
                final String uniqueId = paintingConfig.getUUID();
                System.out.println(uniqueId);
                System.out.println(paintingConfig.toString());
                System.out.println(world.rand.nextInt(100));

                painting.setTagInfo(ItemCustomPainting.NBTPaintingUUID, new NBTTagString(uniqueId));

                if (!paintingConfig.getPaintingTitleRaw().isEmpty()) {
                    painting.setTagInfo(ItemCustomPainting.NBTPaintingTitle, new NBTTagString(paintingConfig.getPaintingTitleRaw().trim()));
                }

                if (!paintingConfig.getPaintingAuthorRaw().isEmpty()) {
                    painting.setTagInfo(ItemCustomPainting.NBTPaintingAuthor, new NBTTagString(paintingConfig.getPaintingAuthorRaw().trim()));
                }

                painting.setTagInfo(ItemCustomPainting.NBTPaintingAuthenticity, new NBTTagByte(EnumAuthenticity.getRandom().getId()));
            }

        }


        // Uses the item (reduces stack)
        --stack.stackSize;


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
