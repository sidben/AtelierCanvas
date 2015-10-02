package sidben.ateliercanvas.command;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import sidben.ateliercanvas.helper.AtelierHelper;
import sidben.ateliercanvas.helper.LogHelper;
import sidben.ateliercanvas.init.MyItems;
import sidben.ateliercanvas.world.storage.PaintingData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


@Deprecated
public class CommandAtelier extends CommandBase
{

    @Override
    public String getCommandName()
    {
        return "atelier";
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.atelier.usage";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String[] params)
    {
        if (params.length > 1) {
            if (params[0].equals(addKeyword)) {

                
                throw new WrongUsageException("Command disabled");

                /* 
                 * Adds a custom painting to the current world.
                 * 
                 * expects: /atelier addImage <url> [name] [author]
                 */
                
                
                /*
                URL url = null;
                BufferedImage image = null;
                String name = "";
                String author = "";
                World world = par1ICommandSender.getEntityWorld();

                
                try {
                    url = new URL(params[1]);
                } catch (MalformedURLException e) {
                    LogHelper.error(e.getMessage());
                    func_152373_a(par1ICommandSender, this, "commands.atelier.url_format", new Object[0]);
                    return;
                }
                
                try {
                    image = ImageIO.read(url);
                } catch (IOException e) {
                    LogHelper.error(e.getMessage());
                    func_152373_a(par1ICommandSender, this, "commands.atelier.image_load", new Object[0]);
                    return;
                }
                

                if (params.length > 2) name = params[2];
                if (params.length > 3) author = params[3];
                
                

                if (image != null) {
                    int w = image.getWidth();
                    int h = image.getHeight();
                    int[] imageData = new int[w * h];
                    
                    image.getRGB(0, 0, w, h, imageData, 0, w);
                    
                    int canvasId = AtelierHelper.addPaintingData(world, author, name, imageData);


                    if (par1ICommandSender instanceof EntityPlayerMP) {
                        EntityPlayerMP player = (EntityPlayerMP)par1ICommandSender;
                        ItemStack item = new ItemStack(MyItems.customPainting, 1, canvasId);

                        EntityItem entityitem = player.dropPlayerItemWithRandomChoice(item, false);
                        entityitem.delayBeforeCanPickup = 0;
                        entityitem.func_145797_a(player.getCommandSenderName());
                    }
                    

                    
                    func_152373_a(par1ICommandSender, this, "commands.atelier.success", new Object[0]);
                    return;
                    
                }
                else {
                    func_152373_a(par1ICommandSender, this, "commands.atelier.image_null", new Object[0]);
                    return;

                }
                
                */
            }

        }
        
        throw new WrongUsageException("commands.atelier.usage", new Object[0]);
    }
    
    
    private final String addKeyword = "addImage";
    

}
