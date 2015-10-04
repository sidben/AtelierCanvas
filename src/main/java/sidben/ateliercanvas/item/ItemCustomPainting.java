package sidben.ateliercanvas.item;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.handler.CustomPaintingConfigItemComparator;
import sidben.ateliercanvas.handler.CustomPaintingConfigItemComparator.SortingType;
import sidben.ateliercanvas.helper.EnumAuthenticity;
import sidben.ateliercanvas.helper.LocalizationHelper;
import sidben.ateliercanvas.helper.LocalizationHelper.Category;
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
    public static final String unlocalizedName   = "custom_painting";
    public static final String NBTPaintingUUID   = "uuid";
    public static final String NBTPaintingTitle  = "title";
    public static final String NBTPaintingAuthor = "author";
    public static final String NBTPaintingSize   = "size";



    private IIcon[]            iconArray;

    private static final int   idxDefaultIcon    = 0;
    private static final int   idxShinyIcon      = 1;



    // --------------------------------------------------------------------
    // Constructors
    // --------------------------------------------------------------------
    public ItemCustomPainting() {
        this.setHasSubtypes(true);
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
        iconArray = new IIcon[2];

        iconArray[idxDefaultIcon] = iconRegister.registerIcon(MyItems.customPaintingIcon);
        iconArray[idxShinyIcon] = iconRegister.registerIcon(MyItems.customPaintingShinyIcon);
    }


    /**
     * Gets an icon index based on an item's damage value
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        if (damage == EnumAuthenticity.ORIGINAL.getId()) {
            return this.iconArray[idxShinyIcon];
        }
        return this.iconArray[idxDefaultIcon];
    }



    // ----------------------------------------------------
    // Item name and flavor text
    // ----------------------------------------------------

    @Override
    public String getUnlocalizedName()
    {
        return LocalizationHelper.getLanguageKey(LocalizationHelper.Category.ITEM, unlocalizedName);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return LocalizationHelper.getLanguageKey(LocalizationHelper.Category.ITEM, unlocalizedName);
    }



    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.hasTagCompound()) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            final String title = nbttagcompound.getString(ItemCustomPainting.NBTPaintingTitle);

            if (!StringUtils.isNullOrEmpty(title)) {
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
            // TODO: how to update author and title when the config changes? Update when the painting is placed / broken

            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            final String author = nbttagcompound.getString(ItemCustomPainting.NBTPaintingAuthor);
            final byte qualityId = (byte) stack.getItemDamage();

            // Author
            if (!StringUtils.isNullOrEmpty(author)) {
                infolist.add(TextFormatTable.COLOR_GRAY + LocalizationHelper.translateFormatted(Category.ITEM_CUSTOM_PAINTING, "author", author));
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
                    infolist.add(TextFormatTable.COLOR_RED + LocalizationHelper.translate(Category.WARNING, "empty_uuid"));
                } else {
                    infolist.add(TextFormatTable.COLOR_GRAY + TextFormatTable.ITALIC + uniqueId);

                    final CustomPaintingConfigItem paintingConfig = ConfigurationHandler.findPaintingByUUID(uniqueId);
                    if (paintingConfig == null || !paintingConfig.isValid()) {
                        infolist.add(TextFormatTable.COLOR_RED + LocalizationHelper.translate(Category.WARNING, "uuid_not_found"));
                    } else if (!paintingConfig.getIsEnabled()) {
                        infolist.add(TextFormatTable.COLOR_RED + LocalizationHelper.translate(Category.WARNING, "disabled"));
                    }

                }
            }
        } else {
            // Debug info (F3 + H), check the UUID and if the painting is installed
            if (debugmode) {
                infolist.add(TextFormatTable.COLOR_RED + LocalizationHelper.translate(Category.WARNING, "empty_nbt"));
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
            final int quality = stack.getItemDamage();

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

            
            // Check if the UUID is valid and enabled. If not, don't place the painting.
            if (!ConfigurationHandler.isUUIDEnabled(uniqueId)) {
                return false;
            }
            

            final int facing = Direction.facingToDirection[side];
            final EntityHanging entityhanging = this.createHangingEntity(world, x, y, z, facing, uniqueId, stack.getItemDamage());

            
            // Check if the player can place blocks
            if (!player.canPlayerEdit(x, y, z, side, stack)) {
                return false;
            } else {
                if (entityhanging != null && entityhanging.onValidSurface()) {
                    // Places the painting entity
                    if (!world.isRemote) {
                        world.spawnEntityInWorld(entityhanging);
                    }

                    // Use the item (reduces stack)
                    --stack.stackSize;
                }

                return true;
            }
        }

    }



    private EntityHanging createHangingEntity(World world, int x, int y, int z, int direction, String uuid, int damageValue)
    {
        return new EntityCustomPainting(world, x, y, z, direction, uuid, damageValue);
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
                stack.setItemDamage(EnumAuthenticity.getRandom().getId());
            } else {
                stack.setItemDamage(EnumAuthenticity.COPY.getId());
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

        // Sorts the config list by painting title
        List<CustomPaintingConfigItem> configList = ConfigurationHandler.getAllMahGoodPaintings();
        Collections.sort(configList, new CustomPaintingConfigItemComparator(SortingType.TITLE));
        
        // Adds each painting to the creative menu, if they are enabled
        for (final CustomPaintingConfigItem configItem : configList) {
            if (ConfigurationHandler.isUUIDEnabled(configItem.getUUID())) {
                paintingStack = new ItemStack(item, 1);
                MyItems.customPainting.addPaintingData(paintingStack, configItem, false);
    
                itemList.add(paintingStack);
            }
        }
    }

}
