package sidben.ateliercanvas.helper;

import java.io.File;
import java.io.FilenameFilter;


/**
 * Used to filter only the accepted image formats.
 * 
 * @author sidben
 */
public class ImageFilenameFilter implements FilenameFilter
{

    @Override
    public boolean accept(File dir, String filename)
    {
        final String extension = filename.substring(filename.lastIndexOf("."));
        return extension.equalsIgnoreCase(".PNG");
    }


    public static String acceptedExtensions()
    {
        return "PNG";
    }

}
