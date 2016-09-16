package rks.gentrexha.whereamiwhatstheweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class infoActivity extends AppCompatActivity
{
    private String mLatitudeText = "NOSENTVALUE";
    private String mLongitudeText = "NOSENTVALUE";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TextView txvLat = (TextView)findViewById(R.id.txvLat);
        TextView txvLong = (TextView)findViewById(R.id.txvLong);
        Bundle objBundle = getIntent().getExtras();
        mLatitudeText = objBundle.getString("Lat");
        mLongitudeText = objBundle.getString("Long");
        txvLat.setText(mLatitudeText);
        txvLong.setText(mLongitudeText);
    }
}
