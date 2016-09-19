package rks.gentrexha.whereamiwhatstheweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
    public static final String DB_NAME = "locations.db";
    private static final int DB_VERSION = 1;

    public static final String LOCATION_TABLE_NAME = "person";
    public static final String LOCATION_COLUMN_ID = "_id";
    public static final String LOCATION_COLUMN_PLACE = "place";
    public static final String LOCATION_COLUMN_WEATHER = "weather";
    public static final String LOCATION_COLUMN_TIME = "time";

    public DBHelper(Context context)
    {
        super(context, DB_NAME , null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + LOCATION_TABLE_NAME + "(" +
                LOCATION_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                LOCATION_COLUMN_PLACE + " TEXT, " +
                LOCATION_COLUMN_WEATHER + " TEXT, " +
                LOCATION_COLUMN_TIME + " TEXT)"
        );
    }

    public boolean insertLocation(String name, String weather, String time)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATION_COLUMN_PLACE, name);
        contentValues.put(LOCATION_COLUMN_WEATHER, weather);
        contentValues.put(LOCATION_COLUMN_TIME, time);
        db.insert(LOCATION_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getAllLocations()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + LOCATION_TABLE_NAME + " ORDER BY " + LOCATION_COLUMN_ID + " DESC", null );
        return res;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // In case the db needs to upgraded, we just drop and recreate
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME);
        onCreate(db);
    }
}
