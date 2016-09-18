package rks.gentrexha.whereamiwhatstheweather;

import android.app.ListActivity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

public class historyActivity extends AppCompatActivity
{
    private ListView listView;
    DBHelper objDB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        objDB = new DBHelper(this);
        final Cursor objCursor = objDB.getAllLocations();

        String [] columns = new String[] {
                DBHelper.LOCATION_COLUMN_PLACE,
                DBHelper.LOCATION_COLUMN_WEATHER,
                DBHelper.LOCATION_COLUMN_TIME
        };

        int [] widgets = new int[] {
                R.id.place,
                R.id.weather,
                R.id.time
        };

        listView = (ListView)findViewById(R.id.list);
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,R.layout.location_info,objCursor,columns,widgets,0);
        listView.setAdapter(cursorAdapter);
    }
}
