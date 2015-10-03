package sidben.ateliercanvas.handler;

import java.util.Comparator;


public class CustomPaintingConfigItemComparator implements Comparator<CustomPaintingConfigItem>
{



    private final SortingType _sortType;



    public CustomPaintingConfigItemComparator() {
        this(SortingType.SIZE);
    }

    public CustomPaintingConfigItemComparator(SortingType sortType) {
        this._sortType = sortType;
    }



    /**
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    @Override
    public int compare(CustomPaintingConfigItem item1, CustomPaintingConfigItem item2)
    {
        int result = 0;

        switch (this._sortType) {
            case SIZE:
                result = compareBySize(item1, item2);
                if (result == 0) result = compareByTitle(item1, item2);
                if (result == 0) result = compareByAuthor(item1, item2);
                break;

            case AUTHOR:
                result = compareByAuthor(item1, item2);
                if (result == 0) result = compareBySize(item1, item2);
                if (result == 0) result = compareByTitle(item1, item2);
                break;

            case TITLE:
                result = compareByTitle(item1, item2);
                if (result == 0) result = compareBySize(item1, item2);
                if (result == 0) result = compareByAuthor(item1, item2);
                break;

        }

        return result;
    }



    private int compareBySize(CustomPaintingConfigItem item1, CustomPaintingConfigItem item2)
    {
        if (item1.getTileWidth() == item2.getTileWidth() && item1.getTileHeight() == item2.getTileHeight()) {
            return 0;
        }

        else if (item1.getTileWidth() < item2.getTileWidth() && item1.getTileHeight() < item2.getTileHeight()) {
            return -1;
        }

        else if (item1.getTileWidth() > item2.getTileWidth() && item1.getTileHeight() > item2.getTileHeight()) {
            return 1;
        }


        else if (item1.getTileWidth() == item2.getTileWidth() && item1.getTileHeight() < item2.getTileHeight()) {
            return -1;
        }

        else if (item1.getTileWidth() == item2.getTileWidth() && item1.getTileHeight() > item2.getTileHeight()) {
            return 1;
        }

        else if (item1.getTileWidth() < item2.getTileWidth() && item1.getTileHeight() == item2.getTileHeight()) {
            return -1;
        }

        else if (item1.getTileWidth() > item2.getTileWidth() && item1.getTileHeight() == item2.getTileHeight()) {
            return 1;
        }

        return 0;
    }


    private int compareByTitle(CustomPaintingConfigItem item1, CustomPaintingConfigItem item2)
    {
        return item1.getPaintingTitleRaw().compareToIgnoreCase(item2.getPaintingTitleRaw());
    }


    private int compareByAuthor(CustomPaintingConfigItem item1, CustomPaintingConfigItem item2)
    {
        return item1.getPaintingAuthorRaw().compareToIgnoreCase(item2.getPaintingAuthorRaw());
    }



    public enum SortingType {

        /** Sort by painting tile size, then author, then title */
        SIZE,

        /** Sort by painting title, then size, then author */
        TITLE,

        /** Sort by painting author, then size, then title */
        AUTHOR
    }

}
