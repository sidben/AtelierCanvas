package sidben.ateliercanvas.handler;

import static sidben.ateliercanvas.handler.ConfigurationHandler.EMPTY_UUID;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.StringUtils;
import sidben.ateliercanvas.reference.TextFormatTable;


/**
 * The object represents all config info about one Custom Painting.
 * 
 * @author sidben
 * 
 */
public class CustomPaintingConfigItem
{

    private final static int       EXPECTED_LENGTH  = 10;

    private final SimpleDateFormat sdfSave          = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdfDisplay       = new SimpleDateFormat(ConfigurationHandler.paintingDateFormat);
    private final String           ancientDate      = "1700-01-01";

    private String                 _fileName        = "";
    private UUID                   _uuid;
    private boolean                _enabled         = false;
    private long                   _sizeBytes       = -1;
    private String                 _title           = "";
    private String                 _author          = "";
    private Date                   _creationDate    = new Date();
    private Date                   _lastUpdateDate  = new Date();
    private int                    _width           = ConfigurationHandler.minPaintingSize;
    private int                    _height          = ConfigurationHandler.minPaintingSize;

    private String                 validationErrors = "";



    // ----------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------

    /**
     * Creates a new config info object.
     * 
     * 
     * @param value
     *            Information about the custom painting. It's expected the following format:
     * 
     *            <ul>
     *            <li><b>[0]</b> - Image file name. String, required.</li>
     *            <li><b>[1]</b> - Painting UUID. Required (created automatically by the mod).</li>
     *            <li><b>[2]</b> - Painting enabled. Boolean, required.</li>
     *            <li><b>[3]</b> - File size, in bytes. Float, required.</li>
     *            <li><b>[4]</b> - Painting name. String, optional.</li>
     *            <li><b>[5]</b> - Painting author. String, optional.</li>
     *            <li><b>[6]</b> - Creation date. Date (format: YYYY-MM-DD), required.</li>
     *            <li><b>[7]</b> - Last update date. Date (format: YYYY-MM-DD), required. Initially, will be the same as the creation date.</li>
     *            <li><b>[8]</b> - Image width in pixels. Integer (if not valid, will be set at 16).</li>
     *            <li><b>[9]</b> - Image height in pixels. Integer (if not valid, will be set at 16).</li>
     * 
     *            <li><b>[?]</b> - Painting group / pack.</li>
     *            <li><b>[?]</b> - Player name that imported / created / uploaded the painting.</li>
     *            <li><b>[?]</b> - Player UUID that imported / created / uploaded the painting.</li>
     *            <li><b>[?]</b> - Painting lore. String, optional.</li>
     *            </ul>
     * 
     */
    public CustomPaintingConfigItem(String[] value) {
        final String[] _entryData = value;

        // Parse the value array
        if (this.isValidArray(_entryData)) {
            final Calendar cal = Calendar.getInstance();
            cal.set(1700, 0, 1);
            final Date ancientTimes = cal.getTime();


            this._fileName = _entryData[0];
            this._uuid = parseUUIDWithRandomDefault(_entryData[1]);
            this._enabled = parseBoolean(_entryData[2]);
            this._sizeBytes = parseLongWithDefault(_entryData[3], -1);
            this._title = _entryData[4];
            this._author = _entryData[5];
            this._creationDate = parseDateWithDefault(_entryData[6], ancientTimes);
            this._lastUpdateDate = parseDateWithDefault(_entryData[7], ancientTimes);
            this._width = parseIntWithDefault(_entryData[8], ConfigurationHandler.minPaintingSize, ConfigurationHandler.minPaintingSize);
            this._height = parseIntWithDefault(_entryData[9], ConfigurationHandler.minPaintingSize, ConfigurationHandler.minPaintingSize);
        }
    }


    /**
     * Creates a new config info object.
     * 
     * @param fileName
     *            Name of the painting image file.
     * @param uuid
     *            Unique identifier.
     * @param enabled
     *            Is the painting enabled in-game?
     * @param fileSize
     *            Size of the painting file, in bytes.
     * @param title
     *            Name of the painting.
     * @param author
     *            Author of the painting.
     * @param createDate
     *            Date when the painting was added to the mod.
     * @param updateDate
     *            Last time the painting config was updated.
     */
    public CustomPaintingConfigItem(String fileName, UUID uuid, boolean enabled, long fileSize, String title, String author, Date createDate, Date updateDate, int width, int height) {
        this._author = author;
        this._creationDate = createDate;
        this._enabled = enabled;
        this._fileName = fileName;
        this._uuid = uuid;
        this._lastUpdateDate = updateDate;
        this._sizeBytes = fileSize;
        this._title = title;
        this._width = width;
        this._height = height;
    }


    /**
     * Creates a new config info object. The unique identifier receives a new random value and the
     * create and last update dates are defined as the current date.
     * 
     * @param fileName
     *            Name of the painting image file.
     * @param enabled
     *            Is the painting enabled in-game?
     * @param fileSize
     *            Size of the painting file, in bytes.
     * @param title
     *            Name of the painting.
     * @param author
     *            Author of the painting.
     */
    public CustomPaintingConfigItem(String fileName, boolean enabled, long fileSize, String title, String author) {
        this(fileName, UUID.randomUUID(), enabled, fileSize, title, author, new Date(), new Date(), ConfigurationHandler.minPaintingSize, ConfigurationHandler.minPaintingSize);
    }



    // ----------------------------------------------------------------
    // Properties
    // ----------------------------------------------------------------

    public String getPaintingFileName()
    {
        return this._fileName;
    }

    public UUID getUUID()
    {
        return this._uuid == null ? EMPTY_UUID : this._uuid;
    }

    public String getStringUUID()
    {
        return this.getUUID().toString();
    }

    public boolean getIsEnabled()
    {
        return this._enabled;
    }

    public long getExpectedSize()
    {
        return this._sizeBytes;
    }

    /** Returns the painting title. If empty, will return a default text. */
    public String getPaintingTitle()
    {
        return this._title.isEmpty() ? TextFormatTable.ITALIC + StatCollector.translateToLocal(this.getLanguageKey("title_empty")) + TextFormatTable.RESET : this._title;
    }

    /** Returns the painting title, even if empty */
    public String getPaintingTitleRaw()
    {
        return this._title;
    }

    /** Returns the painting author. If empty, will return a default text. */
    public String getPaintingAuthor()
    {
        return this._author.isEmpty() ? TextFormatTable.ITALIC + StatCollector.translateToLocal(this.getLanguageKey("author_empty")) + TextFormatTable.RESET : this._author;
    }

    /** Returns the painting author, even if empty */
    public String getPaintingAuthorRaw()
    {
        return this._author;
    }

    public Date getCreationDate()
    {
        return this._creationDate;
    }

    public Date getLastUpdateDate()
    {
        return this._lastUpdateDate;
    }

    public String getFormatedCreationDate()
    {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(this.getCreationDate());

        if (cal.get(Calendar.YEAR) > 1700) {
            return this.sdfDisplay.format(this.getCreationDate());
        } else {
            return "-";
        }
    }

    public String getFormatedLastUpdateDate()
    {
        if (this.getLastUpdateDate().equals(this.getCreationDate())) {
            return "-";
        }

        final Calendar cal = Calendar.getInstance();
        cal.setTime(this.getLastUpdateDate());

        if (cal.get(Calendar.YEAR) > 1700) {
            return this.sdfDisplay.format(this.getCreationDate());
        } else {
            return "-";
        }
    }

    /** Returns the painting width in pixels. */
    public int getWidth()
    {
        return _width;
    }

    /** Returns the painting height in pixels. */
    public int getHeight()
    {
        return _height;
    }

    /** Returns the painting width in tiles. Each 'tile' is a 16x16 block, so a painting with 48x32 size in pixels would occupy 3x2 tiles */
    public int getTileWidth()
    {
        return (int) Math.ceil(this._width / 16);
    }

    /** Returns the painting height in tiles. Each 'tile' is a 16x16 block, so a painting with 48x32 size in pixels would occupy 3x2 tiles */
    public int getTileHeight()
    {
        return (int) Math.ceil(this._height / 16);
    }



    public void setPaintingTitle(String value)
    {
        this._title = value;
    }

    public void setPaintingAuthor(String value)
    {
        this._author = value;
    }

    public void setIsEnabled(boolean value)
    {
        this._enabled = value;
    }

    public void setSizePixels(int width, int height)
    {
        this._width = Math.max(width, ConfigurationHandler.minPaintingSize);
        this._height = Math.max(height, ConfigurationHandler.minPaintingSize);
    }

    public void setActualFileSize(long value)
    {
        this._sizeBytes = value;
    }



    /**
     * <p>
     * Updates this entry with values from the given config entry.
     * </p>
     * <p>
     * Values updates: Title, Author, Enabled, Width, Height
     * </p>
     */
    public void updateEntryFrom(CustomPaintingConfigItem entry)
    {
        this.setIsEnabled(entry.getIsEnabled());
        this.setPaintingAuthor(entry.getPaintingAuthorRaw());
        this.setPaintingTitle(entry.getPaintingTitleRaw());
        this.setSizePixels(entry.getWidth(), entry.getHeight());
        this.setActualFileSize(entry.getExpectedSize());
        this._lastUpdateDate = new Date();          // current date
    }



    // ----------------------------------------------------------------
    // Information about this entry integrity
    // ----------------------------------------------------------------

    /**
     * @return TRUE if this object has the minimum expected values to be used.
     */
    public boolean isValid()
    {
        if (!StringUtils.isBlank(this.validationErrors)) {
            // There are previous validation errors
            return false;
        }
        if (this._fileName.isEmpty()) {
            validationErrors = StatCollector.translateToLocal(this.getLanguageKey("error_empty_filename"));
            return false;
        }
        if (this._uuid == null) {
            validationErrors = StatCollector.translateToLocal(this.getLanguageKey("error_empty_uuid"));
            return false;
        }
        if (this._sizeBytes <= 0) {
            validationErrors = StatCollector.translateToLocal(this.getLanguageKey("error_invalid_filesize"));
            return false;
        }

        return true;
    }


    public String getValiadtionErrors()
    {
        return this.validationErrors;
    }



    public String[] ToStringArray()
    {
        String dateCreated = sdfSave.format(this._creationDate);
        String dateUpdated = sdfSave.format(this._lastUpdateDate);

        if (dateCreated.equals(ancientDate)) {
            dateCreated = "";
        }
        if (dateUpdated.equals(ancientDate)) {
            dateUpdated = "";
        }


        return new String[] { this._fileName, this._uuid.toString(), (this._enabled ? "1" : "0"), Long.toString(this._sizeBytes), this._title, this._author, dateCreated, dateUpdated,
                Integer.toString(this._width), Integer.toString(this._height) };
    }


    @Override
    public String toString()
    {
        return StringUtils.join(this.ToStringArray(), "|");
    }


    /**
     * Returns how the config array is expected to behave.
     */
    public static String getArrayDescription()
    {
        String r = "";

        r += "Each array entry is expected to have the following format.\n";
        r += "All fields are required, even if blank. Fields with [*] are an exception and can't be empty\n\n";
        r += "    File name [*] (only PNG files are accepted)\n";
        r += "    UUID (each entry must have a unique value. leave blank and the mod will create a new UUID)\n";
        r += "    Enabled (1 or 0) [*]\n";
        r += "    File size, in bytes (only numbers) [*]\n";
        r += "    Painting title\n";
        r += "    Author's name\n";
        r += "    Creation date (format yyyy-MM-dd)\n";
        r += "    Last update date (format yyyy-MM-dd)\n";
        r += "    Image width, in pixels\n";
        r += "    Image height, in pixels\n";

        return r;
    }



    // ----------------------------------------------------------------
    // Generic helpers
    // ----------------------------------------------------------------

    /**
     * @return TRUE if the entryData array has the minimum expected size.
     */
    private boolean isValidArray(String[] entryData)
    {
        if (entryData == null) {
            validationErrors = "Null config string array";
            return false;
        } else if (entryData.length < EXPECTED_LENGTH) {
            validationErrors = "Config array shorter than expected";
            return false;
        } else {
            return true;
        }
    }


    /**
     * Returns the full language key for elements of this GUI.
     */
    protected String getLanguageKey(String name)
    {
        return "sidben.ateliercanvas.config.painting_info." + name;
    }



    // ----------------------------------------------------------------
    // Parser helpers
    // ----------------------------------------------------------------
    private int parseIntWithDefault(String textValue, int defaultValue, int minimumValue)
    {
        try {
            return Math.max(Integer.parseInt(textValue.trim()), minimumValue);
        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    private long parseLongWithDefault(String textValue, long defaultValue)
    {
        try {
            return Long.parseLong(textValue.trim());
        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    private Date parseDateWithDefault(String textValue, Date defaultValue)
    {
        try {
            return sdfSave.parse(textValue);
        } catch (final ParseException e) {
            return defaultValue;
        }
    }

    private boolean parseBoolean(String textValue)
    {
        return textValue.equals("1");
    }

    /**
     * @return A random UUID if the given text is invalid.
     */
    private UUID parseUUIDWithRandomDefault(String textValue)
    {
        try {
            return UUID.fromString(textValue);
        } catch (final IllegalArgumentException e) {
            return UUID.randomUUID();
        }
    }

}
