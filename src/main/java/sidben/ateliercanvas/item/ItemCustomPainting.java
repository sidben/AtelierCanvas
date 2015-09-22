package sidben.ateliercanvas.item;

import java.util.List;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import sidben.ateliercanvas.entity.item.EntityCustomPainting;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.handler.CustomPaintingConfigItem;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.reference.BlockSide;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


// TODO - add CanvasID (damage value) and render it

public class ItemCustomPainting extends Item
{


    // --------------------------------------------------------------------
    // Constructors
    // --------------------------------------------------------------------
    public ItemCustomPainting() {
        this.setTextureName("painting");
        this.setUnlocalizedName("custom_painting");
    }



    // --------------------------------------------------------------------
    // Textures and Rendering
    // --------------------------------------------------------------------



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
            final String title = nbttagcompound.getString("title");

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
            final String author = nbttagcompound.getString("author");
            final boolean original = nbttagcompound.getBoolean("original");

            if (!StringUtils.isNullOrEmpty(author)) {
                infolist.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("sidben.ateliercanvas:item.custom_painting.author_label", new Object[] { author }));
            }

            // TODO: copy of a copy is a forgery (use byte instead of boolean)
            infolist.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal(original ? "sidben.ateliercanvas:item.custom_painting.original" : "sidben.ateliercanvas:item.custom_painting.copy"));

            // Debug info (F3 + H), check the UUID and if the painting is installed
            if (debugmode) {
                final String uniqueId = nbttagcompound.getString("uuid");

                if (StringUtils.isNullOrEmpty(uniqueId)) {
                    infolist.add(EnumChatFormatting.RED + "This painting is blank");
                } else {
                    infolist.add(EnumChatFormatting.GRAY + "" + EnumChatFormatting.ITALIC + uniqueId);

                    final CustomPaintingConfigItem paintingConfig = ConfigurationHandler.findPaintingByUUID(uniqueId);
                    if (paintingConfig == null || !paintingConfig.isValid()) {
                        infolist.add(EnumChatFormatting.RED + "This painting was not found on the config file");
                    }
                }
            }
        } else {
            // Debug info (F3 + H), check the UUID and if the painting is installed
            if (debugmode) {
                infolist.add(EnumChatFormatting.RED + "This painting is empty");
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
            final boolean original = nbttagcompound.getBoolean("original");

            if (original) {
                return EnumRarity.rare;
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
                uniqueId = nbttagcompound.getString("uuid");
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



}
