package sidben.ateliercanvas.client.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.StringUtils;
import sidben.ateliercanvas.handler.ConfigurationHandler;
import sidben.ateliercanvas.reference.TextFormatTable;


/**
 * The object represents all config info about one Custom Painting.
 * 
 * @author sidben
 * 
 */
public class CustomPaintingConfigItem
{

    private final static int       EXPECTED_LENGTH  = 8;

    private final SimpleDateFormat sdfSave          = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdfDisplay       = new SimpleDateFormat(ConfigurationHandler.paintingDateFormat);
    private final String           ancientDate      = "1700-01-01";

    private String                 _fileName        = "";
    private String                 _uuid            = "";
    private boolean                _enabled         = false;
    private long                   _sizeBytes       = -1;
    private String                 _title           = "";
    private String                 _author          = "";
    private Date                   _creationDate    = new Date();
    private Date                   _lastUpdateDate  = new Date();

    private String                 validationErrors = "";



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
     * 
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
            this._uuid = _entryData[1];
            this._enabled = _entryData[2].equals("1");

            try {
                this._sizeBytes = Long.parseLong(_entryData[3]);
            } catch (final NumberFormatException e) {
                this._sizeBytes = -1;
            }

            this._title = _entryData[4];
            this._author = _entryData[5];

            try {
                this._creationDate = sdfSave.parse(_entryData[6]);
            } catch (final ParseException e) {
                this._creationDate = ancientTimes;
            }
            try {
                this._lastUpdateDate = sdfSave.parse(_entryData[7]);
            } catch (final ParseException e) {
                this._lastUpdateDate = this._creationDate;
            }

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
    public CustomPaintingConfigItem(String fileName, String uuid, boolean enabled, long fileSize, String title, String author, Date createDate, Date updateDate) {
        this._author = author;
        this._creationDate = createDate;
        this._enabled = enabled;
        this._fileName = fileName;
        this._uuid = uuid;
        this._lastUpdateDate = updateDate;
        this._sizeBytes = fileSize;
        this._title = title;
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
        this(fileName, UUID.randomUUID().toString(), enabled, fileSize, title, author, new Date(), new Date());
    }



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
     * @return TRUE if this object has the minimum expected values to be used.
     */
    public boolean isValid()
    {
        if (!this.validationErrors.isEmpty()) {
            // There are previous validation errors
            return false;
        }
        if (this._fileName.isEmpty()) {
            validationErrors = StatCollector.translateToLocal(this.getLanguageKey("error_empty_filename"));
            return false;
        }
        if (this._uuid.isEmpty()) {
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


        return new String[] { 
                this._fileName, 
                this._uuid, 
                (this._enabled ? "1" : "0"), 
                Long.toString(this._sizeBytes), 
                this._title, 
                this._author, 
                dateCreated,
                dateUpdated };
    }


    @Override
    public String toString()
    {
        return StringUtils.join(this.ToStringArray(), "|");
    }



    public String getPaintingFileName()
    {
        return this._fileName;
    }

    public String getUUID()
    {
        return this._uuid;
    }

    public boolean getIsEnabled()
    {
        return this._enabled;
    }

    public long getExpectedSize()
    {
        return this._sizeBytes;
    }

    public String getPaintingTitle()
    {
        return this._title.isEmpty() ? TextFormatTable.ITALIC + StatCollector.translateToLocal(this.getLanguageKey("title_empty")) + TextFormatTable.RESET : this._title;
    }

    public String getPaintingAuthor()
    {
        return this._author.isEmpty() ? TextFormatTable.ITALIC + StatCollector.translateToLocal(this.getLanguageKey("author_empty")) + TextFormatTable.RESET : this._author;
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



    /**
     * Returns the full language key for elements of this GUI.
     */
    protected String getLanguageKey(String name)
    {
        return "sidben.ateliercanvas.config.painting_info." + name;
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

        return r;
    }



}
