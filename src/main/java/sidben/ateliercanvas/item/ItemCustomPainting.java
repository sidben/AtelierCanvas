package sidben.ateliercanvas.item;

import java.util.List;
import java.util.UUID;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.Direction;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.helper.EnumAuthenticity;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.init.MyItems;
import sidben.ateliercanvas.reference.BlockSide;
import sidben.ateliercanvas.reference.TextFormatTable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



public class ItemCustomPainting extends Item
{

    // --------------------------------------------------------------------
    // Constants
    // --------------------------------------------------------------------
    public static final String unlocalizedName         = "custom_painting";
    public static final String NBTPaintingUUID         = "uuid";
    public static final String NBTPaintingTitle        = "title";
    public static final String NBTPaintingAuthor       = "author";
    public static final String NBTPaintingAuthenticity = "quality";
    public static final String NBTPaintingSize         = "size";



    // --------------------------------------------------------------------
    // Constructors
    // --------------------------------------------------------------------
    public ItemCustomPainting() {
        this.setUnlocalizedName(ItemCustomPainting.unlocalizedName);
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
        super.itemIcon = iconRegister.registerIcon(MyItems.customPaintingIcon);
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



    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.hasTagCompound()) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            final String title = nbttagcompound.getString(ItemCustomPainting.NBTPaintingTitle);

            if (!StringUtils.isNullOrEmpty(title))      // TODO: refactor code where I could use isNullOrEmpty
            {
                return title;
            }
        }

        return super.getItemStackDisplayName(stack);
    }


    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List infolist, boolean debugmode)
    {
        if (stack.hasTagCompound()) {
            // TODO: how to update author and title when the config changes? Maybe I shouldn't store that as NBT at all, or find a way to do a 1-time only check.

            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            final String author = nbttagcompound.getString(ItemCustomPainting.NBTPaintingAuthor);
            final byte qualityId = nbttagcompound.getByte(ItemCustomPainting.NBTPaintingAuthenticity);

            // Author
            if (!StringUtils.isNullOrEmpty(author)) {
                infolist.add(TextFormatTable.COLOR_GRAY + StatCollector.translateToLocalFormatted("sidben.ateliercanvas:item.custom_painting.author_label", new Object[] { author }));
            }

            // Authenticity
            if (qualityId > 0) {
                final EnumAuthenticity qualityType = EnumAuthenticity.parse(qualityId);
                if (qualityType != null && !StringUtils.isNullOrEmpty(qualityType.getTranslatedName())) {
                    infolist.add(TextFormatTable.COLOR_DARKGRAY + TextFormatTable.ITALIC + qualityType.getTranslatedName());
                }
            }

            // Debug info (F3 + H), check the UUID and if the painting is installed
            if (debugmode) {
                final String uniqueId = nbttagcompound.getString(ItemCustomPainting.NBTPaintingUUID);

                if (StringUtils.isNullOrEmpty(uniqueId)) {
                    infolist.add(TextFormatTable.COLOR_RED + "This painting is blank");
                } else {
                    infolist.add(TextFormatTable.COLOR_GRAY + TextFormatTable.ITALIC + uniqueId);

                    final CustomPaintingConfigItem paintingConfig = ConfigurationHandler.findPaintingByUUID(uniqueId);
                    if (paintingConfig == null || !paintingConfig.isValid()) {
                        infolist.add(TextFormatTable.COLOR_RED + "This painting was not found on the config file");
                    }
                }
            }
        } else {
            // Debug info (F3 + H), check the UUID and if the painting is installed
            if (debugmode) {
                infolist.add(TextFormatTable.COLOR_RED + "This painting is empty");
            }
        }
    }



    /**
     * Return an item rarity from EnumRarity
     */
    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        if (stack.hasTagCompound()) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            final byte quality = nbttagcompound.getByte(ItemCustomPainting.NBTPaintingAuthenticity);

            if (quality == EnumAuthenticity.ORIGINAL.getId()) {
                return EnumRarity.uncommon;
            }
        }
        return EnumRarity.common;
    }



    // --------------------------------------------------------------------
    // Actions
    // --------------------------------------------------------------------

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        LogHelper.info("onItemUse()");
        LogHelper.info("    side: " + side);
        LogHelper.info("    pos: " + x + ", " + y + ", " + z);


        // Check if the player clicked on a valid surface (only accept the side of blocks)
        if (side == BlockSide.ABOVE || side == BlockSide.BELOW) {
            return false;

        } else {
            // Find the UUID on the NBT of the painting
            String uniqueId = "";
            if (stack.hasTagCompound()) {
                final NBTTagCompound nbttagcompound = stack.getTagCompound();
                uniqueId = nbttagcompound.getString(ItemCustomPainting.NBTPaintingUUID);
            }


            final int facing = Direction.facingToDirection[side];
            final EntityHanging entityhanging = this.createHangingEntity(world, x, y, z, facing, uniqueId);

            // DEBUG
            if (entityhanging != null) {
                LogHelper.info("    valid surface: " + entityhanging.onValidSurface() + " (ItemCustomPainting)");
            }


            // Check if the player can place blocks
            if (!player.canPlayerEdit(x, y, z, side, stack)) {
                return false;
            } else {
                if (entityhanging != null && entityhanging.onValidSurface()) {
                    // Places the painting entity
                    if (!world.isRemote) {
                        LogHelper.info("--Spawning custom painting at " + x + ", " + y + ", " + z);
                        world.spawnEntityInWorld(entityhanging);
                    }

                    // Use the item (reduces stack)
                    --stack.stackSize;
                }

                return true;
            }
        }

    }



    private EntityHanging createHangingEntity(World world, int x, int y, int z, int direction, String uuid)
    {
        return new EntityCustomPainting(world, x, y, z, direction, uuid);
    }


    /**
     * Injects the given stack with the NBT info corresponding to the given config element.
     * 
     * @param stack
     * @param configItem
     * @param getRandomAuthenticity
     *            Informs if the stack will get a random EnumAuthenticity value.
     *            By default paintings will be considered copies.
     */
    public void addPaintingData(ItemStack stack, CustomPaintingConfigItem configItem, boolean getRandomAuthenticity)
    {
        if (configItem != null && configItem.isValid()) {

            // Adds custom NBT
            final UUID uniqueId = configItem.getUUID();
            stack.setTagInfo(ItemCustomPainting.NBTPaintingUUID, new NBTTagString(uniqueId.toString()));

            if (!configItem.getPaintingTitleRaw().isEmpty()) {
                stack.setTagInfo(ItemCustomPainting.NBTPaintingTitle, new NBTTagString(configItem.getPaintingTitleRaw().trim()));
            }

            if (!configItem.getPaintingAuthorRaw().isEmpty()) {
                stack.setTagInfo(ItemCustomPainting.NBTPaintingAuthor, new NBTTagString(configItem.getPaintingAuthorRaw().trim()));
            }

            if (getRandomAuthenticity) {
                stack.setTagInfo(ItemCustomPainting.NBTPaintingAuthenticity, new NBTTagByte(EnumAuthenticity.getRandom().getId()));
            } else {
                stack.setTagInfo(ItemCustomPainting.NBTPaintingAuthenticity, new NBTTagByte(EnumAuthenticity.COPY.getId()));
            }

        }
    }



    // --------------------------------------------------------------------
    // Creative Tab
    // --------------------------------------------------------------------

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList)
    {
        ItemStack paintingStack;

        for (final CustomPaintingConfigItem configItem : ConfigurationHandler.getAllMahGoodPaintings()) {
            paintingStack = new ItemStack(item, 1);
            MyItems.customPainting.addPaintingData(paintingStack, configItem, false);

            itemList.add(paintingStack);
        }
    }

}
